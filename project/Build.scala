import sbt._
import sbt.Keys._
import scala.Some

object Build extends Build {

  val projectVersion = "0.4.2-SNAPSHOT"

  val defaultSettings = Project.defaultSettings ++ Seq(
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
      version := projectVersion,
      scalaVersion := "2.10.3")

  val publishSettings = Seq(

    /**
     * Publish settings
     */
    publishTo <<= version { v: String =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    organization := "nl.razko",

    publishMavenStyle := true,

    pomIncludeRepository := { _ => false },

    publishArtifact in Test := false,

    pomExtra := (
      <url>https://github.com/Rovak/ScalaWebscraper</url>
        <licenses>
          <license>
            <name>MIT</name>
            <url>http://opensource.org/licenses/MIT</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:Rovak/ScalaWebscraper.git</url>
          <connection>scm:git:git@github.com:Rovak/ScalaWebscraper.git</connection>
        </scm>
        <developers>
          <developer>
            <id>rovak</id>
            <name>Roy van Kaathoven</name>
            <url>http://rovak.pro</url>
          </developer>
        </developers>
      )
  )

  lazy val scraper = Project(
    id = "scraper",
    base = file("scraper"),
    settings = defaultSettings ++ publishSettings ++ Seq(
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
