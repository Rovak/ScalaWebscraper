package org.rovak.scraper

import akka.actor._
import akka.routing.RoundRobinRouter
import org.rovak.scraper.models.{Result, Href, WebPage, QueryBuilder}
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import scala.collection.JavaConversions.asScalaBuffer
import org.jsoup.nodes.Element
import java.net._
import org.rovak.scraper.collectors.Collector
import org.rovak.scraper.models.WebPage
import org.rovak.scraper.scrapers.DefaultScraper

object ScrapeManager {

  implicit def String2Url(url: String) = new URL(url)

  implicit var scraper = new DefaultScraper

  def scrape = new QueryBuilder()

  implicit class StringUtils(query: String) {
    def collect(reader: Element => Result)(implicit c: Collector, page: WebPage) = {
      page.doc.select(query).map(x => c.collect(reader(x)))
    }

    def each[T](reader: Element => T)(implicit page: WebPage): List[T] = {
      page.doc.select(query).map(reader).toList
    }

    /**
     * Validate if the given URL is valid
     * @return
     */
    def isValidURL: Boolean = {
      try {
        new URL(query).toURI
        true
      }
      catch {
        case e: MalformedURLException => false
        case e: URISyntaxException => false
      }
    }
  }
}
