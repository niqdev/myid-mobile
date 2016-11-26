package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.attr

/**
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
    //login()
    //sleep
    //def docGetBalance = browser.get(config.getString("url.balance"))

    val docHtmlBalance = browser.parseResource("/html/my-idmobile-ie-balance.html")
    val expire = docHtmlBalance >> text(".section-text:first-child > strong")
    val balance = docHtmlBalance >> text(".mobile-plan-balance")

    val minutes = Minutes(
      docHtmlBalance >> text(".minutes-widget > .widget-header"),
      docHtmlBalance >> text(".minutes > .widget-compeltion-bar-progress-text"),
      docHtmlBalance >> text(".minutes-used-section-content > .remaining"),
      docHtmlBalance >> text(".minutes-widget > .widget-subheader")
    )

    val data = Data(
      docHtmlBalance >> text(".data-widget > .widget-header"),
      docHtmlBalance >> text(".data > .widget-compeltion-bar-progress-text"),
      docHtmlBalance >> text(".data-used-section-content > .remaining"),
      docHtmlBalance >> text(".data-widget > .widget-subheader")
    )

    PlanInfo(expire, balance, minutes, data)
  }

  // refresh + activity
  def history() = ???

  def sleep = {
    Thread.sleep(1000)
    logger.debug("sleep 1 second")
  }

}
