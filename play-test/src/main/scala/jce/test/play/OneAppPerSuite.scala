package jce.test.play

import java.io.File

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{BeforeAndAfterAll, Suite, SuiteMixin}
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}
import play.api.{Application, Environment, Mode}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


trait OneAppPerSuite extends SuiteMixin with BeforeAndAfterAll with StrictLogging {
  this: Suite =>

  private val longTime = Long.MaxValue.nanos

  private def awaitResult[T](f: Future[T]): T =
    Await.result(f, longTime)

  private var appPerTest: Application = _

  abstract override protected def beforeAll(): Unit = {
    super.beforeAll()
    logger.info(s"Starting a play application")
    synchronized(appPerTest = createApp())
  }

  abstract override protected def afterAll(): Unit = {
    logger.info(s"Killing instantiated play app")
    awaitResult(appPerTest.stop())
    super.afterAll()
  }

  def createApp(): Application = new GuiceApplicationBuilder()
    .in(new Environment(new File("."), getClass.getClassLoader, Mode.Test))
    .configure(extraConfig)
    .disable(disables: _*)
    .overrides(overrides: _*)
    .build()

  def extraConfig: Map[String, Any] = Map.empty

  def overrides: Seq[GuiceableModule] = Seq.empty

  def disables: Seq[Class[_]] = Seq.empty

  final implicit def app: Application = synchronized(appPerTest)
}