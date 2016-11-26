package com.github.niqdev.myidmobile

object PhonePrefix {

  sealed abstract class Prefix(value: String) {
    override def toString: String = s"$value"
  }

  case object IE extends Prefix("+353")
  case object UK extends Prefix("+44")

}
