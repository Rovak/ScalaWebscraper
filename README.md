Scala Webscraper
================

## Documentation

- [API](http://ci.razko.nl/job/WebsiteScraper/Documentation/index.html)

## Build

To build the project you need to have Scala 2.10.0 >= and sbt 1.12.0 >= installed. 
You can easily install it using this [install script](~/eclipse_scala/workspace/webscraper)

## Usage

To try the example navigate to the project folder and run

```
sbt
project scraper-demo
run
```

This will start the example scraper which fetches some results from Google

## Examples

Scraping by Google search term

```scala
import org.rovak.scraper.query._

for (link <- Scrape google "php elephant") {
  println("Elephant found: " + link.name)
}
```

Scraping by url

```scala
import org.rovak.scraper.query._

val query = Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a"

for (link <- query) {
  // Use the resulting url's to scrape the found websites
  for (sublink <- Scrape from link.url select "a") {
    println("Found: " + sublink)
  }
}
```
