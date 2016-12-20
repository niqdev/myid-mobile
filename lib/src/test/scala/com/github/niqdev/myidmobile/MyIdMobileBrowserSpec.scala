package com.github.niqdev.myidmobile

import com.typesafe.config.Config
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class MyIdMobileBrowserSpec extends FlatSpec with Matchers with MockFactory {

  "Login" should "invoke GET with config url" in {
    val configStub = stub[Config]
    configStub.getString _ when "url.login" returns "urlLogin"

    val jsoupBrowserMock = mock[JsoupBrowser]
    jsoupBrowserMock.get _ expects "urlLogin"
    //jsoupBrowserMock.get _ when "urlLogin" returns JsoupDocument(new org.jsoup.nodes.Document(""))

    val myIdCredential = MyIdCredential(PhonePrefix.IE, "MyMobileNumber", "MyPassword")
    val myIdMobile = new MyIdMobile(myIdCredential, configStub, jsoupBrowserMock)
    myIdMobile.getLogin map { document => assert(document == "")}
  }

}
