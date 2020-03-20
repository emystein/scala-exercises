package ar.com.flow.kata

// https://www.codewars.com/kata/559b8e46fa060b2c6a0000bf

import PascalTriangle.topRow

object PascalTriangle {
  val topRow: Int = 0
}

object Diagonal {
  def diagonal(n: Int, p: Int): BigInt = {
    Diagonal(n, p).sum
  }
}

case class Diagonal(row: Int, diagonal: Int) {
  def nodes: List[Node] =
    if (row == topRow && diagonal != 0) {
      List.empty
    } else {
      val start = Node(row, column = diagonal + 1)
      val ns: List[Node] = start.rightParent.map(n => Diagonal(n.row, diagonal).nodes).getOrElse(Nil)
      start :: ns
    }

  def sum: Int = nodes.map(_.value).sum
}

case class Node(row: Int, column: Int) {
  val previousRow = row - 1
  val previousColumn = column - 1
  val isInTheTopRow: Boolean = row == topRow
  val isInTheLastColumn: Boolean = column == row + 1

  def leftParent: Option[Node] = LeftParent.of(this)

  def rightParent: Option[Node] = RightParent.of(this)

  def parents: Seq[Node] = Seq(leftParent, rightParent).flatten

  def value: Int = parents.map(_.value).sum.max(1)
}

object LeftParent {
  def of(node: Node): Option[Node] =
    if (node.isInTheTopRow || node.column == 1) {
      None
    } else if (node.isInTheLastColumn) {
      Some(Node(node.previousRow, column = node.row))
    } else {
      Some(Node(node.previousRow, node.previousColumn))
    }
}

object RightParent {
  def of(node: Node): Option[Node] =
    if (node.isInTheTopRow || node.isInTheLastColumn) {
      None
    } else {
      Some(Node(node.previousRow, node.column))
    }
}