package org.rovak.scraper

import akka.actor._
import akka.kernel.Bootable
import com.typesafe.config._

/**
 * Client application
 */
class ClientApp extends Bootable {
  val system = ActorSystem("Client", ConfigFactory.load.getConfig("client"))
  val actor = system.actorOf(Props[scrapers.QueryScraper], "query")

  def startup = {

  }

  def shutdown = {
    system.shutdown()
  }
}

/**
 * Main
 */
object ClientListener {
  def main(args: Array[String]) {
    new ClientApp
    println("Started Application - waiting for instructions")
  }
}
