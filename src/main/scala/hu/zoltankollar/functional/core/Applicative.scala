package hu.zoltankollar.functional.core

trait Applicative[T[_]] extends Functor[T] with Liftable[T] {

  def apply[A, B](ta: T[A])(f: T[A => B]): T[B]

}

object Applicative {

  trait Ops[A, T[_]] {
    def tc: Applicative[T]
    def self: T[A]

    def apply[B](f: T[A => B]): T[B] = tc.apply(self)(f)
  }

  def toApplicativeOps[A, T[_]](
    ta: T[A]
  )(implicit app: Applicative[T]): Ops[A, T] = new Ops[A, T] {
    override val tc: Applicative[T] = app
    override val self: T[A] = ta
  }

}
