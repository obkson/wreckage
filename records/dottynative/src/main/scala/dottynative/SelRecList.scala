package dottynative

import scala.Selectable

case class SelRecList(elems: (String, Any)*) extends Selectable {
  def selectDynamic(name: String): Any = elems.find(_._1 == name).get._2
}
