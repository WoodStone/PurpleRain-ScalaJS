package no.vestein.webapp.game

import no.vestein.webapp.App.Ctx2D
import no.vestein.webapp.core.{Game, Screen, Vector2D}
import no.vestein.webapp.eventhandler.events.{KeyEvent}
import no.vestein.webapp.eventhandler.{EventBus, EventListener}
import org.scalajs.dom.window

import scala.annotation.tailrec

class PurpleRain extends Game with EventListener[KeyEvent] {
  EventBus.register(classOf[KeyEvent], this)

  val dropList: List[Drop] = Factory.createDropList(1, 600, Vector2D(-64.0f, 0), Vector2D(window.innerWidth.toInt + 32.0f, window.innerHeight.toInt))

  var grid: Boolean = false
  var debug: Boolean = false
  var gridSize: Double = 100

  def render(screen: Screen): Unit = {
    val ctx: Ctx2D = screen.ctx

    ctx.clearRect(0, 0, screen.width, screen.height)
    ctx.fillStyle = "#E6E6FA"
    ctx.fillRect(0, 0, screen.width, screen.height)

    if (grid) Grid(screen, gridSize).render()

    ctx.lineWidth = 1.0f
    dropList.foreach(_.render(ctx))

  }

  def update(delta: Double, keys: Set[Int]): Unit = {
    dropList.foreach(_.update(delta))
  }

  override def invoke(event: KeyEvent): Unit = {
    event.code match {
      case 68 => debug = !debug
      case 71 => grid = !grid
      case 90 =>
      case _ => {
        if (debug) println(event.code)
      }
    }
  }
  

  object Factory {

    private def randomDouble(min: Double, max: Double): Double = min + Math.random() * (max - min)

    def createDrop(pos: Vector2D, dim: Vector2D): Drop = {
      val x: Double = pos.x + Math.random() * dim.x
      val y: Double = pos.y + Math.random() * dim.y

      val prop: Double = Math.random() * 100
      val v: Double = prop match {
        case x if x < 50 => 0.1f
        case x if x >= 50 && x < 80 => 0.2f
        case x if x >= 80 => 0.3f
      }
      Drop(Vector2D(x, y), Vector2D(0.025f, v + randomDouble(0.025f, 0.050f)), pos, dim)
    }


    def createDropList(sizeFactor: Int, amount: Int, pos: Vector2D, dim: Vector2D): List[Drop] = {
      @tailrec def rec(acc: List[Drop], n: Int): List[Drop] = {
        if (n == 0) return acc
        return rec(createDrop(pos, dim) :: acc, n - 1)
      }
      return rec(Nil, amount)
    }

  }

}
