enablePlugins(ScalaJSPlugin)

name := "myid-mobile-app"
version := "1.0"
scalaVersion := "2.11.8"

lazy val V = new {}

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1"
)
