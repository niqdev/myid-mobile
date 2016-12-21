package com.github.niqdev.myidmobile

import com.typesafe.config.Config
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class MyIdMobileBrowserSpec extends FlatSpec with Matchers with MockFactory {

  it should "test" in {
    val configStub = stub[Config]
    configStub.getString _ when "url.login" returns "urlLogin"

    val jsoupBrowserStub = stub[JsoupBrowser]
    jsoupBrowserStub.get _ when  "urlLogin" returns JsoupBrowser().parseResource("/html/myid-mobile-ie-login-get.html")

    val myIdCredential = MyIdCredential(PhonePrefix.IE, "MyMobileNumber", "MyPassword")
    val myIdMobile = new MyIdMobile(myIdCredential, configStub, jsoupBrowserStub)

    val login = myIdMobile.getLogin andThen {
      case document => println(document)
    }

    Await.ready(login, Duration.Inf)
  }

}
