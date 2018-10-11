package hu.zoltankollar.functional.core

trait Monad[T[_]] extends Applicative[T] {

  def flatMap[A, B](ta: T[A])(f: A => T[B]): T[B]

  def flatten[A](tta: T[T[A]]): T[A]

}

object Monad {

  trait Ops[A, T[_]] {
    def tc: Monad[T]
    def self: T[A]

    def flatMap[B](f: A => T[B]): T[B] = tc.flatMap(self)(f)
    def flatten[B](implicit ev: T[A] <:< T[T[B]]): T[B] = tc.flatten(self)
  }

  def toMonadOps[A, T[_]](ta: T[A])(implicit m: Monad[T]): Ops[A, T] =
    new Ops[A, T] {
      override val tc: Monad[T] = m
      override val self: T[A] = ta
    }

}
