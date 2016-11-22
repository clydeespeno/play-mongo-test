package jce.test.play

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.Suite
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.mvc.Result
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait RequestRouter extends OneAppPerSuite with StrictLogging { this: Suite =>

  import Helpers._

  def get(path: String, headers: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.get(path, headers))

  object async {
    def get(path: String, headers: Seq[(String, String)] = Seq.empty): Future[Result] =
      routeRequest("GET", path, headers = headers).get
  }

  def routeRequest(
      method: String, path: String, body: Option[String] = None, headers: Seq[(String, String)] = Seq.empty) = {
    logger.info("Request {} {} with headers: {}", method, path, headers)

    val request = FakeRequest(method, path).withHeaders(headers:_*)
    if(body.isDefined) {
      Helpers.route(app, request.withJsonBody(Json.parse(body.get)))
    } else {
      Helpers.route(app, request)
    }
  }

  def json(result: Result): JsValue = contentAsJson(Future.successful(result))

  def toType[T : Reads](result: Result): T = Json.fromJson[T](json(result)).get

  def awaitResult[T](f: Future[T]): T =
    Await.result(f, Long.MaxValue.nanos)

}
