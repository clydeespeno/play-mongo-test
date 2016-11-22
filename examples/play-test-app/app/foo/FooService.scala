package foo

import scala.concurrent.Future

trait FooService {

  protected def repository: FooRepository

  def get(): Future[List[Foo]] = repository.findAll()

}
