package foo

import play.api.libs.json.Json

case class Foo(name: String)

object Foo {
  implicit val format = Json.format[Foo]
}
