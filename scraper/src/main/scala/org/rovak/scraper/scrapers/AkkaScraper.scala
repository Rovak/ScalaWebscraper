package org.rovak.scraper.scrapers

import akka.actor.{Props, ActorSystem, Actor}
import org.jsoup.Jsoup
import org.rovak.scraper.models.{PageNotFound, WebPage}
import java.net.URL
import akka.routing.RoundRobinRouter
import scala.concurrent.ExecutionContext
import org.rovak.scraper.scrapers.AkkaScraperActor.DownloadPage
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object AkkaScraperManager {
  val system = ActorSystem()
}

object AkkaScraperActor {
  case class DownloadPage(url: String)
}

class AkkaScraperActor extends Actor {

  import AkkaScraperActor._

  def fetchPage(pageUrl: String) = {
    try {
      new WebPage(new URL(pageUrl)) {
        doc = Jsoup
          .connect(pageUrl)
          .userAgent("Mozilla")
          .followRedirects(true)
          .timeout(0)
          .get
      }
    }
    catch {
      case e: Exception => PageNotFound()
    }
  }

  def receive = {
    case DownloadPage(url) => sender ! fetchPage(url.toString)
  }
}

/**
 * Akka scraper
 */
class AkkaScraper extends Scraper {

  import ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5.seconds)

  val scrapeActor = AkkaScraperManager.system.actorOf(Props[AkkaScraperActor].withRouter(RoundRobinRouter(nrOfInstances = 15)), "scraper")

  def downloadPage(url: String) = {
    (scrapeActor ? DownloadPage(url)) map {
      case page: WebPage => page
    }
  }
}