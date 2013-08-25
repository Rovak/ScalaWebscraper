package scraper.demo

import org.rovak.scraper.websites.Google

import org.rovak.scraper.{Collector, ScrapeManager}
import org.rovak.scraper.models.{Href}
import org.jsoup.nodes.Element
import scala.collection.JavaConversions.asScalaBuffer
import org.rovak.scraper.ScrapeManager._


object TestApp extends App {

  implicit val collector = new Collector()

  def scrapeSomething = { x: Element =>
    new TestResult {
      name = x.select("a[href]").text
      results = x.select("safa").map(x => Href(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)).toList
    }
  }

  scrape from Google.search("php elephant") open { implicit page =>

    "a" collect { x: Element =>
      new TestResult {
        name = x.select("a[href]").text
        results = x.select("safa").map(x => Href(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)).toList
      }
    }

    "sadsad" collect scrapeSomething

    "asf safa" each { x: Element =>

    }

  }

  scrape select Google.results from Google.search("php elephant") each { href =>

    scrape select "a" from href.url map { link =>

    }
  }
}