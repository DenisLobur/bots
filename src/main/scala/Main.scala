import akka.actor.ActorSystem

object Main {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("my-actr-system")
    actorSystem.actorOf(EchoBotActor.props(), "echo-bot-actor")
  }
}
