import UserActor.UserMessage
import akka.actor.{Actor, Props}
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.models.Message

import scala.io.Source

class EchoBotActor extends TelegramBot with Polling with Commands with Actor {
  // Use 'def' or 'lazy val' for the token, using a plain 'val' may/will
  // lead to initialization order issues.
  // Fetch the token from an environment variable or untracked file.
  lazy val token = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromFile("bot.token").getLines().mkString)

  onCommand('hello) { implicit msg => reply("My token is SAFE!") }

  onMessage(implicit message => {
    message.from.map(_.id.toString).foreach(userId => {
      val userActor = context.child(userId) getOrElse {
        context.actorOf(UserActor.props(userId), userId)
      }
      userActor ! UserMessage(message.text.getOrElse(":)"))
    })
  })

  override def preStart(): Unit = {
    run()
  }

  override def receive: Receive = {
    case sm@SendMessage(UserMessage(userMessage)) =>
      reply(userMessage)(sm.message)
    case _ => print("command not found")
  }
}

case class SendMessage(userMessage: UserMessage)(implicit val message: Message)

object EchoBotActor {
  def props(): Props = Props(new EchoBotActor())
}


