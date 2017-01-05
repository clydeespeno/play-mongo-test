package jce.test.play

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.Suite
import play.api.libs.json.Writes
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.mvc.Result
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait RequestRouter extends OneAppPerSuite with StrictLogging { this: Suite =>

  import Helpers._

  def get(path: String, h: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.get(path, h))

  def post(path: String, body: Option[String] = None, h: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.post(path, body, h))

  def postJson[B : Writes](path: String, body: B, h: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.postJson(path, body, h))

  def put(path: String, body: Option[String] = None, h: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.put(path, body, h))

  def putJson[B : Writes](path: String, body: B, h: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.putJson(path, body, h))

  def delete(path: String, h: Seq[(String, String)] = Seq.empty): Result =
    awaitResult(async.delete(path, h))

  object async {
    def get(path: String, h: Seq[(String, String)] = Seq.empty): Future[Result] =
      routeRequest("GET", path, headers = h)

    def post(path: String, body: Option[String] = None, h: Seq[(String, String)] = Seq.empty): Future[Result] =
      routeRequest("POST", path, body, h)

    def postJson[B : Writes](path: String, body: B, h: Seq[(String, String)] = Seq.empty): Future[Result] =
      post(path, Some(implicitly[Writes[B]].writes(body).toString()), h :+ ("Content-Type" -> "application/json"))

    def put(path: String, body: Option[String] = None, h: Seq[(String, String)] = Seq.empty): Future[Result] =
      routeRequest("PUT", path, body, h)

    def putJson[B : Writes](path: String, body: B, h: Seq[(String, String)] = Seq.empty): Future[Result] =
      put(path, Some(implicitly[Writes[B]].writes(body).toString()), h :+ ("Content-Type" -> "application/json"))

    def delete(path: String, h: Seq[(String, String)] = Seq.empty): Future[Result] =
      routeRequest("DELETE", path, headers = h)
  }

  def routeRequest(
      method: String, path: String, body: Option[String] = None, headers: Seq[(String, String)] = Seq.empty) = {
    logger.info("Request {} {} with headers: {}", Seq(method, path, headers): _*)

    val request = FakeRequest(method, path).withHeaders(headers:_*)
    (if(body.isDefined) {
      Helpers.route(app, request.withJsonBody(Json.parse(body.get)))
    } else {
      Helpers.route(app, request)
    }).get
  }

  def json(result: Result): JsValue = contentAsJson(Future.successful(result))

  def toType[T : Reads](result: Result): T = Json.fromJson[T](json(result)).get

  def awaitResult[T](f: Future[T]): T =
    Await.result(f, Long.MaxValue.nanos)

  implicit class ResultHelper(result: Result) {
    def to[T : Reads] = toType[T](result)

    def to[T : Reads](path: String) = (json(result) \ path).as[T]
  }
}
