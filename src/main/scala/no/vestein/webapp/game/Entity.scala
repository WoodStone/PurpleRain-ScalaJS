package no.vestein.webapp.game

import no.vestein.webapp.core.Vector2D

abstract class Entity(val position: Vector2D, val velocity: Vector2D) extends Updateable with Renderable {

}
