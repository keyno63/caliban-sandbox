import sbt.Keys.libraryDependencies

name := "caliban-sandbox"

version := "0.1"

scalaVersion := "2.13.6"

Compile / herokuAppName := "keyno-scala-sandbox"

run / fork := true

lazy val `caliban-sandbox` = project
  .in(file("."))
  .settings(
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-deprecation"
    ),
    libraryDependencies ++=
      calibanLibs ++
        zioTestLibs
  )

val calibanVersion   = "1.0.1"
val scalaTestVersion = "3.2.9"

val calibanLibs = Seq(
  "com.github.ghostdogpr" %% "caliban",
  "com.github.ghostdogpr" %% "caliban-akka-http"
).map(_                  % calibanVersion) ++
  Seq(
    "de.heikoseeberger" %% "akka-http-circe" % "1.36.0"
  )

val testLibs = Seq(
  "org.scalatest" %% "scalatest"
).map(_ % scalaTestVersion)

val zioTestLibs = Seq(
  "dev.zio"  %% "zio-test"
).map(_ % "1.0.9")


addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

Universal / javaOptions ++= Seq("-Dlog4j2.formatMsgNoLookups=true")
