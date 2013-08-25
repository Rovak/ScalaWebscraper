package org.rovak.scraper.spiders

import org.rovak.scraper.models.WebPage


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
   * @param page
   * @return
   */
  def beforeReadingPage(page: WebPage) = {
    allowedDomains.contains(page.url.getHost) && !crawledPages.contains(page)
  }

}
