package org.rovak.scraper

import org.rovak.scraper.query._

/**
 * Demonstrates the QueryBuilder usage
 */
object QueryApp extends App {

  val query = Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a"

  for (link <- query.result) {
    for (sublink <- Scrape from link.url select "a" result) {
      println("Found: " + sublink)
    }
  }
  
  /**
   * Shorter example to retrieve results from Google
   */
  for (link <- Scrape google "php elephant" result) {
    println("Elephant found: " + link.name)
  }
}