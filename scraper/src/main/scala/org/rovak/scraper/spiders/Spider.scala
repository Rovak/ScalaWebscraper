package org.rovak.scraper.spiders

import org.rovak.scraper.models.{Href, WebPage}
import org.rovak.scraper.ScrapeManager


class Spider {

  import ScrapeManager._

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

  /**
   * Triggered when a page has been downloaded
   */
  var onReceivedPage = List[WebPage => Unit]()

  /**
   * Triggered just before the spider starts searching
   *
   * Additional start urls can be returned by the method
   */
  var onStart = List[Spider => List[String]]()

  /**
   * Triggered every time a link is found
   */
  var onLinkFound = List[Href => Unit]()

  /**
   * Before reading a page check if it is allowed
   * @param page page which will be scraped
   * @return if the page is allowed to be read
   */
  def beforeReadingPage(page: WebPage) = {
    allowedDomains.contains(page.url.getHost) && !crawledPages.contains(page)
  }

  /**
   * Scrape a single page
   *
   * @param page page to scrape
   */
  def scrapePage(page: WebPage): Unit = {
    try {
      if (beforeReadingPage(page)) {
        crawledPages ::= page
        scrape from page.link open { page =>
          onReceivedPage.foreach(_(page))
          page.links.foreach { link =>
            onLinkFound.foreach(y => y(link))
            scrapePage(WebPage(link.url))
          }
        }
      }
    }
    catch {
      case invalidUrl: java.net.MalformedURLException => println("Invalid URL")
      case e: Exception => println("Error")
    }
  }

  /**
   * Start running the spider
   */
  def start() = {
    onStart.foldLeft(startUrls) {
      case (urls, current) => urls ++ current(this)
    } foreach (x => scrapePage(new WebPage(x)))
  }
}
