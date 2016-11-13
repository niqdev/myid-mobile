package com.github.niqdev.myidmobile

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

object MyIdApp extends App {

  val browser = JsoupBrowser()
  val docGetLogin = browser.get("https://my.idmobile.ie/login")
  val docHtmlLogin = browser.parseResource("/html/my-idmobile-ie-login.html")

  println(docHtmlLogin)

}
