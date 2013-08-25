package org.rovak.scraper.spiders

import org.rovak.scraper.models.WebPage

class TestSpider extends Spider {

  name = "Test"
  allowedDomains = List("asfdas")
  startUrls = List("www.google.nl")

  def onReceivedPage = {
    case WebPage(url) => "found"
  }

}
