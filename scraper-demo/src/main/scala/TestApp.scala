package scraper.demo

import org.rovak.scraper.query._
import org.jsoup.nodes.{ Element, Document }


object TestApp extends App {

  Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a" map { link =>
    Scrape from link.url select "a" map { sublink =>
      println("Found: " + sublink)
    }
  }
}