package org.rovak.scraper


import org.rovak.scraper.query._
import org.jsoup.nodes.{ Element, Document }


object TestApp extends App {

  def printLink(el: Element) = {
    println(el.select("a[href]").attr("abs:href"))
  }
  
	val query = Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a"

	for (link <- query) {
	  // Use the resulting url's to scrape the found websites
	  for (sublink <- Scrape from link.url select "a") {
	    println("Found: " + sublink)
	  }
	}

}