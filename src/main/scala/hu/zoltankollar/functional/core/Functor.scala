package hu.zoltankollar.functional.core

trait Functor[T[_]] {

  def map[A, B](ma: T[A])(f: A => B): T[B]

}

object Functor {

  trait Ops[A, T[_]] {
    def tc: Functor[T]
    def self: T[A]

    def map[B](f: A => B): T[B] = tc.map(self)(f)
  }

  def toFunctorOps[A, T[_]](ta: T[A])(implicit fr: Functor[T]): Ops[A, T] =
    new Ops[A, T] {
      override val tc: Functor[T] = fr
      override val self: T[A] = ta
    }

}
