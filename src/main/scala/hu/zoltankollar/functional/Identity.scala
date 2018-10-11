package hu.zoltankollar.functional

import hu.zoltankollar.functional.core.Monad

case class Identity[A](a: A) {

  def get: A = a

  def map[B](f: A => B): Identity[B] = Identity(f(a))

  def flatMap[B](f: A => Identity[B]): Identity[B] = f(a)

  def flatten[B](implicit ev: A <:< Identity[B]): Identity[B] = flatMap(ev)

}

object Identity {

  implicit lazy val identityMonad: Monad[Identity] = new Monad[Identity] {

    override def lift[A](a: => A): Identity[A] = Identity(a)

    override def map[A, B](ia: Identity[A])(f: A => B): Identity[B] = ia.map(f)

    override def apply[A, B](
      ia: Identity[A]
    )(f: Identity[A => B]): Identity[B] = ia.map(f.get)

    override def flatMap[A, B](
      ia: Identity[A]
    )(f: A => Identity[B]): Identity[B] = ia.flatMap(f)

    override def flatten[A](tta: Identity[Identity[A]]): Identity[A] =
      tta.flatten
  }

}
