package com.github.niqdev.myidmobile

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

object MyIdApp extends App {

  val browser = JsoupBrowser()
  //val docGetLogin = browser.get("https://my.idmobile.ie/login")
  val docHtmlLogin = browser.parseResource("/html/my-idmobile-ie-login.html")

  val authenticityToken: String = docHtmlLogin >> attr("content")("meta[name=csrf-token]")
  println(authenticityToken)

  // + cookie
  // browser.post()

  val form = docHtmlLogin >> extractor("#new_login", element, asIs[Element])
  val encoding = docHtmlLogin >> element("input[name=utf8]")
  println(encoding)
  val mobileNumber = docHtmlLogin >> element("#login_mobile_number")
  println(mobileNumber)
  val password = docHtmlLogin >> element("#login_password")
  println(password)

}
