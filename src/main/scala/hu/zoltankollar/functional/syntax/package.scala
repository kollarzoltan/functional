package hu.zoltankollar.functional

import scala.language.implicitConversions

import hu.zoltankollar.functional.core._

package object syntax {

  implicit def lift[A, T[_]](a: A)(implicit l: Liftable[T]): T[A] = l.lift(a)

  implicit def toFunctorOps[A, T[_]](
    ta: T[A]
  )(implicit fr: Functor[T]): Functor.Ops[A, T] = Functor.toFunctorOps(ta)(fr)

  implicit def toApplicativeOps[A, T[_]](
    ta: T[A]
  )(implicit app: Applicative[T]): Applicative.Ops[A, T] =
    Applicative.toApplicativeOps(ta)(app)

  implicit def toMonadOps[A, T[_]](
    ta: T[A]
  )(implicit m: Monad[T]): Monad.Ops[A, T] = Monad.toMonadOps(ta)(m)

  implicit def toMonadErrorOps[A, T[_]](
    ta: T[A]
  )(implicit m: MonadError[T]): MonadError.Ops[A, T] =
    MonadError.toMonadErrorOps(ta)(m)

}
