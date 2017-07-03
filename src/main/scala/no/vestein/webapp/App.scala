package no.vestein.webapp

import no.vestein.webapp.App.{Canvas, Ctx2D, grid, screen}
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.Node
import org.scalajs.dom.window

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.JSApp

object App extends JSApp {
  type Ctx2D = dom.CanvasRenderingContext2D
  type Canvas = dom.html.Canvas

  var grid: Boolean = false
  var debug: Boolean = false
  var prevTime: Double = js.Date.now()

  val gridSize: Double = 100
  val dropList: List[Drop] = createDropList(1, 600, Vector2D(0, 0), Vector2D(window.innerWidth.toInt, window.innerHeight.toInt))
  val screen: Screen = Screen(window.innerWidth.toInt, window.innerHeight.toInt)

  /**
    * Initial function
    */
  override def main(): Unit = {
    window.addEventListener("keydown", keypush)
    dom.window.setInterval(() => gameLoop(), 1)

    screen.canvas.style.display = "block"
    document.body.appendChild(screen.canvas)
  }

  /**
    * Main game loop.
    */
  def gameLoop(): Unit = {
    val now = js.Date.now()
    val delta = now - prevTime

    if (delta > 16) {
      update(delta)
      render()
      prevTime = now
    }
  }

  /**
    * Updates all world objects.
    * @param delta time-passed
    */
  def update(delta: Double): Unit = {
    dropList.foreach(_.update(delta))
  }

  /**
    * Renders all world objects.
    */
  def render(): Unit = {
    val ctx: Ctx2D = screen.ctx

    def debugLines(): Unit = {
      if (debug) {
        ctx.strokeStyle = "red"
        ctx.strokeRect(100, 100, screen.width - 200, screen.height - 200)
      }
    }

    ctx.clearRect(0, 0, screen.canvas.width, screen.canvas.height)
    ctx.fillStyle = "#E6E6FA"
    ctx.fillRect(0, 0, screen.canvas.width, screen.canvas.height)


    if (grid) Grid(screen, gridSize).render()
    debugLines()

    dropList.foreach(_.render(ctx))
  }

  /**
    * Creates a list of Drop objects.
    * @param sizeFactor
    * @param amount
    * @param pos
    * @param dim
    * @return
    */
  def createDropList(sizeFactor: Int, amount: Int, pos: Vector2D, dim: Vector2D): List[Drop] = {

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

  /**
    * 37 - Left
    * 38 - Up
    * 39 - Right
    * 40 - Down
    * @param e
    */
  def keypush(e: dom.KeyboardEvent): Unit = {
    println(e.keyCode)
    e.keyCode match {
      case 13 => println(dropList.filter(_.mark))
      case 38 => dropList.head.mark = !dropList.head.mark
      case 68 => debug = !debug
      case 71 => grid = !grid
      case _ => //No match
    }
  }

  /**
    * Returns a pseudorandom float.
    * @param min Minimum value
    * @param max Maximum value
    * @return a pseudorandom.
    */
  def randomDouble(min: Double, max: Double): Double = min + Math.random() * (max - min)

}

case class Grid(screen: Screen, size: Double) {

  def render(): Unit = {
    val ctx: Ctx2D = screen.ctx
    val width: Int = screen.width
    val height: Int = screen.height

    ctx.strokeStyle = "grey"
    ctx.lineWidth = 0.5f
    ctx.beginPath()
    drawHorizontal()
    drawVertical()
    ctx.stroke()
    ctx.closePath()

    def line(x: Double, y: Double, x2: Double, y2: Double): Unit = {
      ctx.moveTo(x, y)
      ctx.lineTo(x2, y2)
    }

    def drawHorizontal(): Unit = {
      for ( a <- 0 to Math.floor( height / size ).toInt ) {
        line(0, size * a, width, size * a)
      }
    }

    def drawVertical(): Unit = {
      for ( a <- 0 to Math.floor( width / size ).toInt ) {
        line(size * a, 0, size * a, height)
      }
    }
  }

}

case class Screen(width: Int, height: Int, canvas: dom.html.Canvas = document.createElement("canvas").asInstanceOf[dom.html.Canvas]) {
  val ctx: Ctx2D = canvas.getContext("2d").asInstanceOf[Ctx2D]
  canvas.width = width
  canvas.height = height
}

/**
  * Representation of a two-dimensional vector.
  * @param x
  * @param y
  */
case class Vector2D(var x: Double, var y: Double) {

  def +(o: Vector2D): Vector2D = Vector2D(x + o.x, y + o.y)
  def -(o: Vector2D): Vector2D = Vector2D(x - o.x, y - o.y)
  def length: Double = Math.sqrt(x * x + y * y)

}

/**
  * Representation of a falling drop.
  * @param position
  * @param velocity
  * @param boxPos
  * @param boxDim
  */
case class Drop(position: Vector2D, velocity: Vector2D, boxPos: Vector2D, boxDim: Vector2D, var mark: Boolean = false) {

  /**
    * Renders a drop.
    * Higher velocity, longer length.
    * @param ctx
    */
  def render(ctx: Ctx2D): Unit = {
    ctx.fillStyle = "purple"
    if (mark) ctx.fillStyle = "lime"
    ctx.fillRect(position.x, position.y, 1.0f, 10 * (1 + 4 * velocity.y))
  }

  /**
    * Updates a Drop-object according to the time-passed since last update.
    * @param delta Time passed.
    */
  def update(delta: Double): Unit = {
    position.x = position.x + velocity.x * delta
    position.y = position.y + velocity.y * delta

    if (position.y > boxPos.y + boxDim.y + 32.0f)  {
      //TODO new x position
      position.y = position.y - boxDim.y - 64.0f
    }
  }

}
