package org.rovak.scraper

import akka.actor._
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import org.rovak.tables._

case class Email(email: String, klantId: Int)

/**
 * Stores scrape information
 */
class DatabaseStorage() extends Actor {

  val db = Database.forURL("jdbc:mysql://localhost/potmaat", driver = "com.mysql.jdbc.Driver", user = "root", password = "")

  def receive = {
    case Email(email, klantId) => {
      db withSession EmailsTable.insert(klantId, email)
    }
  }
}