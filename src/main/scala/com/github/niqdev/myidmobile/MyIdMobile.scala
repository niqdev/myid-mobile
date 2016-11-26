package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.attr

/**
  * @author niqdev
  */
object MyIdMobile {

  private[this] val config = ConfigFactory.load()
  private[this] val logger = Logger("MyIdMobile")
  private[this] val browser = JsoupBrowser()


  // GET + POST
  def login(credential: MyIdCredential): MyIdSession = {

    def docGetLogin = browser.get(config.getString("url.login"))
    def docPostLogin(authenticityToken: String) = browser.post(config.getString("url.login"), Map(
      "authenticity_token" -> authenticityToken,
      "login[mobile_number]" -> credential.mobileNumber,
      "login[password]" -> credential.password,
      "utf8" -> "&#x2713;"
    ))

    val authenticityToken = docGetLogin >> attr("content")("meta[name=csrf-token]")
    val bho = docPostLogin(authenticityToken)
    logger.debug(browser.cookies("https://my.idmobile.ie/login").toString)
    logger.debug(authenticityToken.toString)
    println(bho)

    MyIdSession("sessionId")
  }

  // refresh + home
  def accountInfo(session: MyIdSession): PlanInfo = ???

  // refresh + activity
  def activities(session: MyIdSession) = ???

  def logout(session: MyIdSession) = ???

}
