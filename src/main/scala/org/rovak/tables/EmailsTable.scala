package org.rovak.tables

import scala.slick.driver.MySQLDriver.simple._

/**
 * Table: emails
 */
object EmailsTable extends Table[(Int, String)]("emails") {
  def id = column[Int]("id", O.PrimaryKey)
  def klantId = column[Int]("klant_id")
  def email = column[String]("email")
  def * = klantId ~ email
}