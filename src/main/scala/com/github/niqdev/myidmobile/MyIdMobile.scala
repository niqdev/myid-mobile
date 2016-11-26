package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
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

  // refresh + home
  def balance(): Document = {
    login()
    sleep
    browser.get(config.getString("url.balance"))
  }

  // refresh + activity
  def history() = ???

  def sleep = {
    Thread.sleep(1000)
    logger.debug("sleep 1 second")
  }

}
