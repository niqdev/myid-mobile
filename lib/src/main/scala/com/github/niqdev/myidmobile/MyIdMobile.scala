package com.github.niqdev.myidmobile

import cats.Apply
import cats.data.ValidatedNel
import cats.implicits._
import com.github.niqdev.myidmobile.JsonConverter._
import com.github.niqdev.myidmobile.PhonePrefix.Prefix
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.attr

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

/**
  * @author niqdev
  */
object MyIdMobile {

  type Result[A] = ValidatedNel[String, A]

  def apply(prefix: Prefix = PhonePrefix.IE, mobileNumber: Option[String], password: Option[String]): Result[MyIdMobile] = {
    val pwd = password.flatMap(nonBlank).toValidNel("missing password")
    val mbn = mobileNumber.flatMap(nonBlank).toValidNel("missing mobile number")

    Apply[Result].map2(mbn, pwd) {
      case (mobile, pass) => new MyIdMobile(MyIdCredential(prefix, mobile, pass), ConfigFactory.load())
    }
  }

  private[this] def nonBlank(s: String): Option[String] = if (s.trim.isEmpty) None else Some(s)
}

class MyIdMobile(credential: MyIdCredential, config: => Config) {

  private val logger = Logger("MyIdMobile")
  private val browser = JsoupBrowser()

  private def getLogin: Future[Document] = Future {
    logger.debug("GET login")

    browser.get(config.getString("url.login"))
  }

  private def postLogin(authenticityToken: String): Future[Document] = Future {
    logger.debug("POST login")
    sleep for_ 2.second

    browser.post(config.getString("url.login"), Map(
      "authenticity_token" -> authenticityToken,
      "login[mobile_number]" -> credential.mobileNumber,
      "login[password]" -> credential.password,
      "utf8" -> "&#x2713;"
    ))
  }

  private def getRefresh: Future[Document] = Future {
    logger.debug("GET refresh")

    browser.get(config.getString("url.refresh"))
  }

  private def getBalance: Future[Document] = Future {
    logger.debug("GET balance")

    browser.get(config.getString("url.balance"))
  }

  private def getSessionId: Future[String] = Future.successful {
    browser.cookies(config.getString("url.login"))
      .getOrElse("_idm_selfcare_session", "NO_SESSION")
  }

  private def extractBalance(document: Document): PlanInfo = {
    val expire = document >> text(".section-text:first-child > strong")
    val balance = document >> text(".mobile-plan-balance")

    def parseLeft(text: String): String = text replace(" left", "")

    def parseValidUntil(text: String): String = text replace("Valid until: ", "")

    val minutes = MobilePlanWidget(
      total = document >> text(".minutes-widget > .widget-header"),
      used = document >> text(".minutes > .widget-compeltion-bar-progress-text"),
      left = parseLeft(document >> text(".minutes-used-section-content > .remaining")),
      validUntil = parseValidUntil(document >> text(".minutes-widget > .widget-subheader"))
    )

    val data = MobilePlanWidget(
      total = document >> text(".data-widget > .widget-header"),
      used = document >> text(".data > .widget-compeltion-bar-progress-text"),
      left = parseLeft(document >> text(".data-used-section-content > .remaining")),
      validUntil = parseValidUntil(document >> text(".data-widget > .widget-subheader"))
    )

    PlanInfo(expire, balance, minutes, data)
  }

  def balance: Future[PlanInfo] = for {
    authenticityToken <- getLogin map (_ >> attr("content")("meta[name=csrf-token]"))
    sessionId <- getSessionId
    _ <- postLogin(authenticityToken)
    _ <- getRefresh
    documentBalance <- getBalance
  } yield {
    logger.debug(s"$sessionId - ${credential.toJson}")
    extractBalance(documentBalance)
  }

}
