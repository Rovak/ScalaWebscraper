Scala Webscraper
================

## Documentation

- [API](http://ci.razko.nl/job/WebsiteScraper/Documentation/index.html)

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
