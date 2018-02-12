package no.vestein.webapp.eventhandler

import org.scalatest.FlatSpec

class EventBusTest extends FlatSpec {

  "The BUS" should "post to correct event" in {
    var isPost: Boolean = false

    class Event$1 extends Event
    class Event$1Listener extends EventListener[Event$1] {
      override def invoke(event: Event$1): Unit = isPost = true
    }
    class Event$1Listener$2 extends EventListener[Event] {
      override def invoke(event: Event): Unit = isPost = false
    }

    val t1: Event$1Listener = new Event$1Listener
    val t2: Event$1Listener$2 = new Event$1Listener$2
    EventBus.register(classOf[Event$1], t1)
    EventBus.register(classOf[Event], t2)

    assert(isPost)
  }

}
