package hu.zoltankollar.functional.syntax

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import hu.zoltankollar.functional.core._

class SyntaxSpec extends FlatSpec with Matchers with MockFactory {

  class TestClass[A]

  "Syntax" should "call Liftable.lift on lift" in {
    val value: Any = new AnyRef
    implicit val l: Liftable[TestClass] = mock[Liftable[TestClass]]

    (l.lift[Any] (_: Any)).expects(*)

    lift(value)
  }

  it should "call Functor.toFunctorOps on toFunctorOps" in {
    val value = new TestClass[Any]
    implicit val fr: Functor[TestClass] = mock[Functor[TestClass]]

    val expected = Functor.toFunctorOps(value)
    val result = toFunctorOps(value)

    result shouldBe a [Functor.Ops[_, Nothing]]
    result.tc shouldBe expected.tc
    result.self shouldBe expected.self
  }

  it should "call Applicative.toApplicativeOps on toApplicativeOps" in {
    val value = new TestClass[Any]
    implicit val fr: Applicative[TestClass] = mock[Applicative[TestClass]]

    val expected = Applicative.toApplicativeOps(value)
    val result = toApplicativeOps(value)

    result shouldBe a [Applicative.Ops[_, Nothing]]
    result.tc shouldBe expected.tc
    result.self shouldBe expected.self
  }

  it should "call Monad.toMonadOps on toMonadOps" in {
    val value = new TestClass[Any]
    implicit val fr: Monad[TestClass] = mock[Monad[TestClass]]

    val expected = Monad.toMonadOps(value)
    val result = toMonadOps(value)

    result shouldBe a [Monad.Ops[_, Nothing]]
    result.tc shouldBe expected.tc
    result.self shouldBe expected.self
  }

  it should "call MonadError.toMonadErrorOps on toMonadErrorOps" in {
    val value = new TestClass[Any]
    implicit val fr: MonadError[TestClass] = mock[MonadError[TestClass]]

    val expected = MonadError.toMonadErrorOps(value)
    val result = toMonadErrorOps(value)

    result shouldBe a [MonadError.Ops[_, Nothing]]
    result.tc shouldBe expected.tc
    result.self shouldBe expected.self
  }
  
}
