package jce.test.mongo

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{BeforeAndAfterAll, Suite}


trait OneMongoPerSuite extends BeforeAndAfterAll with StrictLogging {
  this: Suite =>

  /**
    * override to create a different configuration
    */
  def config = FakeMongoConfig(
    port = 27017
  )

  /**
    * The fake mongo instance created with the given configuration
    */
  final lazy val fakeMongo = new FakeMongo(config)

  abstract override protected def beforeAll(): Unit = {
    super.beforeAll()
    logger.info(s"Starting mongo instance at port ${config.port}")
    retry(3)(fakeMongo.start())
  }

  abstract override protected def afterAll(): Unit = {
    try fakeMongo.stop() catch {
      case e: Throwable => logger.error(e.getMessage, e)
    }
    super.afterAll()
  }

}