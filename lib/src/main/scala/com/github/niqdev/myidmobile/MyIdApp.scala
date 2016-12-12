package com.github.niqdev.myidmobile

import cats.data.Validated.{Invalid, Valid}
import com.github.niqdev.myidmobile.JsonConverter._
import com.typesafe.scalalogging.Logger

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object MyIdApp extends App {

  private val logger = Logger("MyIdApp")
  private val env = sys.env

  private def balance(myIdMobile: MyIdMobile) = myIdMobile.balance andThen {
    case Success(planInfo) => logger.debug(s"${planInfo.toPrettyJson}")
    case Failure(throwable) => logger.error(s"$throwable")
  }

  MyIdMobile(
    mobileNumber = env.get("mobileNumber"),
    password = env.get("password")
  ) match {
    case Valid(myIdMobile) => Await.ready(balance(myIdMobile), Duration.Inf)
    case Invalid(errors) => errors.toList.foreach(logger.error(_))
  }

}
