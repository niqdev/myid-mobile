package com.github.niqdev.myidmobile

import com.github.niqdev.myidmobile.PhonePrefix.Prefix

case class MyIdCredential(prefix: Prefix = PhonePrefix.IE, mobileNumber: String, password: String) {
  override def toString: String = s"$prefix|$mobileNumber|$password"
}

case class MyIdSession(sessionId: String) {
  // HEADER Cookie: _idm_selfcare_session=XXX
  override def toString: String = s"$sessionId"
}

case class LastTopUp(date: String, amount: String)

case class Expire(date: String)

case class Balance(amount: String)

case class Minutes(total: String, used: String, left: String, validUntil: String)

case class Data(total: String, used: String, left: String, validUntil: String)

case class PlanInfo(lastTopUp: LastTopUp, expire: Expire, balance: Balance, minutes: Minutes, data: Data) {
  override def toString: String = s"TODO"
}
