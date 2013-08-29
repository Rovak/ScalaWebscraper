package org.rovak.scraper.actors

import akka.actor.Actor
import org.jsoup.Jsoup
import org.rovak.scraper.models.{PageNotFound, WebPage}
import java.net.URL

class Scraper extends Actor {

  val userAgent = "Mozilla"

  val timeout = 0

  def fetchPage(pageUrl: String) = {
    try {
      new WebPage(new URL(pageUrl)) {
        doc = Jsoup
          .connect(pageUrl)
          .userAgent(userAgent)
          .followRedirects(true)
          .timeout(timeout)
          .get
      }
    }
    catch {
      case e: Exception => PageNotFound()
    }
  }

  def receive = {
    case WebPage(url) =>
      sender ! fetchPage(url.toString)
  }
}
