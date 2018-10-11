package hu.zoltankollar.functional.core

trait Liftable[M[_]] {

  def lift[A](a: => A): M[A]

}
