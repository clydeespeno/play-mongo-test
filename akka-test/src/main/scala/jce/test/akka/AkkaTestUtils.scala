package jce.test.akka

import akka.actor.ActorRef

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.ClassTag

trait AkkaTestUtils {
  implicit class AwaitingResultActor(ref: ActorRef) {
    import akka.pattern.ask

    def askAndGet[T](
        any: Any, duration: Duration = VeryLongTime)(
        implicit ctx: ExecutionContext, classTag: ClassTag[T]): T =
      awaitResult((ref ? any).mapTo[T])
  }

  def awaitResult[T](f: Future[T], duration: Duration = VeryLongTime): T = {
    Await.result(f, duration)
  }

  val VeryLongTime = Long.MaxValue.nanos
}
