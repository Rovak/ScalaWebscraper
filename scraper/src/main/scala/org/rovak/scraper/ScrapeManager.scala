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

class Scraper(actor: ActorRef) {
  implicit val timeout = new Timeout(15 second)

  def scrape(url: String) = actor ? WebPage(new URL(url))
}

object ScrapeManager {
  val system = ActorSystem()
  val scrapeActor = system.actorOf(Props[actors.Scraper].withRouter(RoundRobinRouter(nrOfInstances = 15)), "scraper")

  implicit val scraper = new Scraper(scrapeActor)

  implicit def String2Url(url: String) = new URL(url)

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
