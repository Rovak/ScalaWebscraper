package org.rovak.scraper.actors

import akka.actor._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.JavaConversions._

case class Links(links: List[String])
case class LinkResult(results: List[String])
case class Website(url: String)

/**
 * Simple actor who retrieves websites and searches for links
 */
class WebsiteScraper extends Actor {

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
  def getLinks(url: String): List[(String, String)] = {

    var links: List[(String, String)] = List()

    try {
      val doc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get()

      if (url.indexOf("www.google.com") > 0) {
        links = doc.select("#res li.g h3.r a").map(x => (
          retrieveGoogleLink(x.select("a[href]").attr("abs:href")),
          x.select("a[href]").text)).toList
      } else {
        links = doc.select("a").map(x => (
          x.select("a[href]").attr("abs:href"),
          x.select("a[href]").text)).toList
      }

      for (link <- links) {
        println("Url: " + link._1 + ", Website: " + link._2)
        val websiteScraper = context.system.actorOf(Props[WebsiteScraper])
        websiteScraper ! Website(link._1)
      }
    } catch {
      case e: Exception => { println("Could not scrape url: " + e.getMessage()) }
    }

    links
  }

  /**
   * Iterate through a list of websites and call the getLinks for each
   *
   * @param links List[String]
   * @return List[String]
   */
  def scrapeLinks(links: List[String]): List[String] = {

    for (link <- links) {
      val websiteScraper = context.system.actorOf(Props[WebsiteScraper])
      websiteScraper ! Website(link)
    }

    // TODO: valid return value
    List("Return value")
  }

  /**
   * Message Listener
   */
  def receive = {
    case Links(links) => {
      scrapeLinks(links)
    }
    case Website(url) => {
      getLinks(url)
    }
  }
}