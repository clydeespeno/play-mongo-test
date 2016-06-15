package foo

import javax.inject.{Inject, Singleton}

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

class FooModule(env: Environment, conf: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[FooService]).to(classOf[FooServiceImpl])
    bind(classOf[FooRepository]).to(classOf[FooRepositoryImpl])
  }
}

@Singleton
class FooServiceImpl @Inject()(
  override protected val repository: FooRepository
) extends FooService

@Singleton
class FooRepositoryImpl @Inject()(
  override protected val reactiveMongoApi: ReactiveMongoApi
)(
  override protected implicit val ctx: ExecutionContext
) extends FooRepository {
  override protected def collectionName: String = "foo"
}
