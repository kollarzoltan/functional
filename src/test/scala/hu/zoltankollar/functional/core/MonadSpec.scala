package hu.zoltankollar.functional.core

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class MonadSpec extends FlatSpec with Matchers with MockFactory {

  class TestType[A]

  "Monad" should "convert object to Ops on to MonadOps" in {
    val obj = mock[TestType[Int]]
    implicit val monad: Monad[TestType] = mock[Monad[TestType]]

    val ops = Monad.toMonadOps(obj)
    ops.tc shouldBe monad
    ops.self shouldBe obj
  }

  "MonadOps" should "invoke the typeclass' flatMap method on flatMap" in {
    val obj = mock[TestType[Int]]
    val monad = mock[Monad[TestType]]
    val f = mockFunction[Int, TestType[Int]]

    (monad.flatMap (_: TestType[Int])(_: Int => TestType[Int])).expects(obj, f)

    val ops = new Monad.Ops[Int, TestType] {
      override def tc: Monad[TestType] = monad
      override def self: TestType[Int] = obj
    }

    ops.flatMap(f)
  }

  it should "invoke the typeclass' flatten method on flatten" in {
    val obj = mock[TestType[TestType[Int]]]
    val monad = mock[Monad[TestType]]

    (monad.flatten _).expects(*)

    val ops = new Monad.Ops[TestType[Int], TestType] {
      override def tc: Monad[TestType] = monad
      override def self: TestType[TestType[Int]] = obj
    }

    ops.flatten
  }

  it should "not compile if flatten called on non nested monad" in {
    val obj = mock[TestType[Int]]
    val monad = mock[Monad[TestType]]

    val ops = new Monad.Ops[Int, TestType] {
      override def tc: Monad[TestType] = monad
      override def self: TestType[Int] = obj
    }

    "ops.flatten" shouldNot compile
  }

}
