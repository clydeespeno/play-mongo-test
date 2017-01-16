package jce.test.play

import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.mockito.stubbing.Stubber
import org.mockito.verification.VerificationMode
import org.scalatest.mockito.MockitoSugar

trait MockSugar extends MockitoSugar {

  def argAnswer[T](invocationContext: Array[AnyRef] => T): Stubber =
    answer(invocation => invocationContext(invocation.getArguments))


  def answer[T](invocationContext: InvocationOnMock => T): Stubber = Mockito.doAnswer(new Answer[T]() {
    override def answer(invocation: InvocationOnMock): T =
      invocationContext(invocation)
  })

  def argOf[A](index: Int)(implicit args: Array[AnyRef]): A = args(index).asInstanceOf[A]

  def doReturn(any: Any) = Mockito.doReturn(any, Array())

  def reset[T](t: T*) = Mockito.reset(t: _*)

  def verify[T](mock: T, mode: VerificationMode) = Mockito.verify(mock, mode)

  def invoked[T](mock: T): MockVerification[T] = new MockVerification(mock)
}

class MockVerification[T](mock: T) {

  import Mockito.verify

  def times(n: Int): T = verify(mock, Mockito.times(n))

  def once(): T = times(1)

  def atLeast(n: Int): T = verify(mock, Mockito.atLeast(n))

  def atLeastOnce(): T = atLeast(1)

  def only(): T = verify(mock, Mockito.only())

  def timeout(n: Long): T = verify(mock, Mockito.timeout(n))

  def after(n: Int): T = verify(mock, Mockito.after(n))

  def never(): T = verify(mock, Mockito.never())
}

object MockSugar extends MockitoSugar
