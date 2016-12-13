name := "myid-mobile"
scalaVersion in ThisBuild := "2.11.8"

lazy val V = new {
  val scalaScraper = "1.1.0"
  val gson = "2.8.0"
  val cats = "0.8.1"
  val scalajs = "0.9.1"

  val logback = "1.1.7"
  val scalaLogging = "3.5.0"
  val scalatest = "3.0.1"
}

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % V.logback,
    "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging,

    "org.scalatest" %% "scalatest" % V.scalatest % Test
  )
)

lazy val libSettings = commonSettings ++ Seq(
  name := "myid-mobile-lib",
  libraryDependencies ++= Seq(
    "net.ruippeixotog" %% "scala-scraper" % V.scalaScraper,
    "com.google.code.gson" % "gson" % V.gson,
    "org.typelevel" %% "cats-core" % V.cats
  )
)

lazy val appSettings = commonSettings ++ Seq(
  name := "myid-mobile-app",
  libraryDependencies += "org.scala-js" %%% "scalajs-dom" % V.scalajs,
  mainClass in run := Some("info.myidmobile.TutorialApp")
)

lazy val lib = project.in(file("lib"))
  .settings(libSettings)

lazy val app = project.in(file("app"))
  .settings(appSettings)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(lib)

lazy val root = project.in(file("."))
  .aggregate(lib, app)
