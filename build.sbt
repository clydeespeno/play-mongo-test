import Common._
import Versions._

lazy val playTestApp = (project in file("./examples/play-test-app")).settings(scalaOptions: _*)
  .settings(publishArtifact := false)
  .settings(
    name := "play-test-app-example",
    libraryDependencies ++= Seq(
      "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion
    ),
    routesGenerator := InjectedRoutesGenerator
  )
  .dependsOn(root % "test->compile")
  .enablePlugins(PlayScala)

lazy val root = (project in file(".")).settings(commonSettings: _*)
  .settings(name := "play-test")
  .dependsOn(reactiveMongoTest, playTestUtils, akkaTest)
  .aggregate(reactiveMongoTest, playTestUtils, mongoTest, akkaTest)

lazy val reactiveMongoTest = (project in file("./modules/reactivemongo-test")).settings(commonSettings: _*)
  .dependsOn(mongoTest, playTestUtils)
  .settings(
    name := "reactivemongo-test",
    version := s"$appVersion-rm$reactiveMongoVersion",
    libraryDependencies ++= Seq(
      "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion
    )
  )

lazy val playTestUtils = (project in file("./modules/play-test-utils")).settings(commonSettings: _*)
  .dependsOn(akkaTest)
  .settings(
    name := "play-test-utils",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % playVersion,
      "com.typesafe.play" %% "play-test" % playVersion,
      "org.mockito" % "mockito-core" % "2.2.21"
    )
  )

lazy val mongoTest = (project in file("./modules/mongo-test")).settings(commonSettings: _*)
  .settings(
    name := "mongo-test",
    libraryDependencies ++= Seq(
      "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.50.5" % Compile
    )
  )

lazy val akkaTest = (project in file("./modules/akka-test")).settings(commonSettings: _*)
  .settings(
    name := "akka-test",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.10" % Compile,
      "com.google.inject" % "guice" % "4.0" % Compile
    )
  )
