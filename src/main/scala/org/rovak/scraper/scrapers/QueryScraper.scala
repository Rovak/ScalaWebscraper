package org.rovak.scraper.scrapers

import akka.actor.Actor
import org.rovak.scraper.query._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.JavaConversions._

/**
 * Sends a message to scrape a QueryBuilder
 *
 * @param queryBuilder
 */
case class Query(queryBuilder: QueryBuilder)

class QueryScraper extends Actor {
  
  

  /**
   * @param qb
   */
  def ScrapeQuery(qb: QueryBuilder) = {
    println("New Actor")
    val doc = Jsoup.connect(qb._url).userAgent("Mozilla").timeout(0).get()
    val list = doc.select(qb._query)
      .map(x => (
        Row(x.select("a[href]").attr("abs:href"), x.select("a[href]").text))).toList

    for (row <- list) {
      //println(row)
    }
  }

  def receive = {
    case Query(queryBuilder) => {
      ScrapeQuery(queryBuilder)
    }
  }
}