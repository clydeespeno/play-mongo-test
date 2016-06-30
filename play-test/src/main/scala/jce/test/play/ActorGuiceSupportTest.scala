package jce.test.play

import akka.actor._
import com.google.inject._
import com.google.inject.name.Names
import jce.test.akka.OneActorSystemPerTest
import org.scalatest.Suite
import play.api.inject.guice.GuiceInjector

trait ActorGuiceSupportTest extends OneActorSystemPerTest { this: Suite =>

  final def injector: Injector = synchronized(guiceInjector)

  def modules: Seq[Module] = Seq[Module]()

  def getNamedActor(name: String) = injector.getInstance(Key.get(classOf[ActorRef], Names.named(name)))

  abstract override protected def beforeAll(): Unit = {
    super.beforeAll()
    logger.info("creating guice factory")
    synchronized(guiceInjector = createInjector())
  }

  private var guiceInjector: Injector = _

  private def createInjector(): Injector = Guice.createInjector(modules :+ actorModule: _*)

  private def actorModule = new AbstractModule {
    override def configure(): Unit = {
      bind(classOf[ActorSystem]).toInstance(actorSystem)
      bind(classOf[play.api.inject.Injector]).to(classOf[GuiceInjector])
    }
  }
}
