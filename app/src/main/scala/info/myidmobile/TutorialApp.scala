package info.myidmobile

import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExport

/**
  * @author niqdev
  */
@JSExport
object TutorialApp {

  @JSExport
  def main(): Unit = {
    dom.console.log("test")
    appendPar(document.body, "Hello World Workbench")
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
