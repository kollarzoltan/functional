package hu.zoltankollar.functional

import scala.concurrent.{ExecutionContext, Future}

import hu.zoltankollar.functional.core.MonadError

object FutureMonad {

  implicit def futureMonad(implicit ec: ExecutionContext): MonadError[Future] =
    new MonadError[Future] {

      override def lift[A](a: => A): Future[A] = Future(a)

      override def map[A, B](ta: Future[A])(f: A => B): Future[B] =
        ta.map(f)

      override def apply[A, B](
        ta: Future[A]
      )(f: Future[A => B]): Future[B] = f.flatMap(func => ta.map(func))

      override def flatMap[A, B](
        ta: Future[A]
      )(f: A => Future[B]): Future[B] = ta.flatMap(f)

      override def flatten[A](tta: Future[Future[A]]): Future[A] = tta.flatten

      override def recover[A](
        ta: Future[A]
      )(f: PartialFunction[Throwable, A]): Future[A] = ta.recover(f)

      override def recoverWith[A](
        ta: Future[A]
      )(f: PartialFunction[Throwable, Future[A]]): Future[A] = ta.recoverWith(f)
    }

}
