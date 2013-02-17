package org.rovak.scraper

import akka.actor._
import akka.kernel.Bootable
import com.typesafe.config._
import org.rovak.scraper.query._

/**
 * Main server which will send instructions to the clients
 */
class ServerApp extends Bootable {
  val system = ActorSystem("ServerPool", ConfigFactory.load.getConfig("server"))
  val actor = system.actorFor("akka://Client@127.0.0.1:2554/user/query");
  
  def sendMessage = {
    actor ! scrapers.Query(Scrape from "http://www.google.nl/search?q=php" select "a")
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
