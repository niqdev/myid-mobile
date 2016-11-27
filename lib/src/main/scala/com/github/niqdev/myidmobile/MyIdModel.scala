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
  override def toString = s"$prefix|$mobileNumber|$password"
}

trait MobilePlanWidget {
  def left(text: String): String = text replace(" left", "")
  def validUntil(text: String): String = text replace("Valid until: ", "")
}

case class Minutes(total: String, used: String, leftStr: String, validUntilStr: String) extends MobilePlanWidget {
  override def toString = s"$total|$used|${left(leftStr)}|${validUntil(validUntilStr)}"
}

case class Data(total: String, used: String, leftStr: String, validUntilStr: String) extends MobilePlanWidget {
  override def toString = s"$total|$used|${left(leftStr)}|${validUntil(validUntilStr)}"
}

case class PlanInfo(
    expire: String,
    balance: String,
    minutes: Minutes,
    data: Data
  ) {
  override def toString = s"$expire|$balance|$minutes|$data"
}
