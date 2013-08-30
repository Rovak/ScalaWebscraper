package org.rovak.scraper.spiders

import scala.xml.{Elem, XML}
import org.jsoup.nodes.Node

trait SitemapSpider {
  this: Spider =>

  var sitemapUrls = List[String]()

  def openSitemap(url: String): List[String] = {
    val xml = XML.load(url)
    (xml \\ "loc") map {
      case (x: Elem) =>
        val location = x.text
        if (location.endsWith(".xml")) openSitemap(location)
        else List(location)
    } flatMap { x: List[String] => x } toList
  }

  onStart ::= { spider: Spider =>
    sitemapUrls.foldLeft(List[String]()) {
      (list, sitemap) => list ++ openSitemap(sitemap)
    }
  }
}
