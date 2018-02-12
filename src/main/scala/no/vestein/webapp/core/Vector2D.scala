package no.vestein.webapp.core

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
