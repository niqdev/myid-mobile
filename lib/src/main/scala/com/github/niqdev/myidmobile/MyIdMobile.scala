package com.github.niqdev.myidmobile

import com.github.niqdev.myidmobile.JsonConverter._
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

  def apply(credential: MyIdCredential): MyIdMobile = {
    assert(isNotBlank(credential.mobileNumber), "missing mobileNumber")
    assert(isNotBlank(credential.password), "missing password")

    new MyIdMobile(credential, ConfigFactory.load())
  }

  private[this] def isNotBlank(o: Option[String]): Boolean = o match {
    case Some(s) => s.trim.nonEmpty
    case None => false
  }
}

// spec2 ++scalaTest scalaCheck
class MyIdMobile(credential: MyIdCredential, config: Config) {

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
      "login[mobile_number]" -> credential.mobileNumber.get,
      "login[password]" -> credential.password.get,
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
