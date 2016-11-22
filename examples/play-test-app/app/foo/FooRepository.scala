package foo

import play.api.libs.json.Json
import play.modules.reactivemongo.json._

import scala.concurrent.Future

trait FooRepository extends Repository {

  def findAll(): Future[List[Foo]] = exec(_.find(Json.obj()).cursor[Foo]().collect[List]())

}
