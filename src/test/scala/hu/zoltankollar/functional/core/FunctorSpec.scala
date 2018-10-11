package hu.zoltankollar.functional.core

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class FunctorSpec extends FlatSpec with Matchers with MockFactory {

  class TestType[A]

  "Functor" should "convert object to Ops on to FunctorOps" in {
    val obj = mock[TestType[Int]]
    implicit val functor: Functor[TestType] = mock[Functor[TestType]]

    val ops = Functor.toFunctorOps(obj)
    ops.tc shouldBe functor
    ops.self shouldBe obj
  }

  "FunctorOps" should "invoke the typeclass' map method on map" in {
    val obj = mock[TestType[Int]]
    val functor = mock[Functor[TestType]]
    val f = mockFunction[Int, Int]

    (functor.map (_: TestType[Int])(_: Int => Int)).expects(obj, f)

    val ops = new Functor.Ops[Int, TestType] {
      override def tc: Functor[TestType] = functor
      override def self: TestType[Int] = obj
    }

    ops.map(f)
  }

}
