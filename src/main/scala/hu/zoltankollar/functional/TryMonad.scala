package hu.zoltankollar.functional

import scala.util.Try

import hu.zoltankollar.functional.core.MonadError

object TryMonad {

  implicit lazy val tryMonad: MonadError[Try] = new MonadError[Try] {

    override def lift[A](a: => A): Try[A] = Try(a)

    override def map[A, B](ta: Try[A])(f: A => B): Try[B] = ta.map(f)

    override def apply[A, B](ta: Try[A])(f: Try[A => B]): Try[B] =
      f.flatMap(func => ta.map(func))

    override def flatMap[A, B](ta: Try[A])(f: A => Try[B]): Try[B] =
      ta.flatMap(f)

    override def flatten[A](tta: Try[Try[A]]): Try[A] = tta.flatten

    override def recover[A](
      ta: Try[A]
    )(f: PartialFunction[Throwable, A]): Try[A] = ta.recover(f)

    override def recoverWith[A](
      ta: Try[A]
    )(f: PartialFunction[Throwable, Try[A]]): Try[A] = ta.recoverWith(f)
  }

}
