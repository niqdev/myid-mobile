name := "myid-mobile-lib"
version := "1.0"
scalaVersion := "2.11.8"

lazy val V = new {
  val scalaScraper = "1.1.0"
  val gson = "2.8.0"

  val logback = "1.1.7"
  val scalaLogging = "3.5.0"
  val scalatest = "3.0.1"
}

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % V.scalaScraper,
  "com.google.code.gson" % "gson" % V.gson,

  "ch.qos.logback" % "logback-classic" % V.logback,
  "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging,

  "org.scalatest" %% "scalatest" % V.scalatest % Test
)
