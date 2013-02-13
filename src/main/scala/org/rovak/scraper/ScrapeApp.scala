package org.rovak.scraper

import akka.actor._

object ScrapeApp extends App {

  val system = ActorSystem()
  val scraper = system.actorOf(Props[scrapers.WebsiteScraper])
  val google = system.actorOf(Props[scrapers.GoogleScraper])

  println("Starting!")

  var links: List[String] = List("scala", "php", "javascript")

  google ! scrapers.SearchTerms(links)

  println("Scrape request send!")
}