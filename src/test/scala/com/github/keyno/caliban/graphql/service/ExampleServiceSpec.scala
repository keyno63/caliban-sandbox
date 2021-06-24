package com.github.keyno.caliban.graphql.service

import com.github.keyno.caliban.graphql.data.ExampleData.{Character, sampleCharacters}
import com.github.keyno.caliban.graphql.data.ExampleData.Origin._
import com.github.keyno.caliban.graphql.service.ExampleService.ExampleService
import zio.{Has, Managed, UIO, ULayer, URIO, ZIO, ZLayer}
import zio.ZManaged.blocking
import zio.test.Annotations.live
import zio.test.Assertion._
import zio.test.Gen.sized
import zio.test.environment.TestEnvironment
import zio.test.environment.TestRandom.random
import zio.test.{DefaultRunnableSpec, RunnableSpec, ZSpec, assert, assertM}

import java.lang.System.console

object ExampleServiceSpec extends DefaultRunnableSpec {
  type ServiceEnv = ZSpec[ExampleService, Any]
  private val serviceM: ULayer[ExampleService] =
    ExampleService.make(sampleCharacters)

  override def spec: ZSpec[TestEnvironment, Any] = ???
}

