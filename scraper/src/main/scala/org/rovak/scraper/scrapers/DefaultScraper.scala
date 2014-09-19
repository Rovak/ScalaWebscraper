package org.rovak.scraper.scrapers

import scala.concurrent.{ExecutionContext, Future}
import org.rovak.scraper.models.{PageNotFound, WebPage}
import java.net.URL
import org.jsoup.Jsoup

/**
 * Default scraper
 */
class DefaultScraper extends Scraper {

  import ExecutionContext.Implicits.global

  def downloadPage(pageUrl: String) = {
    downloadPage((pageUrl, ""))
  }

  def downloadPage(urlAndReferrer: (String, String)) = Future {
      new WebPage(new URL(urlAndReferrer._1)) {
        doc = Jsoup
          .connect(urlAndReferrer._1)
          .referrer(urlAndReferrer._2)
          .userAgent("Mozilla")
          .followRedirects(true)
          .timeout(0)
          .get
      }
  }
}
