package org.rovak.scraper.models

import org.jsoup.nodes.Document

case class WebPage(url: java.net.URL) {

  var content = ""

  var doc: Document = null

}

case class Href(var url: String = "", var name: String = "") extends Result {
  def toCSV: String = url
}