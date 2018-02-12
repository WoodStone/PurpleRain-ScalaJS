package no.vestein.webapp.eventhandler

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object EventBus {
  type EventListener = no.vestein.webapp.eventhandler.EventListener[_ <: Event]
  type EventT = T forSome { type T <: Event}

  private val BUS: mutable.HashMap[Class[_ <: Event], ListBuffer[EventListener]] = mutable.HashMap.empty

  def register[T](eventType: Class[_ <: Event], listener: EventListener): Boolean = {
    BUS.get(eventType) match {
      case Some(list) => list += listener
      case None => BUS.put(eventType, ListBuffer(listener))
    }

    return true
  }

  def post(event: EventT): Unit = {
    BUS.get(event.getClass) match {
      case Some(list) => {
        val l: List[no.vestein.webapp.eventhandler.EventListener[event.type]] = list.toList.asInstanceOf[List[no.vestein.webapp.eventhandler.EventListener[event.type]]]
        l.foreach(_.invoke(event))
      }
      case None =>
    }
  }

}
