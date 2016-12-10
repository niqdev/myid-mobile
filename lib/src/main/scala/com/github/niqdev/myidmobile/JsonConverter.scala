package com.github.niqdev.myidmobile

import com.google.gson.{Gson, GsonBuilder}

// TODO circe
trait JsonConverter[-T] {
  def toJson(t: T): String
  def toPrettyJson(t: T): String
}

object JsonConverter {
  implicit val anyRefJsonConverter = new JsonConverter[AnyRef] {
    override def toJson(t: AnyRef): String = new Gson().toJson(t)
    override def toPrettyJson(t: AnyRef): String = new GsonBuilder().setPrettyPrinting().create().toJson(t)
  }
  implicit val intJsonConverter = new JsonConverter[Int] {
    override def toJson(t: Int) = s"$t"
    override def toPrettyJson(t: Int) = s"$t"
  }

  implicit class AnyRefOps[T](val t: T) extends AnyVal {
    def toJson(implicit c: JsonConverter[T]): String = c.toJson(t)
    def toPrettyJson(implicit c: JsonConverter[T]): String = c.toPrettyJson(t)
  }

}
