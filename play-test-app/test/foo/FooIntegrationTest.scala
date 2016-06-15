package foo

import play.mvc.Http.Status._

class FooIntegrationTest extends AppIntegrationTest {

  "Foo Controller" should "get foos" in {
    val result = get("/foo")
    result.header.status shouldBe OK
    toType[List[Foo]](result) should have size 0
  }

}
