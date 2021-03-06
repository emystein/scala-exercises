package ar.com.flow.fp.manning.chapter3

import ar.com.flow.fp.manning.chapter3.TreeExercises._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks._

class TreeExercisesTest extends FunSuite with Matchers {
  test("size of leaf should be 1") {
    val leaf = Leaf[Int](1)
    leaf.size shouldBe 1
  }

  test("size of branch with two leaves should be 3") {
    val branch = Branch(Leaf(1), Leaf(2))
    branch.size shouldBe 3
  }

  val maximumValues = Table(
    ("tree", "max"),
    (Leaf(1), 1),
    (Branch(Leaf(1), Leaf(2)), 2),
    (Branch(Leaf(3), Branch(Leaf(1), Leaf(2))), 3),
    (Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)), 3),
    (Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))), 4)
  )

  forAll(maximumValues) { (tree: Tree[Int], max: Int) =>
    maximum(tree) shouldBe max
  }

  val depthValues = Table(
    ("tree", "expectedDepth"),
    (Leaf(1), 1),
    (Branch(Leaf(1), Leaf(2)), 2),
    (Branch(Leaf(3), Branch(Leaf(1), Leaf(2))), 3),
    (Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)), 3),
    (Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))), 3)
  )

  forAll(depthValues) { (tree: Tree[Int], expectedDepth: Int) =>
    depth(tree) shouldBe expectedDepth
  }

  val plusOneValues = Table(
    ("tree", "expected"),
    (Leaf(1), Leaf(2)),
    (Branch(Leaf(1), Leaf(2)), Branch(Leaf(2), Leaf(3))),
    (Branch(Leaf(1), Branch(Leaf(2), Leaf(3))), Branch(Leaf(2), Branch(Leaf(3), Leaf(4)))),
    (Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)), Branch(Branch(Leaf(2), Leaf(3)), Leaf(4))),
    (Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))), Branch(Branch(Leaf(2), Leaf(3)), Branch(Leaf(4), Leaf(5))))
  )

  forAll(plusOneValues) { (tree: Tree[Int], expected: Tree[Int]) =>
    map(tree)((v:Int) => v + 1) shouldBe expected
  }
}
