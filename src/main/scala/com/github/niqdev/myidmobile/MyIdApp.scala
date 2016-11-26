package com.github.niqdev.myidmobile

import com.typesafe.scalalogging.Logger

object MyIdApp extends App {

  val logger = Logger("MyIdApp")

  val myIdCredential = MyIdCredential(
    mobileNumber = System.getenv("mobileNumber"),
    password = System.getenv("password"))

  val myIdMobile = MyIdMobile(myIdCredential)
  val planInfo = myIdMobile.balance()
  logger.debug(s"$planInfo")

}
