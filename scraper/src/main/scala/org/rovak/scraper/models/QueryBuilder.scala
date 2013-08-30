package org.rovak.scraper.models

import org.rovak.scraper.Scraper
import scala.collection.JavaConversions.asScalaBuffer
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Await}

case class FromClass(f: QueryBuilder => String) {
  def execute(qb: QueryBuilder) = f(qb)
}

class QueryBuilder(implicit scraper: Scraper, var url: String = "", var query: String = "") extends Serializable with Iterable[Href] {

  import ExecutionContext.Implicits.global

  def iterator = Await.result(links, 5 second).iterator

  def from(newUrl: String): QueryBuilder = {
    url = newUrl
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
   * @param x
   * @return
   */
  def open(x: WebPage => Unit): QueryBuilder = {
    page onSuccess {
      case (page: WebPage) => x(page)
    }
    this
  }

  /**
   * Download a page
   */
  protected def page = {
    scraper.scrape(url)
  }

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
  def each(f: Href => Unit): QueryBuilder = {
    links onSuccess {
      case (x: List[Href]) => x.map(f)
    }
    this
  }
}