package com.github.niqdev.myidmobile

trait Request {

  def login(mobileNumber: String, password: String)

  def refreshUser()

  def home()

  def activity()

  def logout()

}

case class LastTopUp(date: String, amount: String)

case class Expire(date: String)

case class Balance(amount: String)

case class Minutes(total: String, used: String, left: String, validUntil: String)

case class Data(total: String, used: String, left: String, validUntil: String)

case class PlanInfo(lastTopUp: LastTopUp, expire: Expire, balance: Balance, minutes: Minutes, data: Data)

/**
  * @author niqdev
  */
class MyIdMobile(val mobileNumber: String, val password: String) {

}
