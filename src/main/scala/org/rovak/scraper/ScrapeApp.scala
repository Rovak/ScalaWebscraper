package org.rovak.scraper

import akka.actor._

object ScrapeApp extends App {

  val system = ActorSystem()
  system.actors

  val scraper = system.actorOf(Props[actors.WebsiteScraper])

  println("Starting!")

  var links: List[String] = List(
    "https://www.google.com/search?q=scala",
    "https://www.google.com/search?q=php",
    "https://www.google.com/search?q=javascript")

  for (link <- links) {
    println("Scraping website: " + link)
    scraper ! actors.Website(link)
  }

  println("Scrape request send!")
}