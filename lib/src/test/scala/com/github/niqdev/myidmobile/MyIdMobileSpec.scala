package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory

/**
  * @author niqdev
  */
class MyIdMobileSpec extends UnitSpec {

  val config = ConfigFactory.load()

  "config" should "contains urls" in {
    config.getString("url.base-path") should be ("https://my.idmobile.ie")
    config.getString("url.login") should be ("https://my.idmobile.ie/login")
    config.getString("url.logout") should be ("https://my.idmobile.ie/logout")
    config.getString("url.refresh") should be ("https://my.idmobile.ie/refresh-user")
    config.getString("url.balance") should be ("https://my.idmobile.ie")
    config.getString("url.history") should be ("https://my.idmobile.ie/my-activity")
  }

  "MyIdCredential" should "have default prefix" in {
    val credential = MyIdCredential(mobileNumber = Some(""), password = Some(""))
    credential.prefix should be (PhonePrefix.IE)
  }

  it should "throw AssertionError if credentials are missing" in {
//    an [IllegalArgumentException] should be thrownBy {
//      MyIdMobile(Some(""), )
//    }
  }

}
