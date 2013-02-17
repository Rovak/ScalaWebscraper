package org.rovak.scraper

import org.rovak.scraper.query._
import akka.actor._
import akka.routing._
import com.typesafe.config._

/**
 * Demonstrates the QueryBuilder usage
 */
object QueryApp extends App {

  val query = Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a"
  val system = ActorSystem("Default", ConfigFactory.load())
  val QueryActor = system.actorOf(Props[scrapers.QueryScraper].withDispatcher("akka.actor.my-dispatcher"), "query")

  for (link <- Scrape google "php elephant" links) {
    QueryActor ! scrapers.Query(Scrape from link.url select "a")
  }

  /*
  for (link <- query.links) {
    for (sublink <- Scrape from link.url select "a" links) {
      println("Found: " + sublink)
    }
  }

  /**
   * Shorter example to retrieve results from Google
   */
  for (link <- Scrape google "php elephant" links) {
    println("Elephant found: " + link.name)
  }
  */
}