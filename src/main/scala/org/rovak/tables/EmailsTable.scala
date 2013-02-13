package org.rovak.tables

import scala.slick.driver.MySQLDriver.simple._

object EmailsTable extends Table[(Int, String)]("emails") {
  def id = column[Int]("id", O.PrimaryKey) // This is the primary key column
  def klantId = column[Int]("klant_id")
  def email = column[String]("email")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = klantId ~ email
}