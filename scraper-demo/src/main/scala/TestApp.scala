package scraper.demo

import org.rovak.scraper.websites.Google

import org.rovak.scraper.models._
import org.jsoup.nodes.Element
import scala.collection.JavaConversions.asScalaBuffer
import org.rovak.scraper.ScrapeManager._
import org.rovak.scraper.spiders._
import org.rovak.scraper.collectors.{FileWriterCollector, Collector}
import org.rovak.scraper.models.Href


class TestResult extends Result {

  var name = ""
  var results: List[Href] = List()

  def toCSV = "\"" + name + "\"," + "\"" + results.size + "\""
}

case class EmailResult(email: String) extends Result {
  def toCSV = "\"" + email + "\""
}

object TestApp extends App {

  implicit val collector = new FileWriterCollector()

  def manual() = {

    scrape from Google.search("php+elephant") open {
      implicit page =>

        Google.results each { x: Element =>
          val link = x.select("a[href]").attr("abs:href").substring(28)
          println("searching link: " + link)
          try {
            scrape from link each (x => println("found: " + x))
          }
          catch {
            case e: Exception => println("Error while scraping " + link)
          }
        }

        Google.results collect { x: Element =>
          new TestResult {
            name = x.select("a[href]").text
            results = x.select("safa").map(x => Href(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)).toList
          }
        }
    }
  }

  def spider() = {
    val emailSpider = new Spider with EmailSpider with SitemapSpider {
      startUrls ::= "dummy"
      allowedDomains ::= "dummy"
      sitemapUrls ::= "dummy"

      onEmailFound ::= { email: String =>
        collector.collect(EmailResult(email))
      }

      onReceivedPage ::= { page: WebPage =>

      }
    }

    emailSpider.start()
  }

  spider()
}