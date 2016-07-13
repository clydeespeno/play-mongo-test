val playVersion = "2.5.3"

val scalaOptions = Seq(
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

val publishRepo = sys.props.get("repo").map(repo => "repo" at repo).getOrElse(Resolver.mavenLocal)

val commonSettings = scalaOptions ++ Seq(
  version := "0.3",
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

lazy val playTestApp = (project in file("./play-test-app")).settings(scalaOptions: _*)
  .settings(publishArtifact := false)
  .settings(
    name := "play-test-app-example",
    libraryDependencies ++= Seq(
      "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13"
    ),
    routesGenerator := InjectedRoutesGenerator
  )
  .dependsOn(root % "test->compile")
  .enablePlugins(PlayScala)

lazy val root = (project in file(".")).settings(commonSettings: _*)
  .settings(name := "play-mongo-test")
  .dependsOn(reactiveMongoTest, playTest, akkaTest)
  .aggregate(reactiveMongoTest, playTest, mongoTest, akkaTest)

lazy val reactiveMongoTest = (project in file("./reactivemongo-test")).settings(commonSettings: _*)
  .dependsOn(mongoTest, playTest)
  .settings(
    name := "reactivemongo-test",
    libraryDependencies ++= Seq(
      "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13"
    )
  )

lazy val playTest = (project in file("./play-test")).settings(commonSettings: _*)
  .dependsOn(akkaTest)
  .settings(
    name := "play-test",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % playVersion,
      "com.typesafe.play" %% "play-test" % playVersion,
      "org.mockito" % "mockito-core" % "1.10.19"
    )
  )

lazy val mongoTest = (project in file("./mongo-test")).settings(commonSettings: _*)
  .settings(
    name := "mongo-test",
    libraryDependencies ++= Seq(
      "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.50.5" % Compile
    )
  )

lazy val akkaTest = (project in file("./akka-test")).settings(commonSettings: _*)
  .settings(
    name := "akka-test",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.4" % Compile,
      "com.google.inject" % "guice" % "4.0" % Compile
    )
  )
