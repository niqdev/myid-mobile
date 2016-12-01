package com.github.niqdev.myidmobile

import com.github.niqdev.myidmobile.PhonePrefix.Prefix
import com.google.gson.{Gson, GsonBuilder}

object PhonePrefix {
  sealed abstract class Prefix(value: String) {
    override def toString = s"$value"
  }
  case object IE extends Prefix("+353")
  case object UK extends Prefix("+44")
}

abstract class JsonConverter {
  final def toJson = new Gson().toJson(this)
  final def toPrettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(this)
}

case class MyIdCredential(prefix: Prefix = PhonePrefix.IE, mobileNumber: String, password: String) extends JsonConverter

case class MobilePlanWidget(total: String, used: String, left: String, validUntil: String) extends JsonConverter

case class PlanInfo(
    expire: String,
    balance: String,
    minutes: MobilePlanWidget,
    data: MobilePlanWidget
  )
  extends JsonConverter
