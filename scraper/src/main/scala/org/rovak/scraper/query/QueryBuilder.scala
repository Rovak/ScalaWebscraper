package org.rovak.scraper.query

import scala.collection.JavaConversions.asScalaBuffer

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * @param url the full url
 * @param name 	the contents of the A tag
 */
case class Row(url: String, name: String)

/**
 * URL Query Builder
 *
 * @param url the website from which to scrape
 */
class QueryBuilder(var url: String, var query: String = "") extends Serializable with Iterable[Row] {

  def iterator = links.iterator

  /**
   * Set the url which has to be scraped
   */
  def from(newUrl: String): QueryBuilder = {
    url = newUrl
    this
  }

  /**
   * Input a CSS selector to create elements
   */
  def select(newQuery: String): QueryBuilder = {
    query = newQuery
    this
  }

  /**
   * Download a page
   *
   * @return Document JSoup
   */
  protected def page: Document = {
    Jsoup.connect(url).userAgent("Mozilla").timeout(0).get()
  }

  /**
   * Download the page and look for <a> tags with a href attribute
   *
   * @return a list of Rows
   */
  def links: List[(Row)] = {
    page.select(query).map(x => Row(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)).toList
  }

  /**
   * Scrape the page and collect the data
   *
   * @param f a function which will be called for every result
   */
  def each(f: Element => Unit): QueryBuilder = {
    page.select(query).map(f)
    this
  }
}