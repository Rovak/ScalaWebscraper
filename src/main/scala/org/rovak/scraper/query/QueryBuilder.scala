/**
 * 
 */
package org.rovak.scraper.query

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import scala.collection.JavaConversions._

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
class QueryBuilder(url: String) extends Serializable {

  var _url: String = null
  var _query: String = null
  
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
   * Download a page
   * 
   * @param url The page which must be scraped
   * @return Document JSoup
   */
  protected def downloadPage(url: String): Document = {
    Jsoup.connect(url).userAgent("Mozilla").timeout(0).get()
  }
  
  /**
   * Download the page and look for <a> tags with a href attribute
   * 
   * @return a list of Rows
   */
  def links: List[(Row)] = {
    val doc = downloadPage(_url)
    return doc.select(_query)
       .map(x => (
           Row(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)
       )).toList
  }
  
  /**
   * Scrape the page and collect the data
   * 
   * @param f a function which will be called for every result
   */
  def each(f: (Element) => Unit): QueryBuilder = {
    val doc = downloadPage(_url)
    for (link <- doc.select(_query)) {
    	f(link)
    }
    this
  }
}