package org.rovak.scraper.storage

import akka.actor._
import scala.slick.driver.MySQLDriver.simple._
import org.rovak.tables.{EmailsTable}

case class Email(email: String, klantId: Int)

/**
 * Database Storage
 */
class DatabaseStorage() extends Actor {

  val db = Database.forURL("jdbc:mysql://localhost/potmaat", driver = "com.mysql.jdbc.Driver", user = "root", password = "")

  def receive = {
    case Email(email, klantId) => {
      //db withSession EmailsTable.insert(klantId, email)
    }
  }
}