package com.github.niqdev.myidmobile

import cats.data.Validated.{Invalid, Valid}
import com.typesafe.config.ConfigFactory

/**
  * @author niqdev
  */
class MyIdMobileSpec extends UnitSpec {

  "config" should "contains urls" in {
    val config = ConfigFactory.load()

    config.getString("url.base-path") shouldBe "https://my.idmobile.ie"
    config.getString("url.login") shouldBe "https://my.idmobile.ie/login"
    config.getString("url.logout") shouldBe "https://my.idmobile.ie/logout"
    config.getString("url.refresh") shouldBe "https://my.idmobile.ie/refresh-user"
    config.getString("url.balance") shouldBe "https://my.idmobile.ie"
    config.getString("url.history") shouldBe "https://my.idmobile.ie/my-activity"
  }

  it should "validates all missing field" in {
    MyIdMobile(mobileNumber = Some(""), password = Some("")) match {
      case Valid(_) => fail()
      case Invalid(errors) => {
        val errorList = errors.toList

        errorList.length shouldBe 2
        errorList.head shouldBe "missing mobile number"
        errorList(1) shouldBe "missing password"
      }
    }
  }

  it should "validates missing mobile number" in {
    MyIdMobile(mobileNumber = Some(" "), password = Some("12345")) match {
      case Valid(_) => fail()
      case Invalid(errors) => {
        val errorList = errors.toList

        errorList.length shouldBe 1
        errorList.head shouldBe "missing mobile number"
      }
    }
  }

  it should "validates missing password" in {
    MyIdMobile(mobileNumber = Some("12345"), password = Some(" ")) match {
      case Valid(_) => fail()
      case Invalid(errors) => {
        val errorList = errors.toList

        errorList.length shouldBe 1
        errorList.head shouldBe "missing password"
      }
    }
  }

  it should "succeed validating all fields" in {
    MyIdMobile(mobileNumber = Some("MyMobileNumber"), password = Some("MyPassword")) match {
      case Invalid(_) => fail()
      case Valid(myIdMobile) => {

        myIdMobile.credential.prefix shouldBe PhonePrefix.IE
        myIdMobile.credential.mobileNumber shouldBe "MyMobileNumber"
        myIdMobile.credential.password shouldBe "MyPassword"
      }
    }
  }

}
