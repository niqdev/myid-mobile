package com.github.niqdev.myidmobile

import com.typesafe.scalalogging.Logger

object MyIdApp extends App {

  val logger = Logger("MyIdApp")

  val myIdCredential = MyIdCredential(mobileNumber = "", password = "")
  val myIdSession = MyIdMobile.login(myIdCredential)

  /*
  val planInfo = MyIdMobile.accountInfo(myIdSession)
  logger.debug(planInfo.toString)

  val activities = MyIdMobile.activities(myIdSession)
  logger.debug(planInfo.toString)

  MyIdMobile.logout(myIdSession)
  */

}
