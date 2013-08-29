package org.rovak.scraper.spiders

import org.rovak.scraper.models.WebPage
import org.rovak.scraper.ScrapeManager


abstract class Spider {

  var name: String = "unknown"

  /**
   * Allowed domains
   */
  var allowedDomains = List[String]()

  /**
   * Urls from which the crawling will start
   */
  var startUrls = List[String]()

  /**
   * All crawled pages
   */
  var crawledPages = List[WebPage]()

  def onReceivedPage: WebPage => Unit

  /**
   * Before reading a page check if it is allowed
   * @param page page which will be scraped
   * @return if the page is allowed to be read
   */
  def beforeReadingPage(page: WebPage) = {
    allowedDomains.contains(page.url.getHost) && !crawledPages.contains(page)
  }

  /**
   * Scrape a single page, it will check if its valid and if
   * @param page page to scrape
   */
  def scrapePage(page: WebPage): Unit = {
    import ScrapeManager._
    if (beforeReadingPage(page)) {
      crawledPages ::= page
      scrape from page.link open {
        page =>
          onReceivedPage(page)
          page.links.map(link => scrapePage(WebPage(link.url)))
      }
    }
  }

  def start() = {
    import ScrapeManager._
    startUrls.foreach(x => scrapePage(new WebPage(x)))
  }
}
