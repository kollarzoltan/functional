package hu.zoltankollar.functional

import scala.util.{Failure, Success, Try}

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import hu.zoltankollar.functional.TryMonad.tryMonad

class TryMonadSpec extends FlatSpec with Matchers with MockFactory {

  "TryMonad" should "complete a Try on lift" in {
    val value = new AnyRef
    tryMonad.lift(value) shouldBe Try(value)
  }

  it should "fail a Future on lifting an exception" in {
    val ex = new Throwable
    def value: Any = throw ex

    tryMonad.lift(value) shouldBe Failure(ex)
  }

  it should "use Try.map on map if the Try is succeeded" in {
    val ex = new Throwable
    val value = new AnyRef
    val result = new AnyRef
    val f = stubFunction[AnyRef, AnyRef]

    val succeeded:Try[AnyRef] = Success(value)

    f.when(value).returns(result)

    tryMonad.map(succeeded)(f) shouldBe succeeded.map(f)
  }

  it should "use Try.map on map if the Try is failed" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef, AnyRef]

    val failed:Try[AnyRef] = Failure(ex)

    f.expects(*).never()

    tryMonad.map(failed)(f) shouldBe failed.map(f)
  }

  it should "apply the lifted method on apply" in {
    val value = new AnyRef
    val result = new AnyRef
    val f = stubFunction[AnyRef, AnyRef]
    val ff: Try[AnyRef => AnyRef] = Success(f)
    val monad:Try[AnyRef] = Success(value)

    f.when(value).returns(result)

    tryMonad.apply(monad)(ff) shouldBe Try(result)
  }

  it should "return the failure on apply failed function" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef, AnyRef]
    val ff: Try[AnyRef => AnyRef] = Failure(ex)
    val monad:Try[AnyRef] = Success(new AnyRef)

    f.expects(*).never()

    tryMonad.apply(monad)(ff) shouldBe Failure(ex)
  }

  it should "return the failure on apply function if the Try is failed" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef, AnyRef]
    val ff: Try[AnyRef => AnyRef] = Success(f)
    val monad:Try[AnyRef] = Failure(ex)

    f.expects(*).never()

    tryMonad.apply(monad)(ff) shouldBe Failure(ex)
  }

  it should "use Try.flatMap on flatMap if the Try is succeeded" in {
    val value = new AnyRef
    val result:Try[AnyRef] = Success(new AnyRef)
    val f = stubFunction[AnyRef, Try[AnyRef]]
    val monad:Try[AnyRef] = Success(value)

    f.when(value).returns(result)

    tryMonad.flatMap(monad)(f) shouldBe monad.flatMap(f)
  }

  it should "use Try.flatMap on flatMap if the Try is failed" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef,Try[AnyRef]]

    val failed:Try[AnyRef] = Failure(ex)

    f.expects(*).never()

    tryMonad.flatMap(failed)(f) shouldBe failed.flatMap(f)
  }

  it should "return the value from the nested Try on flatten if the outer is succeeded" in {
    val nested = Success(new AnyRef)
    val monad = Success(nested)

    tryMonad.flatten(monad) shouldBe nested
  }

  it should "return the exception from the nested Try on flatten if the outer is succeeded" in {
    val ex = new Throwable
    val nested = Failure[AnyRef](ex)
    val monad = Success(nested)

    tryMonad.flatten(monad) shouldBe nested
  }

  it should "return exception on flatten if the outer is failed" in {
    val ex = new Throwable
    val monad = Failure(ex)

    tryMonad.flatten(monad) shouldBe Failure(ex)
  }
  
  it should "call recovery function on failed Try on recover" in {
    val ex = new Throwable
    val expected = new AnyRef
    val f: PartialFunction[Throwable, Any] = { case _: Throwable => expected }

    val failed = Failure[Any](ex)

    tryMonad.recover(failed)(f).map(_ shouldBe expected)
  }

  it should "call recovery function on failed Try on recoverWith" in {
    val ex = new Throwable
    val expected = Success(new AnyRef)
    val f: PartialFunction[Throwable, Try[Any]] = {
      case _: Throwable => expected
    }

    val failed = Failure[Any](ex)

    tryMonad.recover(failed)(f).map(_ shouldBe expected)
  }
  
}
