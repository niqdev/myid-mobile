package com.github.niqdev.myidmobile

import com.typesafe.scalalogging.Logger

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object MyIdApp extends App {

  val logger = Logger("MyIdApp")

  val myIdCredential = MyIdCredential(
    mobileNumber = System.getenv("mobileNumber"),
    password = System.getenv("password"))

  val myIdMobile = MyIdMobile(myIdCredential)

  val balance = myIdMobile.balance andThen  {
    case Success(planInfo) => logger.debug(s"$planInfo")
    case Failure(throwable) => logger.error(s"$throwable")
  }

  Await.ready(balance, Duration.Inf)

}
