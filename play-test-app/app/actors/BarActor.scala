package actors

import javax.inject.Inject

import akka.actor.{Actor, ActorRef}
import play.api.libs.concurrent.InjectedActorSupport

import scala.util.Random

class BarActor @Inject()(
  fooActorFactory: FooActor.Factory
) extends Actor with InjectedActorSupport {

  var client: ActorRef = self

  override def receive: Receive = {
    case "baz" =>
      client = sender()
      fooActor() ! "foo"
    case "bar" =>
      client ! "baz"
  }

  def fooActor() = injectedChild(fooActorFactory(), s"foo-actor-${Random.nextDouble()}")
}
