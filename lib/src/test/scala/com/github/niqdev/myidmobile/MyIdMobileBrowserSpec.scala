package com.github.niqdev.myidmobile

import com.typesafe.config.Config
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class MyIdMobileBrowserSpec extends FlatSpec with Matchers with MockFactory {

  trait Test {
    val UrlLogin = "urlLogin"
    val UrlRefresh = "urlRefresh"
    val UrlBalance = "urlBalance"

    val browser = JsoupBrowser()
    val LoginDocument = browser.parseResource("/html/myid-mobile-ie-login-get.html")
    val RefreshDocument = browser.parseString("REDIRECT")
    val BalanceDocument = browser.parseResource("/html/myid-mobile-ie-balance.html")

    val configStub = stub[Config]
    configStub.getString _ when "url.login" returns UrlLogin
    configStub.getString _ when "url.refresh" returns UrlRefresh
    configStub.getString _ when "url.balance" returns UrlBalance

    val jsoupBrowserStub = stub[JsoupBrowser]
    jsoupBrowserStub.get _ when UrlLogin returns LoginDocument
    jsoupBrowserStub.get _ when UrlRefresh returns RefreshDocument
    jsoupBrowserStub.get _ when UrlBalance returns BalanceDocument

    val MobileNumber = "MyMobileNumber"
    val Password = "MyPassword"
    val myIdCredential = MyIdCredential(PhonePrefix.IE, MobileNumber, Password)
    val myIdMobile = new MyIdMobile(myIdCredential, configStub, jsoupBrowserStub)
  }

  it should "verify getLogin" in new Test {
    Await.ready(myIdMobile.getLogin andThen {
      case document => document.get.toHtml shouldBe LoginDocument.toHtml
    }, 1.second)
  }

  it should "verify getRefresh" in new Test {
    Await.ready(myIdMobile.getRefresh andThen {
      case document => document.get.toHtml shouldBe RefreshDocument.toHtml
    }, 1.second)
  }

  it should "verify getBalance" in new Test {
    Await.ready(myIdMobile.getBalance andThen {
      case document => document.get.toHtml shouldBe BalanceDocument.toHtml
    }, 1.second)
  }

}
