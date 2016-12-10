package com.github.niqdev

import scala.concurrent.duration.FiniteDuration

package object myidmobile {

  /* Alternative to TimeUnit.SECONDS.sleep(5)
   * Usage:
   * import scala.concurrent.duration._
   * 5.seconds.sleep
   */
  implicit class FiniteDurationOps(val d: FiniteDuration) extends AnyVal {
    final def sleep: Unit = Thread.sleep(d.toMillis)
  }

  /* Alternative to TimeUnit.SECONDS.sleep(5)
   * Usage:
   * import scala.concurrent.duration._
   * sleep for_ 5.seconds
   */
  object sleep {
    def for_(d: FiniteDuration): Unit = d.sleep
  }
}