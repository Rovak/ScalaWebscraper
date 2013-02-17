Scala Webscraper
================

## Documentation

- [API](http://ci.razko.nl/job/WebsiteScraper/Documentation/index.html)

## Build

To build the project you need to have Scala 2.10.0 >= and sbt 1.12.0 >= installed. 
You can easily install it using this [install script](~/eclipse_scala/workspace/webscraper)

## Usage

Run `sbt run` in the root folder and choose the client or server application.

You have to start the client application first

## Examples

Scraping by Google search term

```scala
import org.rovak.scraper.query._

for (link <- Scrape google "php elephant" result) {
  println("Elephant found: " + link.name)
}
```

Scraping by url

```scala
import org.rovak.scraper.query._

val query = Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a"

for (link <- query.result) {
  // Use the resulting url's to scrape the found websites
  for (sublink <- Scrape from link.url select "a" result) {
    println("Found: " + sublink)
  }
}
```
