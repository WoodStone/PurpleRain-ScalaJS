package no.vestein.webapp.core

trait Game {

  def render(screen: Screen): Unit
  def update(delta: Double, keys: Set[Int]): Unit

}
