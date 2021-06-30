package com.github.keyno.caliban.graphql.service

import com.github.keyno.caliban.graphql.data.ExampleData.{Character, sampleCharacters}
import com.github.keyno.caliban.graphql.data.ExampleData.Origin._
import com.github.keyno.caliban.graphql.service.ExampleService.ExampleService
import zio.{Has, Managed, Runtime, UIO, ULayer, URIO, ZIO, ZLayer}
import zio.ZManaged.blocking
import zio.clock.Clock
import zio.console.Console
import zio.internal.Platform
import zio.test.Annotations.live
import zio.test._
import zio.test.Assertion._
import zio.test.Gen.sized
import zio.test.environment.TestEnvironment
import zio.test.environment.TestRandom.random
import zio.test.{DefaultRunnableSpec, RunnableSpec, TestAspect, ZSpec, assert, assertM}

import java.lang.System.console

object ExampleServiceSpec extends DefaultRunnableSpec {
  type TestEnvSpec = ZSpec[TestEnvironment, Any]
  // test env
  type ServiceEnv = ExampleService with TestEnvironment
  type ServiceSpecEnv = ZSpec[ServiceEnv, Any]
  def serviceSpec: ZSpec[ServiceEnv, Any]

  private val dependencies: ULayer[ExampleService] = {
    ExampleService.make(sampleCharacters)
  }

  // define spec function to exec test
  override def spec: ZSpec[TestEnvironment, Any] = {
    serviceSpec.provideCustomLayer(dependencies) @@ TestAspect.sequential
  }

  // test
//  val serviceSpec: ServiceSpecEnv = suite("test suite")(
//    assert(ExampleService.getCharacters(Some(EARTH)))(
//      equalTo(List(Character("", List.empty, EARTH, None)))
//    )
//  ).provideSomeLayer(ExampleService.make(sampleCharacters))

  val test2: ServiceSpecEnv = suite("")(
    testM("")(
      for {
        repository <- ZIO.service[ExampleService]
        ret <- repository.get[Service].getCharacters(Some(EARTH))
      } yield assert(ret)(equalTo(List(Character("", List.empty, EARTH, None))))
    )
  ).provideSomeLayer()
}

trait ServiceSpec extends DefaultRunnableSpec {
  type ServiceEnv = ExampleService with TestEnvironment
  def serviceSpec: ZSpec[ServiceEnv, Any]

  private val dependencies: ULayer[ExampleService] = {
    ExampleService.make(sampleCharacters)
  }

  override def spec: ZSpec[TestEnvironment, Any] = {
    serviceSpec.provideCustomLayer(dependencies) @@ TestAspect.sequential
  }
}

