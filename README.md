# myid-mobile

MyiD Mobile [IE](http://www.idmobile.ie) and [UK](https://www.idmobile.co.uk)

[Flow diagram](https://sketchboard.me/RAdOBg32ynxh)

Questions:
* lib use rxscala/future/?? no sleep - must be easy to test
* lib how model data? use companion object or case class or ??
* multi-project: declare same version and scalaVersion in single point
* declare common dependencies (log/test) in a single shared point
* move project/plugins.sbt scalaJS in app only (exlude lib)
* how to sbt run main in multi project (command line): NyIdApp and TutorialApp
