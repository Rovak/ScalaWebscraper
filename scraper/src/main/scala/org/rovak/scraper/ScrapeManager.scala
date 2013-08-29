package org.rovak.scraper

import akka.actor._
import akka.routing.RoundRobinRouter
import org.rovak.scraper.models.{Result, Href, WebPage, QueryBuilder}
import org.rovak.scraper.scrapers.SearchTerm
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import scala.collection.JavaConversions.asScalaBuffer
import org.jsoup.nodes.Element
import java.net.URL

class Scraper(actor: ActorRef) {
  implicit val timeout = new Timeout(15 second)

  def scrape(url: String) = actor ? WebPage(new URL(url))
}

object ScrapeManager {
  val system = ActorSystem()
  val scraper = system.actorOf(Props[actors.Scraper].withRouter(RoundRobinRouter(nrOfInstances = 15)), "scraper")

  implicit val scrapeMgr = new Scraper(scraper)

  implicit def String2Url(url: String) = new URL(url)

  def scrape = new QueryBuilder()

  def collect(query: String, reader: => Href)(implicit c: Collector) = {

  }

  implicit class Test(query: String) {
    def collect(reader: Element => Result)(implicit c: Collector, page: WebPage) = {
      page.doc.select(query).map(x => c.collect(reader(x)))
    }

    def each[T](reader: Element => T)(implicit page: WebPage): List[T] = {
      page.doc.select(query).map(reader).toList
    }
  }

}
