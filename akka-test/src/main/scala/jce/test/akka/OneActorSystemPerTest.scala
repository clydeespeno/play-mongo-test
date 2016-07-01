package jce.test.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{BeforeAndAfterAll, Suite}
import scala.concurrent.Await
import scala.concurrent.duration._

trait OneActorSystemPerTest extends BeforeAndAfterAll with StrictLogging { this: Suite =>


  private def createActorSystem(): ActorSystem = {
    val config = ConfigFactory.parseResources(akkaTestConfigFile)
    ActorSystem(akkaTestSystemName, config)
  }

  abstract override protected def afterAll(): Unit = {
    logger.info("Stopping actor system")
    synchronized(Await.result(system.terminate(), Long.MaxValue.nanos))
    super.afterAll()
  }

  final implicit val system = createActorSystem()

  final def actorOf[T <: Actor](c: Class[T], args: Any*): ActorRef = system.actorOf(Props(c, args: _*))

  def akkaTestConfigFile = "akka-test.conf"

  def akkaTestSystemName = "test-system"
}
