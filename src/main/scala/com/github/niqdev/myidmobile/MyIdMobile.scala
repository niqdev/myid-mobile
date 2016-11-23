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

case class PlanInfo(lastTopUp: LastTopUp, expire: Expire, balance: Balance, minutes: Minutes, data: Data) {

}

/**
  * @author niqdev
  */
class MyIdMobile() {

}

// TODO enum prefix IE/UK
case class MyIdCredential(prefix: String = "+353", mobileNumber: String, password: String) {

  override def toString: String = s"$prefix|$mobileNumber|$password"
}

object MyIdMobile {

  def accountInfo(credential: MyIdCredential): PlanInfo = ???

  def activities(credential: MyIdCredential) = ???

}
