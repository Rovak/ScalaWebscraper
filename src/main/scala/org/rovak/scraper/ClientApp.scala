package org.rovak.scraper

import akka.actor._
import akka.routing._
import akka.kernel.Bootable
import com.typesafe.config._

/**
 * Client application
 */
class ClientApp(port: Int = 2555) extends Bootable {

  var cfg = ConfigFactory.parseString("""
      akka.remote.netty { 
		  hostname 	= "127.0.0.1" 
		  port 		= """ + port + """
	}
    """)
    .withFallback(ConfigFactory.load.getConfig("client"));

  val system = ActorSystem("Client", cfg)
  val actor = system.actorOf(Props[scrapers.QueryScraper].withRouter(RoundRobinRouter(nrOfInstances = 15)), "query")

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
