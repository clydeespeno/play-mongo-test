package jce.test.play

import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.mockito.stubbing.Stubber
import org.scalatest.mock.MockitoSugar

trait MockSugar extends MockitoSugar {

  def argAnswer[T](invocationContext: Array[AnyRef] => T): Stubber =
    answer(invocation => invocationContext(invocation.getArguments))


  def answer[T](invocationContext: InvocationOnMock => T): Stubber = Mockito.doAnswer(new Answer[T]() {
    override def answer(invocation: InvocationOnMock): T =
      invocationContext(invocation)
  })

  def argOf[A](index: Int)(implicit args: Array[AnyRef]): A = args(index).asInstanceOf[A]

  def doReturn(any: Any) = Mockito.doReturn(any)

}
