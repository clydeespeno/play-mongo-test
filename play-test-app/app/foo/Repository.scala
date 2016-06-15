package foo

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

trait Repository {

  protected implicit def ctx: ExecutionContext

  protected def reactiveMongoApi: ReactiveMongoApi

  protected def exec[T](f: JSONCollection => Future[T]): Future[T] =
    collection.flatMap(f)

  protected def collection: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection(collectionName))

  protected def collectionName: String

}
