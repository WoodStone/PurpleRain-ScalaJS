package no.vestein.webapp

import no.vestein.webapp.core.Screen
import no.vestein.webapp.game.PurpleRain
import org.scalajs.dom
import org.scalajs.dom.{document, window}

import scala.scalajs.js.JSApp

object App extends JSApp {
  type Ctx2D = dom.CanvasRenderingContext2D

  val canvas: dom.html.Canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
  val screen: Screen = new Screen(window.innerWidth.toInt, window.innerHeight.toInt, canvas, new PurpleRain)

  override def main(): Unit = {

    dom.window.setInterval(() => screen.update(), 1)

    canvas.style.display = "block"
    document.body.appendChild(canvas)

  }

}
