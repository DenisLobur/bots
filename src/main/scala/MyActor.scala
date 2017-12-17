import UserActor.Message
import akka.actor.Actor
import akka.event.Logging

class MyActor extends Actor {
  val log = Logging(context.system, this)

  val currentMessage: String = ""

  def receive = {
    case Message(message) => log.info(s"received test $message")
    case _ => log.info("received unknown message")
  }
}

object UserActor {

  case class Message(value: String)

}
