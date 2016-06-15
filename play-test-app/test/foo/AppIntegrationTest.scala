package foo

import jce.test.play.{OneAppPerSuite, RequestRouter}
import jce.test.reactivemongo.OneReactiveMongoPerSuite
import org.scalatest.{FlatSpec, ShouldMatchers}
import play.api.inject._
import play.api.inject.guice.GuiceableModule
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

trait AppIntegrationTest extends FlatSpec
  with ShouldMatchers with OneReactiveMongoPerSuite with OneAppPerSuite with RequestRouter {

  override implicit def ctx: ExecutionContext = ExecutionContext.global

  override def dbName: String = "foosdb"

  override def overrides: Seq[GuiceableModule] =
    super.overrides ++ Seq[GuiceableModule](bind(classOf[ReactiveMongoApi]).toInstance(reactivemongo))

}
