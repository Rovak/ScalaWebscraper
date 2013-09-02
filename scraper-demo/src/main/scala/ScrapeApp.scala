package scraper.demo

import org.rovak.scraper.models._
import org.rovak.scraper.spiders._
import org.rovak.scraper.models.Href

object ScrapeApp extends App {

  new Spider {
    startUrls ::= "http://events.stanford.edu/"
    allowedDomains ::= "events.stanford.edu"

    onReceivedPage ::= { page: WebPage =>
      // Page received
    }

    onLinkFound ::= { link: Href =>
      println(s"Found link ${link.url} with name ${link.name}")
    }
  }.start()

}