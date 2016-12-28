# myid-mobile

[![Build Status](https://travis-ci.org/niqdev/myid-mobile.svg?branch=master)](https://travis-ci.org/niqdev/myid-mobile)
[![Stories in Ready](https://badge.waffle.io/niqdev/myid-mobile.svg?label=ready&title=Ready)](http://waffle.io/niqdev/myid-mobile)

MyiD Mobile [IE](http://www.idmobile.ie) and [UK](https://www.idmobile.co.uk)

[Flow diagram](https://sketchboard.me/RAdOBg32ynxh)

> Work in progress

Retrieve latest balance from command line
```
sbt -DmobileNumber=XXX -Dpassword=XXX lib/run
```

Open the url `http://localhost:12345/app/target/scala-2.11/classes/index-tutorial.html`
```
sbt ~fastOptJS
```
