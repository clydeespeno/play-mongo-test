package actors

import akka.pattern.ask
import akka.util.Timeout
import jce.test.play.ActorGuiceSupportTest
import org.scalatest.{FlatSpec, ShouldMatchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class ActorTest extends FlatSpec with ShouldMatchers with ActorGuiceSupportTest {

  "An Actor Suite" should "spawn an actor system with the given modules" in {
    val barActor = getNamedActor("bar")
    val result = Await.result(barActor ? "baz", duration)
    result shouldBe "baz"
  }

  override def modules = super.modules :+ new ActorModule

  val duration = 10.seconds
  implicit val timeout = Timeout(duration)
}
