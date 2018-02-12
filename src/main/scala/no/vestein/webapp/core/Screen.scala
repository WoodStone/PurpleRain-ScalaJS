package no.vestein.webapp.core

import no.vestein.webapp.App.Ctx2D
import no.vestein.webapp.eventhandler.EventBus
import no.vestein.webapp.eventhandler.events.KeyEvent
import org.scalajs.dom

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

class Screen(val width: Int, val height: Int, val canvas: dom.html.Canvas, val game: Game) {
  type KeyboardEvent = dom.KeyboardEvent
  type FocusEvent = dom.FocusEvent

  val updateInterval: Double = 16
  val keys: ListBuffer[Int] = new ListBuffer[Int]
  val keysPressed: ListBuffer[Int] = new ListBuffer[Int]
  val ctx: Ctx2D = canvas.getContext("2d").asInstanceOf[Ctx2D]

  var active: Boolean = true
  var prevTime: Double = js.Date.now()

  canvas.tabIndex = 1
  canvas.width = width
  canvas.height = height
  canvas.onkeydown = (e : KeyboardEvent) => {
    if (!keys.contains(e.keyCode)) {
      keys += e.keyCode
      EventBus.post(KeyEvent(e.keyCode))
    }
  }
  canvas.onkeyup = (e: KeyboardEvent) => keys -= e.keyCode
  canvas.onfocus = (e: FocusEvent) => active = true
  canvas.onblur = (e: FocusEvent) => active = false

  def update(): Unit = {
    val now = js.Date.now()

    if (active) {
      val delta = now - prevTime

      if (delta > updateInterval) {
        game.update(delta, keys.toSet[Int])
        game.render(this)
        prevTime = now
      }
    } else {
      prevTime = now
    }
  }

}
