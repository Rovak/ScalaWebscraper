package org.rovak.scraper

import akka.actor._

object ScrapeApp extends App {
  
  val system = ActorSystem()
  val scraper = system.actorOf(Props[actors.WebsiteScraper])

  println("Starting!")
  
  var links: List[String] = List("https://www.google.com/search?q=scala")

  scraper ! actors.LinkList(links)
 
  println("Scrape request send!")

}