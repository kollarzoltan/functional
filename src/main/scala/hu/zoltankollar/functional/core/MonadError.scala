package hu.zoltankollar.functional.core

trait MonadError[T[_]] extends Monad[T] {

  def recover[A](ta: T[A])(f: PartialFunction[Throwable, A]): T[A]

  def recoverWith[A](ta: T[A])(f: PartialFunction[Throwable, T[A]]): T[A]

}

object MonadError {

  trait Ops[A, T[_]] {
    def tc: MonadError[T]
    def self: T[A]

    def recover(f: PartialFunction[Throwable, A]): T[A] = tc.recover(self)(f)
    def recoverWith(f: PartialFunction[Throwable, T[A]]): T[A] =
      tc.recoverWith(self)(f)
  }

  def toMonadErrorOps[A, T[_]](ta: T[A])(implicit m: MonadError[T]): Ops[A, T] =
    new Ops[A, T] {
      override val tc: MonadError[T] = m
      override val self: T[A] = ta
    }

}
