/**
 * 
 */
package org.rovak.scraper.query

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.JavaConversions._

/**
 * @param url the full url
 * @param name the contents of the A tag
 */
case class Row(url: String, name: String)

/**
 * URL Query Builder
 * 
 * @param url the website from which to scrape
 */
class QueryBuilder(url: String) {

  protected var _url: String = null
  protected var _query: String = null
  
  /** Useless getters and setters just for test **/
  def getUrl() = url
  def setUrl(url: String) = _url = url
  
  _url = url
  
  /**
   * Set the url which has to be scraped
   * 
   * @param url full url
   */
  def from(url: String): QueryBuilder = {
    _url = url
    this
  }
  
  /**
   * Input a CSS selector to create elements
   * 
   * @param query the css selector
   */
  def select(query: String): QueryBuilder = {
    _query = query
    this
  }
  
  /**
   * Download the page and generate the result
   * 
   * @return a list of Rows
   */
  def result: List[(Row)] = {
    val doc = Jsoup.connect(_url).userAgent("Mozilla").timeout(0).get()
    return doc.select(_query)
       .map(x => (
           Row(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)
       )).toList
  }
}