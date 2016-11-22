val playVersion = "2.5.9"
val reactiveMongoVersion = "0.12.0"

val scalaOptions = Seq(
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

val publishRepo = sys.props.get("repo").map(repo => "repo" at repo).getOrElse(Resolver.mavenLocal)

val commonSettings = scalaOptions ++ Seq(
  version := "0.4.1",
  organization := "jce.tools",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.4",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
    "Maven Central" at "http://central.maven.org/maven2/",
    "SpinGo OSS" at "http://spingo-oss.s3.amazonaws.com/repositories/releases"
  ),
  publishTo := Some(publishRepo)
)

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
  .settings(name := "play-mongo-test")
  .dependsOn(reactiveMongoTest, playTestUtils, akkaTest)
  .aggregate(reactiveMongoTest, playTestUtils, mongoTest, akkaTest)

lazy val reactiveMongoTest = (project in file("./modules/reactivemongo-test")).settings(commonSettings: _*)
  .dependsOn(mongoTest, playTestUtils)
  .settings(
    name := "reactivemongo-test",
    version := reactiveMongoVersion,
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
