package com.github.niqdev.myidmobile

import com.github.niqdev.myidmobile.PhonePrefix.Prefix

object PhonePrefix {
  sealed abstract class Prefix(value: String) {
    override def toString = s"$value"
  }
  case object IE extends Prefix("+353")
  case object UK extends Prefix("+44")
}

case class MyIdCredential(prefix: Prefix, mobileNumber: String, password: String)

case class MobilePlanWidget(total: String, used: String, left: String, validUntil: String)

case class PlanInfo(
    expire: String,
    balance: String,
    minutes: MobilePlanWidget,
    data: MobilePlanWidget
  )
