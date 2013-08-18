package org.rovak.scraper.query

import java.net.URLEncoder

/**
 * QueryBuilder factory
 *
 * Enables you to start a query with "Scrape"
 *
 * Example: Scrape from "http://www.google.com" select "a"
 */
object Scrape {

  /**
   * @param url the website which to scrape
   * @return querybuilder which scrapes the given website
   */
  def from(url: String): QueryBuilder = {
    return new QueryBuilder(url)
  }

  /**
   * Shortcut to search from google
   *
   * @param query what to search on google
   * @return configured QueryBuilder
   */
  def google(query: String): QueryBuilder = {
    val qb = new QueryBuilder("http://www.google.com/search?q=" + URLEncoder.encode(query))
    qb select "#res li.g h3.r a"
  }

  /**
   * Execute a google result with automatic iteration over the links
   *
   * @param query the search query
   * @param f closure which will be iterated
   */
  def google(query: String, f: (String) => Unit): Unit = {
    for (el <- google(query).links) {
      f(el.url)
    }
  }
}