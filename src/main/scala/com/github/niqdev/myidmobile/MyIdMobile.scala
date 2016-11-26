package com.github.niqdev.myidmobile

import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.attr

/**
  * @author niqdev
  */
object MyIdMobile {

  val logger = Logger("MyIdMobile")

  // GET + POST
  def login(credential: MyIdCredential): MyIdSession = {
    val browser = JsoupBrowser()
    //val docGetLogin = browser.get("https://my.idmobile.ie/login")
    val docHtmlLogin = browser.parseResource("/html/my-idmobile-ie-login.html")

    val authenticityToken: String = docHtmlLogin >> attr("content")("meta[name=csrf-token]")
    logger.debug(authenticityToken)

    MyIdSession("sessionId")
  }

  // refresh + home
  def accountInfo(session: MyIdSession): PlanInfo = ???

  // refresh + activity
  def activities(session: MyIdSession) = ???

  def logout(session: MyIdSession) = ???

}
