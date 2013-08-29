package scraper.demo

import org.rovak.scraper.websites.Google

import org.rovak.scraper.Collector
import org.rovak.scraper.models.{Result, Href}
import org.jsoup.nodes.Element
import scala.collection.JavaConversions.asScalaBuffer
import org.rovak.scraper.ScrapeManager._
import org.rovak.scraper.spiders.EmailSpider


class TestResult extends Result {

  var name = ""
  var results: List[Href] = List()

  def toCSV = "\"" + name + "\"," + "\"" + results.size + "\""
}

object TestApp extends App {

  def manual() = {
    implicit val collector = new Collector()

    scrape from Google.search("php+elephant") open {
      implicit page =>

        Google.results each { x: Element =>
          val link = x.select("a[href]").attr("abs:href").substring(28)
          println("searching link: " + link)
          try {
            scrape from link each(x => println("found: " + x))
          }
          catch { case e: Exception => println("Error while scraping " + link) }
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
    val emailSpider = new EmailSpider {
      startUrls = List("http://www.google.nl/")
      allowedDomains = List("www.google.eu")
      override def onEmailFound = { email =>
        println("email found: " + email)
      }
    }

    emailSpider.start()
  }

  spider()
}