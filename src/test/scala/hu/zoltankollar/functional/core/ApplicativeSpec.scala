package hu.zoltankollar.functional.core

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ApplicativeSpec extends FlatSpec with Matchers with MockFactory {

  class TestType[A]

  "Applicative" should "convert object to Ops on to ApplicativeOps" in {
    val obj = mock[TestType[Int]]
    implicit val app: Applicative[TestType] = mock[Applicative[TestType]]

    val ops = Applicative.toApplicativeOps(obj)
    ops.tc shouldBe app
    ops.self shouldBe obj
  }

  "ApplicativeOps" should "invoke the typeclass' apply method on apply" in {
    val obj = mock[TestType[Int]]
    val f = mock[TestType[Int => Int]]
    val applicative = mock[Applicative[TestType]]

    (applicative.apply[Int, Int] (_: TestType[Int])(_: TestType[Int => Int]))
      .expects(obj, f)

    val ops = new Applicative.Ops[Int, TestType] {
      override def tc: Applicative[TestType] = applicative
      override def self: TestType[Int] = obj
    }

    ops.apply(f)
  }

}
