package org.rovak.scraper

import akka.actor._
import akka.kernel.Bootable
import com.typesafe.config._
import org.rovak.scraper.query._
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import org.rovak.tables.LinksTable

import org.jsoup.nodes.{Element, Document}

object TestApp extends App {

  var url: String = "Test"
  val db = Database.forURL("jdbc:mysql://localhost/scala_scrape", driver = "com.mysql.jdbc.Driver", user = "root", password = "")
 
  for (link <- Scrape google "php elephant" links) {
    Scrape from link.url select "a" each saveLink
  }

  def saveLink(el: Element) = {
    db withSession LinksTable.insert(0, el.select("a[href]").attr("abs:href"))
  }
}