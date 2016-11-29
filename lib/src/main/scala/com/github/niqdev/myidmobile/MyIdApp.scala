package com.github.niqdev.myidmobile

import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object MyIdApp extends App {

  val logger = Logger("MyIdApp")

  val myIdCredential = MyIdCredential(
    mobileNumber = System.getenv("mobileNumber"),
    password = System.getenv("password"))

  val myIdMobile = MyIdMobile(myIdCredential)

  myIdMobile.balance onComplete {
    case Success(planInfo) => logger.debug(s"$planInfo")
    case Failure(throwable) => logger.error(s"$throwable")
  }

}
