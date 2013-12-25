package org.rovak.scraper.scrapers

import scala.concurrent.Future
import org.rovak.scraper.models.{PageNotFound, WebPage}
import java.net.URL
import org.jsoup.Jsoup

/**
 * Default scraper
 */
class DefaultScraper extends Scraper {
  def downloadPage(pageUrl: String) = Future {
      new WebPage(new URL(pageUrl)) {
        doc = Jsoup
          .connect(pageUrl)
          .userAgent("Mozilla")
          .followRedirects(true)
          .timeout(0)
          .get
      }
  }
}
