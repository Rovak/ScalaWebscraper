package org.rovak.scraper.scrapers

import akka.actor._
import org.jsoup.Jsoup
import scala.collection.JavaConversions._

case class Links(links: List[String])

case class LinkResult(results: List[String])

case class Website(url: String)

/**
 * Simple actor who retrieves websites and searches for links
 */
class WebsiteScraper extends Actor {

  /**
   * Retrieve all the <a> tags from the given url
   *
   * @param url String A website url which will be scraped for links
   * @return List[(String, String)]
   */
  def getLinks(url: String): List[(String, String)] = {

    var links: List[(String, String)] = List()

    try {
      val doc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get()

      links = doc.select("a").map(x => (
        x.select("a[href]").attr("abs:href"),
        x.select("a[href]").text)).toList

      links.map { link =>
        val websiteScraper = context.system.actorOf(Props[WebsiteScraper])
        websiteScraper ! Website(link._1)
      }
    } catch {
      case e: Exception => {
        println("Could not scrape url: " + e.getMessage)
      }
    }

    links
  }

  /**
   * Iterate through a list of websites and call the getLinks for each
   *
   * @param links List[String]
   * @return List[String]
   */
  def scrapeLinks(links: List[String]) = {
    links.map { link =>
      val websiteScraper = context.system.actorOf(Props[WebsiteScraper])
      websiteScraper ! Website(link)
    }
  }

  def receive = {
    case Links(links) => {
      scrapeLinks(links)
    }
    case Website(url) => {
      getLinks(url)
    }
  }
}