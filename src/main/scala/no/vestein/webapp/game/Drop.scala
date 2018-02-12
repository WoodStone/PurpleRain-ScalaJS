package no.vestein.webapp.game

import no.vestein.webapp.App.Ctx2D
import no.vestein.webapp.core.Vector2D

/**
  * Representation of a falling drop.
  *
  * @param position
  * @param velocity
  * @param boxPos
  * @param boxDim
  */
class Drop(position: Vector2D,
           velocity: Vector2D,
           boxPos: Vector2D,
           boxDim: Vector2D,
           var mark: Boolean = false)
  extends Entity(position, velocity) {

  /**
    * Renders a drop.
    * Higher velocity, longer length.
    * @param ctx
    */
  def render(ctx: Ctx2D): Unit = {
    ctx.fillStyle = "purple"
    if (mark) ctx.fillStyle = "lime"
//    ctx.fillRect(position.x, position.y, 1.0f, 10 * (1 + 4 * velocity.y))

    ctx.beginPath()
    ctx.strokeStyle = "purple"
    ctx.moveTo(position.x, position.y)
    ctx.lineTo(position.x + 5.0f, position.y + 10 * (1 + 4 * velocity.y))
    ctx.stroke()
//    ctx.closePath()

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
      position.x = Math.random() * boxDim.x
      position.y = position.y - boxDim.y - 64.0f
    }
  }

}

object Drop {

  def apply(position: Vector2D, velocity: Vector2D, boxPos: Vector2D, boxDim: Vector2D, mark: Boolean = false): Drop = {
    new Drop(position, velocity, boxPos, boxDim, mark)
  }

}
