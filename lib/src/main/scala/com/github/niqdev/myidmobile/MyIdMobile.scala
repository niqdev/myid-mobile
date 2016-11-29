package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.attr

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
  * @author niqdev
  */
case class MyIdMobile(credential: MyIdCredential) {

  private val logger = Logger("MyIdMobile")
  private val config = ConfigFactory.load()
  private val browser = JsoupBrowser()

  private def getLogin: Future[Document] = Future {
    browser.get(config.getString("url.login"))
  }

  private def postLogin(authenticityToken: String): Future[Document] = Future {
    browser.post(config.getString("url.login"), Map(
      "authenticity_token" -> authenticityToken,
      "login[mobile_number]" -> credential.mobileNumber,
      "login[password]" -> credential.password,
      "utf8" -> "&#x2713;"
    ))
  }

  private def getRefresh: Future[Document] = Future {
    browser.get(config.getString("url.refresh"))
  }

  private def getBalance: Future[Document] = Future {
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
    logger.debug(s"$credential|$sessionId")
    extractBalance(documentBalance)
  }

}
