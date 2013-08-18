package org.rovak.scraper.scrapers

import akka.actor._
import org.jsoup.Jsoup
import scala.collection.JavaConversions._

case class SearchTerms(searchTerms: List[String])

case class SearchTerm(searchTerm: String)

/**
 * Google actors which scrapes searchterm
 */
class GoogleScraper extends Actor {

  /**
   * @param link String A link from a Google search result
   * @return String
   */
  def retrieveGoogleLink(link: String) = {
    link.substring(link.indexOf("?q=") + 3, link.indexOf("&sa"))
  }

  /**
   * @param url String A website url which will be scraped for links
   * @return List[(String, String)]
   */
  def getResults(url: String) = {

    var links: List[(String, String)] = List()

    try {
      val doc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get()

      links = doc.select("#res li.g h3.r a").map(x => (
        retrieveGoogleLink(x.select("a[href]").attr("abs:href")),
        x.select("a[href]").text)).toList

      for (link <- links) {
        println("Url: " + link._1 + ", Website: " + link._2)
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
  def scrapeSearchTerms(links: List[String]) = {
    links.map { link =>
      val websiteScraper = context.system.actorOf(Props[GoogleScraper])
      websiteScraper ! SearchTerm(link)
    }
  }

  /**
   * Message Listener
   */
  def receive = {
    case SearchTerms(links) => {
      scrapeSearchTerms(links)
    }
    case SearchTerm(link) => {
      getResults("http://www.google.com/search?q=" + link)
    }
  }
}