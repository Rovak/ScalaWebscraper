package org.rovak.tables

import scala.slick.driver.MySQLDriver.simple._

/**
 * Table: links
 */
object LinksTable extends Table[(Int, String)]("links") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def link = column[String]("link")
  def * = id ~ link
}