package actors

import akka.actor.Actor

class FooActor extends Actor {
  override def receive: Receive = {
    case "foo" =>
      sender() ! "bar"
      context.stop(self)
  }
}

object FooActor {
  trait Factory {
    def apply(): Actor
  }
}
