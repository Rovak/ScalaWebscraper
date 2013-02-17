package org.rovak.scraper

import akka.actor._
import com.typesafe.config._

object ClientApp extends App {

  val system = ActorSystem("Remote", ConfigFactory.load())
  val actor = system.actorFor("akka://Default@127.0.0.1:2552/user/query");
  
  println(actor)
}