package no.vestein.webapp.game

import no.vestein.webapp.App.Ctx2D
import no.vestein.webapp.core.Screen

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
