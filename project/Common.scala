import sbt.Keys._
import sbt._
import DependencyVersion.appVersion

object Common {

  lazy val scalaOptions = Seq(
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )

  lazy val publishRepo = sys.props.get("repo").map(repo => "repo" at repo).getOrElse(Resolver.mavenLocal)

  lazy val commonSettings = scalaOptions ++ Seq(
    version := appVersion,
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
}