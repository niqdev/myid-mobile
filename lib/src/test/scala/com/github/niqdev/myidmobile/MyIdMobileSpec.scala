package com.github.niqdev.myidmobile

import cats.data.Validated.{Invalid, Valid}
import com.typesafe.config.ConfigFactory
import org.scalatest.Assertions._

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

  trait TestMyIdMobile {
    val myIdMobile = MyIdMobile(mobileNumber = Some(""), password = Some(""))
  }

  "it" should "validates all missing field" in {
    MyIdMobile(mobileNumber = Some(""), password = Some("")) match {
      case Valid(_) => fail()
      case Invalid(errors) => {
        val errorList = errors.toList
        assert(errorList.length === 2)
        assert(errorList.head === "missing mobile number")
        assert(errorList(1) === "missing password")
      }
    }
  }

  "it" should "validates missing mobile number" in {
    MyIdMobile(mobileNumber = Some(" "), password = Some("12345")) match {
      case Valid(_) => fail()
      case Invalid(errors) => {
        val errorList = errors.toList
        assert(errorList.length === 1)
        assert(errorList.head === "missing mobile number")
      }
    }
  }

  "it" should "validates missing password" in {
    MyIdMobile(mobileNumber = Some("12345"), password = Some(" ")) match {
      case Valid(_) => fail()
      case Invalid(errors) => {
        val errorList = errors.toList
        assert(errorList.length === 1)
        assert(errorList.head === "missing password")
      }
    }
  }

}
