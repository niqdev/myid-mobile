import sbt._

object MultiBuild extends Build {
  lazy val app = Project(id = "app", base = file("app")) dependsOn "lib"
  lazy val lib = Project(id = "lib", base = file("lib"))
  lazy val root = Project(id = "root", base = file(".")) aggregate ("app", "lib")
}