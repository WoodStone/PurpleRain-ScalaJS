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
  val dropList: List[Drop] = createDropList(1, 300, window.innerWidth.toInt, window.innerHeight.toInt)
  val screen: Screen = Screen(window.innerWidth.toInt, window.innerHeight.toInt)

  override def main(): Unit = {
    window.addEventListener("keydown", keypush)
    dom.window.setInterval(() => gameLoop(), 1)

    screen.canvas.style.display = "block"
    document.body.appendChild(screen.canvas)
  }

  def gameLoop(): Unit = {
    val now = js.Date.now()
    val delta = now - prevTime

    if (delta > 16) {
      update(delta)
      render()
      prevTime = now
    }
  }

  def update(d: Double): Unit = {
    dropList.foreach(_.update(d, screen.canvas))
  }

  def render(): Unit = {
    val ctx: Ctx2D = screen.ctx

    ctx.clearRect(0, 0, screen.canvas.width, screen.canvas.height)
    screen.ctx.fillStyle = "#E6E6FA"
    screen.ctx.fillRect(0, 0, screen.canvas.width, screen.canvas.height)

    dropList.foreach(drop => drop.render(ctx))
  }

  def createDropList(sizeFactor: Int, amount: Int, width: Int, height: Int): List[Drop] = {

    @tailrec def rec(acc: List[Drop], n: Int): List[Drop] = {
      if (n == 0) return acc

      val x: Float = Math.random().toFloat * width - 16.0f
      val y: Float = Math.random().toFloat * height - 16.0f

      return rec(Drop(Vector2D(x, y), Vector2D(0, randomFloat(0.1f, 0.3f))) :: acc, n - 1)
    }

    return rec(Nil, amount)
  }

  def keypush(e: dom.KeyboardEvent): Unit = {
    println(e.keyCode)
  }

  def randomFloat(min: Float, max: Float): Float = {
    (min + Math.random() * (max - min)).toFloat
  }

}

case class Screen(width: Int, height: Int, canvas: dom.html.Canvas = document.createElement("canvas").asInstanceOf[dom.html.Canvas]) {
  val ctx: Ctx2D = canvas.getContext("2d").asInstanceOf[Ctx2D]
  canvas.width = width
  canvas.height = height
}

case class Vector2D(var x: Float, var y: Float)

case class Drop(position: Vector2D, velocity: Vector2D) {

  def render(ctx: Ctx2D): Unit = {
    ctx.fillStyle = "purple"
    ctx.fillRect(position.x, position.y, 2, 10 * (1 + 4 * velocity.y))
  }

  def update(delta: Double, canvas: Canvas): Unit = {
    position.x = position.x + velocity.x * delta.toFloat
    position.y = position.y + velocity.y * delta.toFloat

    if (position.y > canvas.height + 32.0f)  {
      position.y = position.y - canvas.height - 64.0f
    }
  }

}
