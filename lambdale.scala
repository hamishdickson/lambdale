package lambdale

import cats.implicits._
import cats.kernel.Semilattice
import org.joda.time.DateTime

import shapeless.{::, Generic, HList, HNil, Lazy}
import model._
import model.Id._

object model {
  case class Id[T](value: String)
  object Id {
    implicit def idSemilattice[T] = new Semilattice[Id[T]] {
      def combine(i1: Id[T], i2: Id[T]) = i1
    }    
  }

  case class User(id: Id[User], name: String)
  case class Post(id: Id[Post], text: String)

  case class State(id: Id[Post], likes: Set[Id[User]])
}


object AutoSemilattice {

  implicit def autoSemilatticeHNil = new Semilattice[HNil] {
    override def combine(x: HNil, y: HNil) = HNil
  }

  implicit def autoSemilatticeHCons[H, L <: HList](
    implicit headSemilattice: Lazy[Semilattice[H]],
    tailSemilattice: Lazy[Semilattice[L]]
  ) = new Semilattice[H :: L] {
    override def combine(x: H :: L, y: H :: L) =
      headSemilattice.value.combine(x.head, y.head) :: tailSemilattice.value.combine(x.tail, y.tail)
  }

  implicit def autoSemilattice[T, Repr](
    implicit generic: Generic.Aux[T, Repr],
    genericSemilattice: Lazy[Semilattice[Repr]]
  ) = new Semilattice[T] {
    override def combine(x: T, y: T) =
      generic.from(genericSemilattice.value.combine(generic.to(x), generic.to(y)))
  }
}

object Derived {
  import AutoSemilattice._

  implicitly[Semilattice[State]]
}


object DumbAttempt {
//   implicit def setSemilattice[U] = new Semilattice[Set[U]] {
//     def combine(s1: Set[U], s2: Set[U]): Set[U] = s1 ++ s2
//   }

  implicitly[Semilattice[Set[Id[User]]]]

  implicit val stateSemilattice: Semilattice[State] = new Semilattice[State] {
    def combine(s1: State, s2: State): State = State(s1.id, s1.likes |+| s2.likes)
  }
}
