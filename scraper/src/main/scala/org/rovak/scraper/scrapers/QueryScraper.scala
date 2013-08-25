package org.rovak.scraper.scrapers

import akka.actor.Actor
import org.rovak.scraper.query._
import org.jsoup.Jsoup
import scala.collection.JavaConversions._

/**
 * Query messages, contains a QueryBuilder which contains scrape information
 */
case class Query(queryBuilder: QueryBuilder)

/**
 * Handles QueryBuilder instructions
 */
class QueryScraper extends Actor {

  def ScrapeQuery(qb: QueryBuilder) = {
    val doc = Jsoup.connect(qb.url).userAgent("Mozilla").timeout(0).get()
    val list = doc.select(qb.query)
      .map(x => Href(x.select("a[href]").attr("abs:href"), x.select("a[href]").text)).toList
  }

  def receive = {
    case Query(queryBuilder) => {
      ScrapeQuery(queryBuilder)
    }
  }
}