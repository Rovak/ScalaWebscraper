package org.rovak.scraper.scrapers

import org.rovak.scraper.models.WebPage
import scala.concurrent.Future

trait Scraper {
  def downloadPage(url: String): Future[WebPage]
}
