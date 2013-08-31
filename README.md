Scala Webscraper
================

## Getting started

The project is build with Scala 2.10.2 and sbt 0.13.0, both can be installed
using this [install script](https://gist.github.com/Rovak/4967148)

To try the example navigate to the project folder and run `sbt "project scraper-demo" run`
which will start the example scraper

## DSL

The webscraper provides a simple DSL to write scrape rules

```scala
import org.rovak.scraper.ScrapeManager._

object Google {
  val results = "#res li.g h3.r a"
  def search(term: String) = {
    "http://www.google.com/search?q=" + term.replace(" ", "+")
  }
}

// Open the search results page for the query "php elephant"
scrape from Google.search("php elephant") open { implicit page =>

  // Iterate through every result link
  Google.results each { x: Element =>
  
    val link = x.select("a[href]").attr("abs:href").substring(28)
    if (link.isValidURL) {

      // Iterate through every found link in the found page
      scrape from link each (x => println("found: " + x))
    }
  }
}

```

## Spiders

A spider is a scraper which recursively loads a page and opens every link it finds. It will
keep scraping until all pages within the allowed domains are visited once.

The following snippet demonstrates a basic spider which crawls a website and provides
hooks to do something with the data

```scala
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
```

The spider can be extended by providing traits, if you want to scrape emails then
the EmailSpider trait can be used.

```scala
new Spider with EmailSpider {
  startUrls ::= "http://events.stanford.edu/"
  allowedDomains ::= "events.stanford.edu"

  onEmailFound ::= { email: String =>
    // Email found
  }

  onReceivedPage ::= { page: WebPage =>
    // Page received
  }

  onLinkFound ::= { link: Href =>
    println(s"Found link ${link.url} with name ${link.name}")
  }
}.start()
```

Multiple spiders can be mixed together

```scala
new Spider with EmailSpider with SitemapSpider {
  startUrls ::= "http://events.stanford.edu/"
  allowedDomains ::= "events.stanford.edu"
  sitemapUrls ::= "http://events.stanford.edu/sitemap.xml"

  onEmailFound ::= { email: String =>
    println("Found email: " + email)
  }

  onReceivedPage ::= { page: WebPage =>
    // Page received
  }

  onLinkFound ::= { link: Href =>
    println(s"Found link ${link.url} with name ${link.name}")
  }
}.start()
```

## Documentation

- [API](http://ci.razko.nl/job/WebsiteScraper/Documentation/index.html)
