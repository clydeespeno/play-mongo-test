package jce.test.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect.ClassTag

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

  def actorOf[T <: Actor : ClassTag](args: Any*): ActorRef = createActorRef[T](args)()

  def actorOf[T <: Actor : ClassTag](name: String, args: Any*) = createActorRef[T](args)(name)

  private def createActorRef[T <: Actor : ClassTag](args: Any*)(name: String = ""): ActorRef = {
    val props = Props(implicitly[ClassTag[T]].runtimeClass, args: _*)
    name match {
      case "" => system.actorOf(props)
      case _ => system.actorOf(props, name)
    }
  }

  def akkaTestConfigFile = "akka-test.conf"

  def akkaTestSystemName = "test-system"
}
