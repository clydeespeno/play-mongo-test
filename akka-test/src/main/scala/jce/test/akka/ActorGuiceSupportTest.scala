package jce.test.akka

import akka.actor._
import com.google.inject._
import com.google.inject.name.Names
import org.scalatest.Suite

trait ActorGuiceSupportTest extends OneActorSystemPerTest { this: Suite =>

  final def injector: Injector = synchronized(guiceInjector)

  def modules: Seq[Module] = Seq[Module]()

  def getActorRef(name: String): ActorRef = {
    actorSystem.actorOf(Props(classOf[ActorFetcher], injector, Key.get(classOf[Actor], Names.named(name))))
  }

  abstract override protected def beforeAll(): Unit = {
    super.beforeAll()
    logger.info("creating guice factory")
    synchronized(guiceInjector = createInjector())
  }

  private var guiceInjector: Injector = _

  private def createInjector(): Injector = Guice.createInjector(modules :+ actorModule: _*)

  private def actorModule = new AbstractModule {
    override def configure(): Unit = bind(classOf[ActorSystem]).toInstance(actorSystem)
  }
}

class ActorFetcher(injector: Injector, key: Key[Actor]) extends IndirectActorProducer {
  override def actorClass: Class[_ <: Actor] = classOf[Actor]
  override def produce(): Actor = injector.getInstance(key)
}
