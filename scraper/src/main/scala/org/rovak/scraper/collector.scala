package org.rovak.scraper

import java.io._
import org.rovak.scraper.models.Result

class Collector {

  val writer: Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("results.txt"), "utf-8"));

  def collect(result: Result) = {
    writer.write(result.toCSV + "\n")
    writer.flush()
  }

}
