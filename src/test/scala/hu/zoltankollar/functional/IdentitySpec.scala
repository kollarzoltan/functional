package hu.zoltankollar.functional

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import hu.zoltankollar.functional.Identity.identityMonad

class IdentitySpec extends FlatSpec with Matchers with MockFactory {

  "Identity" should "return lifted value on get" in {
    val value = new AnyRef
    Identity(value).get shouldBe value
  }

  it should "apply the method on the wrapped value without any additional effect on map" in {
    val value = new AnyRef
    val expected = new AnyRef
    val f = mockFunction[Any, Any]

    f.expects(value).returning(expected)

    Identity(value).map(f) shouldBe Identity(expected)
  }

  it should "apply the method on the wrapped value without any additional effect on flatMap" in {
    val value = new AnyRef
    val expected: Identity[Any] = Identity(new AnyRef)
    val f = mockFunction[AnyRef, Identity[Any]]

    f.expects(value).returning(expected)

    Identity(value).flatMap(f) shouldBe expected
  }

  "IdentityMonad" should "create an Identity on lift" in {
    val value = new AnyRef
    identityMonad.lift(value) shouldBe Identity(value)
  }

  it should "use Identity.map on map" in {
    val id = mock[Identity[Any]]
    val f = mockFunction[Any, Any]

    (id.map (_: Any => Any)).expects(f)

    identityMonad.map(id)(f)
  }

  it should "apply the wrapped function on apply" in {
    val value: Any = new AnyRef
    val id = Identity(value)
    val f = mockFunction[Any, Any]
    val ff: Identity[Any => Any] = Identity(f)

    f.expects(value)

    identityMonad.apply(id)(ff)
  }

  it should "use Identity.flatMap on flatMap" in {
    val id = mock[Identity[Any]]
    val f = mockFunction[Any, Identity[Any]]

    (id.flatMap (_: Any => Identity[Any])).expects(f)

    identityMonad.flatMap(id)(f)
  }

  it should "return the nested Identity on flatten" in {
    val nested = mock[Identity[Any]]
    val id = Identity(nested)

    identityMonad.flatten(id) shouldBe nested
  }

}
