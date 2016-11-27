package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.attr

/**
  * POST    https://my.idmobile.ie/login
  * GET     https://my.idmobile.ie/refresh-user (302)
  * GET     https://my.idmobile.ie
  * GET     https://my.idmobile.ie/my-activity
  * GET     https://my.idmobile.ie/logout (302)
  *
  * @author niqdev
  */
case class MyIdMobile(credential: MyIdCredential) {

  private val logger = Logger("MyIdMobile")
  private val config = ConfigFactory.load()
  private val browser = JsoupBrowser()

  private def login(): Unit = {

    def docGetLogin = browser.get(config.getString("url.login"))

    def docPostLogin(authenticityToken: String) = browser.post(config.getString("url.login"), Map(
      "authenticity_token" -> authenticityToken,
      "login[mobile_number]" -> credential.mobileNumber,
      "login[password]" -> credential.password,
      "utf8" -> "&#x2713;"
    ))

    def docRefresh = browser.get(config.getString("url.refresh"))

    val authenticityToken = docGetLogin >> attr("content")("meta[name=csrf-token]")
    sleep
    docPostLogin(authenticityToken)
    sleep
    docRefresh

    val sessionId = browser.cookies(config.getString("url.login"))
      .getOrElse("_idm_selfcare_session", "NO_SESSION")
    logger.debug(s"$credential|$sessionId")
  }

  def balance(): PlanInfo = {
    login()
    sleep

    val docGetBalance = browser.get(config.getString("url.balance"))

    val expire = docGetBalance >> text(".section-text:first-child > strong")
    val balance = docGetBalance >> text(".mobile-plan-balance")

    val minutes = Minutes(
      docGetBalance >> text(".minutes-widget > .widget-header"),
      docGetBalance >> text(".minutes > .widget-compeltion-bar-progress-text"),
      docGetBalance >> text(".minutes-used-section-content > .remaining"),
      docGetBalance >> text(".minutes-widget > .widget-subheader")
    )

    val data = Data(
      docGetBalance >> text(".data-widget > .widget-header"),
      docGetBalance >> text(".data > .widget-compeltion-bar-progress-text"),
      docGetBalance >> text(".data-used-section-content > .remaining"),
      docGetBalance >> text(".data-widget > .widget-subheader")
    )

    PlanInfo(expire, balance, minutes, data)
  }

  def sleep = {
    Thread.sleep(1000)
    logger.debug("sleep 1 second")
  }

}
