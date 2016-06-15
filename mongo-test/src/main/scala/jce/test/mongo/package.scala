package jce.test

import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}


package object mongo {

  private val logger = LoggerFactory.getLogger("jce.mongo.test")

  def retry[T](n: Int)(fn: => T): Try[T] = Try(fn) match {
    case Success(t) => Success(t)
    case Failure(e) if n > 0 => retry(n - 1) {
      logger.error("execution failed, retrying in 3 seconds", e)
      Thread.sleep(3000)
      fn
    }
    case Failure(e) => Failure(e)
  }



}
