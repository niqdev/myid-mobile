package com.github.niqdev.myidmobile

import com.github.niqdev.myidmobile.PhonePrefix.Prefix

object PhonePrefix {

  sealed abstract class Prefix(value: String) {
    override def toString = s"$value"
  }

  case object IE extends Prefix("+353")
  case object UK extends Prefix("+44")

}

case class MyIdCredential(prefix: Prefix = PhonePrefix.IE, mobileNumber: String, password: String) {
  final def prettyPrint = s"$prefix|$mobileNumber|$password"
}

case class MobilePlanWidget(total: String, used: String, left: String, validUntil: String) {
  final def prettyPrint = s"$total|$used|$left|$validUntil"
}

case class PlanInfo(
    expire: String,
    balance: String,
    minutes: MobilePlanWidget,
    data: MobilePlanWidget
  ) {
  override def toString = s"$expire|$balance|$minutes|$data"
}
