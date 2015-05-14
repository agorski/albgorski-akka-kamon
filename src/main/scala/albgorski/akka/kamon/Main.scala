package albgorski.akka.kamon

import akka.actor._
import com.typesafe.config.ConfigFactory
import kamon.Kamon

import scala.concurrent.duration._

object Main extends App {
  // start kamon very first
  Kamon.start(ConfigFactory.load())

  val system = ActorSystem("albgorski-app")

  import system.dispatcher

  val greeter = system.actorOf(Props[Greeter], name = "greeter")

  system.scheduler.schedule(
    0 milliseconds,
    50 milliseconds,
    greeter,
    RequestHello("hi")
  )
}