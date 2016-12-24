package com.github.niqdev.myidmobile

import com.typesafe.config.Config
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Success

class MyIdMobileScraperSpec extends FlatSpec with Matchers with MockFactory {

  trait Test {
    val UrlLogin = "urlLogin"
    val UrlRefresh = "urlRefresh"
    val UrlBalance = "urlBalance"
    val MobileNumber = "MyMobileNumber"
    val Password = "MyPassword"
    val AuthenticityToken = "myAuthenticityToken"
    val SessionId = "mySessionId"

    val browser = JsoupBrowser()
    val LoginGetDocument = browser.parseResource("/html/myid-mobile-ie-login-get.html")
    val LoginPostDocument = browser.parseResource("/html/myid-mobile-ie-login-post.html")
    val RefreshDocument = browser.parseString("REDIRECT")
    val BalanceDocument = browser.parseResource("/html/myid-mobile-ie-balance.html")

    val configStub = stub[Config]
    configStub.getString _ when "url.login" returns UrlLogin
    configStub.getString _ when "url.refresh" returns UrlRefresh
    configStub.getString _ when "url.balance" returns UrlBalance

    val jsoupBrowserStub = stub[JsoupBrowser]
    jsoupBrowserStub.get _ when UrlLogin returns LoginGetDocument
    jsoupBrowserStub.post _ when (UrlLogin, Map(
      "authenticity_token" -> AuthenticityToken,
      "login[mobile_number]" -> MobileNumber,
      "login[password]" -> Password,
      "utf8" -> "&#x2713;"
    )) returns LoginPostDocument
    jsoupBrowserStub.get _ when UrlRefresh returns RefreshDocument
    jsoupBrowserStub.get _ when UrlBalance returns BalanceDocument
    jsoupBrowserStub.cookies _ when UrlLogin returns Map("_idm_selfcare_session" -> SessionId)

    val myIdCredential = MyIdCredential(PhonePrefix.IE, MobileNumber, Password)
    val myIdMobile = new MyIdMobile(myIdCredential, configStub, jsoupBrowserStub)
  }

  it should "verify getLogin" in new Test {
    Await.ready(myIdMobile.getLogin andThen {
      case document => document.get.toHtml shouldBe LoginGetDocument.toHtml
    }, 1.second)
  }

  it should "verify postLogin" in new Test {
    Await.ready(myIdMobile.postLogin(AuthenticityToken) andThen {
      case document => document.get.toHtml shouldBe LoginPostDocument.toHtml
    }, 3.seconds)
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

  it should "verify getSessionId" in new Test {
    Await.ready(myIdMobile.getSessionId andThen {
      case sessionId => sessionId.get shouldBe SessionId
    }, 1.second)
  }

  it should "verify balance" in new Test {
    Await.ready(myIdMobile.balance andThen {
      case Success(planInfo) => {
        planInfo.expire shouldBe "30th November"
        planInfo.balance shouldBe "€9.93"

        val minutes = planInfo.minutes
        minutes.total shouldBe "300 Minutes"
        minutes.used shouldBe "0 Minutes"
        minutes.left shouldBe "300 Minutes"
        minutes.validUntil shouldBe "01/02/2016"

        val data = planInfo.data
        data.total shouldBe "30GB"
        data.used shouldBe "123 MB"
        data.left shouldBe "12.34 GB"
        data.validUntil shouldBe "01/02/2016"
      }
    }, 3.seconds)
  }

}
