package ar.com.flow.fp.manning.chapter5

sealed trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }

  def toList: List[A] = this match {
    case Empty => List.empty
    case Cons(h, t) => h() :: t().toList
  }

  def take(n: Int): Stream[A] = {
    if (n <= 0) return Stream.empty

    this match {
      case Empty => Stream.empty
      case Cons(h, t) => Cons(h, () => t().take(n - 1))
    }
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    this match {
      case Empty => Stream.empty
      case Cons(h, t) => {
        if (p(h())) {
          Cons(h, () => t().takeWhile(p))
        } else {
          Stream.empty
        }
      }
    }
  }

  def drop(n: Int): Stream[A] = {
    if (n <= 0) return this

    this match {
      case Empty => Empty
      case Cons(h, t) => t().drop(n - 1)
    }
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B = {
    this match {
      case Cons(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }
  }

  def forAll(p: A => Boolean): Boolean = this match {
    case Empty => true
    case Cons(h, t) => {
      if (p(h())) {
        t().forAll(p)
      } else {
        false
      }
    }
  }

  def headOptionUsingFoldRight: Option[A] = {
    foldRight[Option[A]](None)((a, b) => Some(a))
  }

  def from(n: Int): Stream[Int] = {
    Stream.cons(n, from(n + 1))
  }
}

case object Empty extends Stream[Nothing]

case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    f(z) match {
      case None => Stream.empty
      case Some((a, s)) => Stream.cons(a, unfold(s)(f))
    }
  }
}


