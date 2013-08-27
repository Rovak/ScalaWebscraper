package org.rovak.scraper.websites


object Google {

  val results = "#res li.g h3.r a"

  def search(term: String) = {
    "http://www.google.com/search?q=" + term.replace(" ", "%20")
  }
}
