package no.vestein.webapp.game

import no.vestein.webapp.App.Ctx2D

trait Renderable {

  def render(ctx: Ctx2D): Unit

}
