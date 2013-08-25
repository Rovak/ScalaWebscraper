package org.rovak.scraper.actors

import akka.actor.Actor
import org.jsoup.Jsoup
import org.rovak.scraper.models.WebPage

class Scraper extends Actor {

  val userAgent = "Mozilla"

  val timeout = 0

  def fetchPage(pageUrl: String) = {
    Jsoup.connect(pageUrl).userAgent(userAgent).timeout(timeout).get
  }

  def receive = {
    case WebPage(url) =>
      sender ! fetchPage(url.toString)
  }
}
