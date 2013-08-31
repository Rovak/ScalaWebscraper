package org.rovak.scraper.spiders

import java.net.URL
import scala.xml.{Elem, XML}

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
    } flatMap { x: List[String] => x} toList
  }

  def startUrlsSiteMaps = {
    startUrls.map { startUrl: String =>
      val start = new URL(startUrl)
      s"${start.getProtocol}://${start.getHost}/sitemap.xml"
    }
  }

  onStart ::= { spider: Spider =>
    (sitemapUrls ++ startUrlsSiteMaps).foldLeft(List[String]()) {
      (list, sitemap) => list ++ openSitemap(sitemap)
    }
  }
}
