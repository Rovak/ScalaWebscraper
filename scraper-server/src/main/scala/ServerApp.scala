package scraper.demo

import akka.actor._
import akka.kernel.Bootable
import com.typesafe.config._
import akka.routing.RoundRobinRouter

/**
 * Main server which will send instructions to the clients
 */
class ServerApp extends Bootable {
  val system = ActorSystem("ServerPool", ConfigFactory.load.getConfig("server"))
  val server1 = system.actorFor("akka://Client@127.0.0.1:2554/user/query")
  val server2 = system.actorFor("akka://Client@127.0.0.1:2555/user/query")
  val routees = List[ActorRef](server1, server2)
  val router2 = system.actorOf(Props().withRouter(RoundRobinRouter(routees = routees)))

  def sendMessage = {

  }

  def startup = {

  }

  def shutdown = {
    system.shutdown()
  }
}

object ServerBootableApp {
  def main(args: Array[String]) {
    val app = new ServerApp
    println("Started Server Sending messages")
    while (true) {
      app.sendMessage
      Thread.sleep(200)
    }
  }
}
