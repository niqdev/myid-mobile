# myid-mobile

MyiD Mobile [IE](http://www.idmobile.ie) and [UK](https://www.idmobile.co.uk)

[Flow diagram](https://sketchboard.me/RAdOBg32ynxh)

> Work in progress

```
sbt fastOptJS
```

TODO
* multi-project: declare same version and scalaVersion in single point
* declare common dependencies (log/test) in a single shared point
* move project/plugins.sbt scalaJS in app only (exclude lib)
* sbt run main in multi project (command line): MyIdApp and TutorialApp (sbt ~run)
