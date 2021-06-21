package com.github.keyno.caliban.akkahttp.app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import caliban.interop.circe.AkkaHttpCirceAdapter
import com.github.keyno.caliban.graphql.ExampleApi
import com.github.keyno.caliban.graphql.data.ExampleData.sampleCharacters
import com.github.keyno.caliban.graphql.service.ExampleService
import com.github.keyno.caliban.graphql.service.ExampleService.ExampleService
import zio.clock.Clock
import zio.console.Console
import zio.Runtime
import zio.internal.Platform

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object SandboxApp extends scala.App with AkkaHttpCirceAdapter {
  implicit val system: ActorSystem                                      = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor               = system.dispatcher
  implicit val runtime: Runtime[ExampleService with Console with Clock] =
    Runtime.unsafeFromLayer(ExampleService.make(sampleCharacters) ++ Console.live ++ Clock.live, Platform.default)

  val interpreter = runtime.unsafeRun(ExampleApi.api.interpreter)

  val route =
    path("api" / "graphql") {
      adapter.makeHttpService(interpreter)
    } ~ path("ws" / "graphql") {
      adapter.makeWebSocketService(interpreter)
    } ~ path("graphiql") {
      getFromResource("graphiql.html")
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
  println(s"Server online at http://localhost:8088/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
