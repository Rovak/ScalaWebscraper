package org.rovak.scraper.collectors

import org.rovak.scraper.models.Result

abstract class Collector {
  def collect(result: Result)
}

