package org.rovak.scraper.collectors

import java.io._
import org.rovak.scraper.models.Result

/**
 * Collects results and writes them to a local file
 */
class FileWriterCollector extends Collector {

  val filename = "results.txt"
  val writer: Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))

  def collect(result: Result) = {
    writer.write(result.toCSV + "\n")
    writer.flush()
  }

}
