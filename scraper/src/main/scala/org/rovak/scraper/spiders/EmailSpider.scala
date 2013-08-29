package org.rovak.scraper.spiders

import org.rovak.scraper.models.WebPage
import java.util.regex.Pattern

abstract class EmailSpider extends Spider {

  var foundEmails = List[String]()

  def onReceivedPage = {
    case page: WebPage if page.doc != null =>
      try {
        val m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(page.doc.body().text)
        while (m.find()) {
          onEmailFound(m.group)
          foundEmails ::= m.group
        }
      }
      catch {
        case e: Exception =>
      }
    case _ =>
  }

  def onEmailFound: String => Unit = { x => }
}
