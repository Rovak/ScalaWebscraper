import sbt._
import sbt.Keys._

object SbtProject extends Build {

  val projectVersion = "0.3-SNAPSHOT"

  val defaultSettings = Project.defaultSettings ++ Seq(
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
      version := projectVersion,
      scalaVersion := "2.10.2")

  lazy val scraper = Project(
    id = "scraper",
    base = file("scraper"),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Seq(
          Dependencies.Etc.jsoup,
          Dependencies.Akka.actor)
    )
  )

  lazy val scraperServer = Project(
    id = "scraper-server",
    base = file("scraper-server"),
    dependencies = Seq(scraper),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Dependencies.akka
    )
  ).aggregate(scraper)

  lazy val scraperDemo = Project(
    id = "scraper-demo",
    base = file("scraper-demo"),
    dependencies = Seq(scraper),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Dependencies.database ++ Seq(Dependencies.Typesafe.config)
    )
  ).aggregate(scraper)

  object Dependencies {
    object Akka {
      val actor       = "com.typesafe.akka"     %% "akka-actor"       % "2.2.1"
      val remote      = "com.typesafe.akka"     %% "akka-remote"      % "2.2.1"
      val kernel      = "com.typesafe.akka"     %% "akka-kernel"      % "2.2.1"
    }

    object Typesafe {
      val config      = "com.typesafe"          % "config"            % "1.0"
    }

    object Db {
      val slick       = "com.typesafe.slick"    %% "slick"            % "1.0.0"
      val mysql       = "mysql"                 % "mysql-connector-java" % "5.1.13"
    }

    object Etc {
      val jsoup     = "org.jsoup"               % "jsoup"             % "1.7.2"
      val slf4j     = "org.slf4j"               % "slf4j-nop"         % "1.6.4"
    }

    val database = Seq(Db.slick, Db.mysql)
    val akka = Seq(Akka.actor, Akka.remote, Akka.kernel)
  }
}
