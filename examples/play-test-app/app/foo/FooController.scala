package foo

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

@Singleton
class FooController @Inject()(
  fooService: FooService
)(
  implicit ctx: ExecutionContext
) extends Controller {

  def getFoos = Action.async { req =>
    fooService.get().map(fs => Ok(Json.toJson(fs)))
  }

}