package jce.test.reactivemongo

import jce.test.mongo.OneMongoPerSuite
import org.scalatest.Suite
import play.api.Configuration
import play.api.inject.DefaultApplicationLifecycle
import play.modules.reactivemongo.{DefaultReactiveMongoApi, ReactiveMongoApi}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}


trait OneReactiveMongoPerSuite extends OneMongoPerSuite {
  this: Suite =>

  private var api: ReactiveMongoApi = _

  private val appLifeCycle = new DefaultApplicationLifecycle()

  private def initializeApi(): Unit =
    api = new DefaultReactiveMongoApi(
      Configuration.from(Map("mongodb.uri" -> s"mongodb://localhost:${config.port}/$dbName")),
      appLifeCycle
    )

  abstract override protected def beforeAll(): Unit = {
    super.beforeAll()
    logger.info(s"Starting reactive mongo")
    initializeApi()
  }

  abstract override protected def afterAll(): Unit = {
    Await.result(appLifeCycle.stop(), Long.MaxValue.nanos)
    logger.info(s"Stopping reactive mongo")
    super.afterAll()
  }

  implicit def ctx: ExecutionContext

  def dbName: String

  final def reactivemongo: ReactiveMongoApi = synchronized(api)

}

