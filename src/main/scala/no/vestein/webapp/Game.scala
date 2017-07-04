package no.vestein.webapp

import no.vestein.webapp.App.Ctx2D
import org.scalajs.dom
import org.scalajs.dom.window

import scala.annotation.tailrec

class Game {

  val dropList: List[Drop] = createDropList(1, 600, Vector2D(0, 0), Vector2D(window.innerWidth.toInt, window.innerHeight.toInt))

  var grid: Boolean = false
  var debug: Boolean = false
  var gridSize: Double = 100

  def render(screen: Screen): Unit = {
    val ctx: Ctx2D = screen.ctx

    ctx.clearRect(0, 0, screen.width, screen.height)
    ctx.fillStyle = "#E6E6FA"
    ctx.fillRect(0, 0, screen.width, screen.height)

    if (grid) Grid(screen, gridSize).render()

    dropList.foreach(_.render(ctx))

  }

  def update(delta: Double, keys: Set[Int]): Unit = {

    if (keys.nonEmpty) println(keys)

    keys.foreach {
      case 13 => println(dropList.filter(_.mark))
      case 38 => dropList.head.mark = !dropList.head.mark
      case 68 => debug = !debug
      case 71 => grid = !grid
      case _ => //No match
    }

    dropList.foreach(_.update(delta))
  }

  /**
    * Creates a list of Drop objects.
    * @param sizeFactor
    * @param amount
    * @param pos
    * @param dim
    * @return
    */
  private def createDropList(sizeFactor: Int, amount: Int, pos: Vector2D, dim: Vector2D): List[Drop] = {

    def randomDouble(min: Double, max: Double): Double = min + Math.random() * (max - min)

    @tailrec def rec(acc: List[Drop], n: Int): List[Drop] = {
      if (n == 0) return acc

      val x: Double = pos.x + Math.random() * dim.x
      val y: Double = pos.y + Math.random() * dim.y

      val prop: Double = Math.random() * 100
      val v: Double = prop match {
        case x if x < 50 => 0.1f
        case x if x >= 50 && x < 80 => 0.2f
        case x if x >= 80 => 0.3f
      }

      return rec(Drop(Vector2D(x, y), Vector2D(0, v + randomDouble(0.025f, 0.050f)), pos, dim) :: acc, n - 1)
    }

    return rec(Nil, amount)
  }

}
