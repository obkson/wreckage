package se.obkson.wreckage.parsing

import sjsonnew.shaded.scalajson.ast.{JValue, JNull, JTrue, JFalse, JNumber, JString, JArray, JObject}
import jawn.{ MutableFacade }
import scala.collection.mutable

object ScalaJsonFacade {
  implicit val parserFacade: MutableFacade[JValue] = new MutableFacade[JValue] {
    def jnull() = JNull
    def jfalse() = JFalse
    def jtrue() = JTrue
    def jnum(s: CharSequence, decIndex: Int, expIndex: Int) = JNumber(s.toString).get
    def jstring(s: CharSequence) = JString(s.toString)
    def jarray(vs: mutable.ArrayBuffer[JValue]) = JArray(vs.toVector)
    def jobject(vs: mutable.Map[String, JValue]) = JObject(vs.toMap)
  }
}
