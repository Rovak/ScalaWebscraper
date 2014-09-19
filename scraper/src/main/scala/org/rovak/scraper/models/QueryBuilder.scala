package org.rovak.scraper.models

import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._
import scala.collection.JavaConversions._
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.rovak.scraper.scrapers.Scraper

case class FromClass(f: QueryBuilder => String) {
  def execute(qb: QueryBuilder) = f(qb)
}

class QueryBuilder(implicit val scraper: Scraper, var pageUrl: String = "", var query: String = "", var referrer: String = "") extends Serializable with Iterable[Href] {

  import ExecutionContext.Implicits.global

  def iterator = Await.result(links, 5 second).iterator

  def from(newUrl: String): QueryBuilder = {
    pageUrl = newUrl
    this
  }

  def from(urlAndReferrer: (String, String)): QueryBuilder = {
    pageUrl = urlAndReferrer._1
    referrer = urlAndReferrer._2
    this
  }

  def from(func: FromClass): QueryBuilder = {
    func.execute(this)
    this
  }

  def select(newQuery: String): QueryBuilder = {
    query = newQuery
    this
  }

  /**
   * Read the page and execute the method on success
   * @param webPage
   * @return
   */
  def open(webPage: WebPage => Unit): QueryBuilder = {
    page onSuccess { case (page: WebPage) => webPage(page) }
    this
  }

  /**
   * Download a page
   */
  protected def page = scraper.downloadPage((pageUrl, referrer))

  /**
   * Download the page and look for <a> tags with a href attribute
   */
  def links = {
    page map {
      case (x: WebPage) => {
        x.doc.select(query).map(x =>
          new Href {
            url = x.select("a[href]").attr("abs:href")
            name = x.select("a[href]").text
          }).toList
      }
    }
  }

  /**
   * Scrape the page and collect the data
   *
   * @param f a function which will be called for every result
   */
  def each(f: Element => Unit): QueryBuilder = {
    page map {
      case (x: WebPage) => x.doc.select(query)
    } onSuccess {
      case (x: Elements) => x.map(f)
    }
    this
  }
}
