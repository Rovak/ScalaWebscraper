import AssemblyKeys._ // put this at the top of the file

assemblySettings

jarName in assembly := "scala_webscraper.jar"

mainClass in assembly := Some("org.rovak.scraper.MainApp")

name := "Scala Webscraper"

version := "0.1.0"

scalaVersion := "2.10.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "sonatype-public" at "https://oss.sonatype.org/content/groups/public"

libraryDependencies ++= List(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0",
  "com.typesafe.akka" %% "akka-remote" % "2.1.0",
  "com.typesafe.akka" %% "akka-kernel" % "2.1.0",
  "com.typesafe.slick" %% "slick" % "1.0.0",
  "com.typesafe" % "config" % "1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.3.166",
  "mysql" % "mysql-connector-java" % "5.1.13",
  "org.jsoup" % "jsoup" % "1.7.2"
)
