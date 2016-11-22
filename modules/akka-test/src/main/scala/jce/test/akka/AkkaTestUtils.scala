package jce.test.akka

import akka.actor.ActorRef
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.ClassTag

trait AkkaTestUtils {
  implicit class AwaitingResultActor(ref: ActorRef) {
    import akka.pattern.ask

    def askAndGet[T](
        any: Any, duration: FiniteDuration = VeryLongTime)(
        implicit ctx: ExecutionContext, classTag: ClassTag[T]): T =
      awaitResult(ref.?(any)(timeout = Timeout(duration)).mapTo[T])
  }

  def awaitResult[T](f: Future[T], duration: FiniteDuration = VeryLongTime): T = {
    Await.result(f, duration)
  }

  val VeryLongTime = 91250.days
}
