package org.rovak.scraper

import akka.actor._

/**
 * Main
 */
object ScrapeApp extends App {

  val system = ActorSystem()
  val scraper = system.actorOf(Props[scrapers.WebsiteScraper])
  val google = system.actorOf(Props[scrapers.GoogleScraper])
  
  println("Starting!")

  var searchTerms: List[String] = List("scala", "php", "javascript")

  println("Scrape request send!")
}