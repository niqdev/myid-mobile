package com.github.niqdev.myidmobile

import com.github.niqdev.myidmobile.JsonConverter._
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

  val balance = myIdMobile.balance
  balance onComplete {
    case Success(planInfo) => logger.debug(s"${planInfo.toPrettyJson}")
    case Failure(throwable) => logger.error(s"$throwable")
  }

  Await.ready(balance, Duration.Inf)
}
