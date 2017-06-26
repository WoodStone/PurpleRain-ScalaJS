package no.vestein.webapp

import no.vestein.webapp.App.Ctx2D
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.Node
import org.scalajs.dom.window

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.JSApp

object App extends JSApp {
  type Ctx2D = dom.CanvasRenderingContext2D

  var prevTime: Double = js.Date.now()
  val dropList: List[Drop] = createDropList(1, 300)
  val canvas: Canvas = Canvas(800, 600)

  override def main(): Unit = {
    window.addEventListener("keydown", keypush)
    println(dropList)
    dom.window.setInterval(() => gameLoop(), 1)

    document.body.appendChild(canvas.canvas)
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

  def update(d: Double) = {
    dropList.foreach(_.update(d, canvas))
  }

  def render(): Unit = {
    val ctx: Ctx2D = canvas.ctx

    ctx.clearRect(0, 0, canvas.width, canvas.height)
    canvas.ctx.fillStyle = "#E6E6FA"
    canvas.ctx.fillRect(0, 0, canvas.width, canvas.height)

    dropList.foreach(drop => drop.render(ctx))
  }

  def initCanvas(): dom.html.Canvas = {
    val canvas: dom.html.Canvas = document.createElement("canvas").asInstanceOf[dom.html.Canvas]
    val ctx: Ctx2D = canvas.getContext("2d").asInstanceOf[Ctx2D]
    val n: Int = 400

    canvas.width = n
    canvas.height= n

    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, n, n)

    ctx.fillStyle = "red"
    ctx.fillRect(200, 200, 20, 20)

    canvas
  }

  def createDropList(sizeFactor: Int, amount: Int): List[Drop] = {

    @tailrec def rec(acc: List[Drop], n: Int): List[Drop] = {
      if (n == 0) return acc

      val x: Float = Math.random().toFloat * 800.0f - 16.0f
      val y: Float = Math.random().toFloat * 800.0f - 16.0f

      return rec(Drop(Vector2D(x, y), Vector2D(0, Math.random().toFloat * 0.2f)) :: acc, n - 1)
    }

    return rec(Nil, amount)
  }

  def keypush(e: dom.KeyboardEvent): Unit = {
    println(e.keyCode)
    appendP(document.body, e.keyCode.toString)
  }

  def appendP(targetNode: Node, text: String): Unit = {
    val parentNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parentNode.appendChild(textNode)
    targetNode.appendChild(parentNode)
  }

}

case class Canvas(width: Int, height: Int, canvas: dom.html.Canvas = document.createElement("canvas").asInstanceOf[dom.html.Canvas]) {
  val ctx: Ctx2D = canvas.getContext("2d").asInstanceOf[Ctx2D]
  canvas.width = width
  canvas.height = height
}

case class Vector2D(var x: Float, var y: Float)

case class Drop(position: Vector2D, velocity: Vector2D) {

  def render(ctx: Ctx2D): Unit = {
    ctx.fillStyle = "purple"
    ctx.fillRect(position.x, position.y, 2, 10)
  }

  def update(delta: Double, canvas: Canvas): Unit = {
    position.x = position.x + velocity.x * delta.toFloat
    position.y = position.y + velocity.y * delta.toFloat

    if (position.y > canvas.height + 32.0f)  {
      position.y = position.y - canvas.height - 64.0f
    }
  }

}
