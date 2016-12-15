package com.github.niqdev.myidmobile

import com.typesafe.config.ConfigFactory

/**
  * @author niqdev
  */
class MyIdMobileSpec extends UnitSpec {

  val config = ConfigFactory.load()

  "config" should "contains urls" in {
    config.getString("url.base-path") shouldBe "https://my.idmobile.ie"
    config.getString("url.login") shouldBe "https://my.idmobile.ie/login"
    config.getString("url.logout") shouldBe "https://my.idmobile.ie/logout"
    config.getString("url.refresh") shouldBe "https://my.idmobile.ie/refresh-user"
    config.getString("url.balance") shouldBe "https://my.idmobile.ie"
    config.getString("url.history") shouldBe "https://my.idmobile.ie/my-activity"
  }

}
