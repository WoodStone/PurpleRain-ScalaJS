package no.vestein.webapp.eventhandler

trait EventListener[T <: Event] {

  def invoke(event: T)

}
