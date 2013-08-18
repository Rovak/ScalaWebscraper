Scala Webscraper
================

## Documentation

- [API](http://ci.razko.nl/job/WebsiteScraper/Documentation/index.html)

## Build

To build the project you need to have Scala 2.10.0 >= and sbt 1.12.0 >= installed. 
You can easily install it using this [install script](https://gist.github.com/Rovak/4967148)

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

Scrape google "php elephant" map { link =>
  println("Elephant found: " + link.name)
}
```

Scraping by url

```scala
import org.rovak.scraper.query._

val query = Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a"

Scrape from "http://www.google.nl/search?q=scala" select "#res li.g h3.r a" map { link =>
  Scrape from link.url select "a" map { sublink =>
    println("Found: " + sublink)
  }
}
```
