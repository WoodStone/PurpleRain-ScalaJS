package no.vestein.webapp

import no.vestein.webapp.App.{Canvas, Ctx2D}
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

  var prevTime: Double = js.Date.now()
  val dropList: List[Drop] = createDropList(1, 300, Vector2D(100, 100), Vector2D(window.innerWidth.toInt - 200, window.innerHeight.toInt - 200))
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

    ctx.clearRect(0, 0, screen.canvas.width, screen.canvas.height)
    ctx.fillStyle = "#E6E6FA"
    ctx.fillRect(0, 0, screen.canvas.width, screen.canvas.height)

    ctx.strokeStyle = "red"
    ctx.strokeRect(100, 100, screen.width - 200, screen.height - 200)

    dropList.foreach(drop => drop.render(ctx))
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

      val x: Float = pos.x + Math.random().toFloat * dim.x
      val y: Float = pos.y + Math.random().toFloat * dim.y

      return rec(Drop(Vector2D(x, y), Vector2D(0, randomFloat(0.1f, 0.3f)), pos, dim) :: acc, n - 1)
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
    }
  }

  /**
    * Returns a pseudorandom float.
    * @param min Minimum value
    * @param max Maximum value
    * @return a pseudorandom.
    */
  def randomFloat(min: Float, max: Float): Float = {
    (min + Math.random() * (max - min)).toFloat
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
case class Vector2D(var x: Float, var y: Float)

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
    ctx.fillRect(position.x, position.y, 2, 10 * (1 + 4 * velocity.y))
  }

  /**
    * Updates a Drop-object according to the time-passed since last update.
    * @param delta Time passed.
    */
  def update(delta: Double): Unit = {
    position.x = position.x + velocity.x * delta.toFloat
    position.y = position.y + velocity.y * delta.toFloat

    if (position.y > boxPos.y + boxDim.y + 32.0f)  {
      position.y = position.y - boxDim.y - 64.0f
    }
  }

}
