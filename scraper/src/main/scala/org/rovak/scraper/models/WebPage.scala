package org.rovak.scraper.models

import org.jsoup.nodes.Document
import scala.collection.JavaConversions._

case class WebPage(url: java.net.URL) {

  var content = ""

  var doc: Document = null

  def link = url.toString

  def links = {
    doc.select("a").map(x => new Href {
      url = x.select("a[href]").attr("abs:href")
      name = x.select("a[href]").text
    }).toList
  }

}

case class PageNotFound()

case class Href(var url: String = "", var name: String = "") extends Result {
  def toCSV: String = url
}