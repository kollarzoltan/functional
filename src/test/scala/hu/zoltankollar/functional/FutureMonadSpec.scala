package hu.zoltankollar.functional

import scala.concurrent.Future

import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFlatSpec, Matchers}

import hu.zoltankollar.functional.FutureMonad._

class FutureMonadSpec extends AsyncFlatSpec with Matchers with AsyncMockFactory {

  "FutureMonad" should "complete a Future on lift" in {
    val value = new AnyRef
    futureMonad.lift(value).map(_ shouldBe value)
  }

  it should "fail a Future on lifting an exception" in {
    val ex = new Throwable
    def value: Any = throw ex

    val result = recoverToExceptionIf[Throwable] {
      futureMonad.lift(value)
    }
    result.map(_ shouldBe ex)
  }

  it should "use Future.map on map if the Future is succeeded" in {
    val ex = new Throwable
    val value = new AnyRef
    val result = new AnyRef
    val f = stubFunction[AnyRef, AnyRef]

    val succeeded = Future.successful(value)

    f.when(value).returns(result)

    val seq = Seq(
      futureMonad.map(succeeded)(f),
      succeeded.map(f)
    )
    Future.sequence(seq).map(r => r shouldBe r.reverse)
  }

  it should "use Future.map on map if the Future is failed" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef, AnyRef]

    val failed = Future.failed(ex)

    f.expects(*).never()

    val seq = Seq(
      recoverToExceptionIf[Throwable] {
        futureMonad.map(failed)(f)
      },
      recoverToExceptionIf[Throwable] {
        failed.map(f)
      }
    )
    Future.sequence(seq).map(r => r shouldBe r.reverse)
  }

  it should "apply the lifted method on apply" in {
    val value = new AnyRef
    val result = new AnyRef
    val f = stubFunction[AnyRef, AnyRef]
    val ff: Future[AnyRef => AnyRef] = Future.successful(f)
    val monad = Future.successful(value)

    f.when(value).returns(result)

    futureMonad.apply(monad)(ff).map(_ shouldBe result)
  }

  it should "return the failure on apply failed function" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef, AnyRef]
    val ff: Future[AnyRef => AnyRef] = Future.failed(ex)
    val monad = Future.successful(new AnyRef)

    f.expects(*).never()

    val result = recoverToExceptionIf[Throwable] {
      futureMonad.apply(monad)(ff)
    }
    result.map(_ shouldBe ex)
  }

  it should "return the failure on apply function if the Future is failed" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef, AnyRef]
    val ff: Future[AnyRef => AnyRef] = Future.successful(f)
    val monad = Future.failed(ex)

    f.expects(*).never()

    val result = recoverToExceptionIf[Throwable] {
      futureMonad.apply(monad)(ff)
    }
    result.map(_ shouldBe ex)
  }

  it should "use Future.flatMap on flatMap if the Future is succeeded" in {
    val value = new AnyRef
    val result = Future.successful(new AnyRef)
    val f = stubFunction[AnyRef, Future[AnyRef]]
    val monad = Future.successful(value)

    f.when(value).returns(result)

    val seq = Seq(
      futureMonad.flatMap(monad)(f),
      monad.flatMap(f)
    )
    Future.sequence(seq).map(r => r shouldBe r.reverse)
  }

  it should "use Future.flatMap on flatMap if the Future is failed" in {
    val ex = new Throwable
    val f = mockFunction[AnyRef,Future[AnyRef]]

    val failed = Future.failed(ex)

    f.expects(*).never()

    val seq = Seq(
      recoverToExceptionIf[Throwable] {
        futureMonad.flatMap(failed)(f)
      },
      recoverToExceptionIf[Throwable] {
        failed.flatMap(f)
      }
    )
    Future.sequence(seq).map(r => r shouldBe r.reverse)
  }

  it should "return the value from the nested Future on flatten if the outer is succeeded" in {
    val nested = Future.successful(new AnyRef)
    val monad = Future.successful(nested)

    futureMonad.flatten(monad).flatMap { value =>
      nested.map(_ shouldBe value)
    }
  }

  it should "return the exception from the nested Future on flatten if the outer is succeeded" in {
    val ex = new Throwable
    val nested = Future.failed[AnyRef](ex)
    val monad = Future.successful(nested)


    val result = recoverToExceptionIf[Throwable] {
      futureMonad.flatten(monad)
    }
    result.map(_ shouldBe ex)
  }

  it should "return exception on flatten if the outer is failed" in {
    val ex = new Throwable
    val monad = Future.failed(ex)

    val result = recoverToExceptionIf[Throwable] {
      futureMonad.flatten(monad)
    }
    result.map(_ shouldBe ex)
  }

  it should "call recovery function on failed Future on recover" in {
    val ex = new Throwable
    val expected = new AnyRef
    val f: PartialFunction[Throwable, Any] = { case _: Throwable => expected }

    val failed = Future.failed[Any](ex)

    futureMonad.recover(failed)(f).map(_ shouldBe expected)
  }

  it should "call recovery function on failed Future on recoverWith" in {
    val ex = new Throwable
    val expected = Future.successful(new AnyRef)
    val f: PartialFunction[Throwable, Future[Any]] = {
      case _: Throwable => expected
    }

    val failed = Future.failed[Any](ex)

    futureMonad.recover(failed)(f).map(_ shouldBe expected)
  }

}
