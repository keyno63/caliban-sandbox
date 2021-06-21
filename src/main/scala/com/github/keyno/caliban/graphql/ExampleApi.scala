package com.github.keyno.caliban.graphql

import caliban.{ GraphQL, RootResolver }
import caliban.GraphQL.graphQL
import caliban.schema.Annotations.{ GQLDeprecated, GQLDescription }
import caliban.schema.Schema.gen
import caliban.wrappers.ApolloTracing.apolloTracing
import caliban.wrappers.Wrappers.{ maxDepth, maxFields, printSlowQueries, timeout }
import com.github.keyno.caliban.graphql.data.ExampleData.{ CharacterArgs, CharactersArgs, Role }
import com.github.keyno.caliban.graphql.service.ExampleService
import com.github.keyno.caliban.graphql.service.ExampleService.ExampleService
import zio.URIO
import zio.clock.Clock
import zio.console.Console
import zio.stream.ZStream
import com.github.keyno.caliban.graphql.data.ExampleData.Character
import zio.duration.durationInt

import scala.language.postfixOps

object ExampleApi {
  case class Queries(
    @GQLDescription("Return all characters from a given origin")
    characters: CharactersArgs => URIO[ExampleService, List[Character]],
    @GQLDeprecated("Use `characters`")
    character: CharacterArgs => URIO[ExampleService, Option[Character]]
  )
  case class Mutations(deleteCharacter: CharacterArgs => URIO[ExampleService, Boolean])
  case class Subscriptions(characterDeleted: ZStream[ExampleService, Nothing, String])

  implicit val roleSchema           = gen[Role]
  implicit val characterSchema      = gen[Character]
  implicit val characterArgsSchema  = gen[CharacterArgs]
  implicit val charactersArgsSchema = gen[CharactersArgs]

  val api: GraphQL[Console with Clock with ExampleService] =
    graphQL(
      RootResolver(
        Queries(
          args => ExampleService.getCharacters(args.origin),
          args => ExampleService.findCharacter(args.name)
        ),
        Mutations(args => ExampleService.deleteCharacter(args.name)),
        Subscriptions(ExampleService.deletedEvents)
      )
    ) @@
      maxFields(200) @@               // query analyzer that limit query fields
      maxDepth(30) @@                 // query analyzer that limit query depth
      timeout(3 seconds) @@           // wrapper that fails slow queries
      printSlowQueries(500 millis) @@ // wrapper that logs slow queries
      apolloTracing                   // wrapper for https://github.com/apollographql/apollo-tracing
}
