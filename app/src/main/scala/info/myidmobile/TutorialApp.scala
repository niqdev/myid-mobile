package info.myidmobile

import org.scalajs.dom
import org.scalajs.dom.document

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

/**
  * @author niqdev
  */
object TutorialApp extends JSApp {

  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    appendPar(document.body, "Hello World")
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  @JSExport
  def showBalance(): Unit = {
    appendPar(document.body, "TODO")
  }

}
