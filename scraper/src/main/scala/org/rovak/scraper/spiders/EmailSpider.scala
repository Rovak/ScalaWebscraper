package org.rovak.scraper.spiders

import org.rovak.scraper.models.WebPage
import java.util.regex.Pattern

trait EmailSpider {
  this: Spider =>

  var foundEmails = List[String]()

  val searchPattern = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+"

  private val pattern = Pattern.compile(searchPattern)

  var onEmailFound = List[String => Unit]()

  onReceivedPage ::= {
    case page: WebPage if page.doc != null =>
      try {
        val m = pattern.matcher(page.doc.body().text)
        while (m.find()) {
          onEmailFound.foreach(_(m.group))
          foundEmails ::= m.group
        }
      }
      catch {
        case e: Exception =>
      }
    case _ =>
  }
}
