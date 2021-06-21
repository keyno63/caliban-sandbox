import sbt.Keys.libraryDependencies

name := "caliban-sandbox"

version := "0.1"

scalaVersion := "2.13.6"

lazy val `caliban-sandbox` = project
  .in(file("."))
  .settings(
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-deprecation"
    ),
    libraryDependencies ++= calibanLibs
  )

val calibanVersion = "1.0.1"

val calibanLibs = Seq(
  "com.github.ghostdogpr" %% "caliban",
  "com.github.ghostdogpr" %% "caliban-akka-http"
).map(_ % calibanVersion)
