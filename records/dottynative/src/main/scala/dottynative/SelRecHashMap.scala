package dottynative

import scala.Selectable
import scala.collection.immutable.HashMap

object SelRecHashMap {
  def apply(_data: (String, Any)*) = new SelRecHashMap(_data = HashMap(_data:_*))
}

case class SelRecHashMap(_data: Map[String, Any]) extends Selectable {
  def selectDynamic(name: String): Any = _data(name)
}

