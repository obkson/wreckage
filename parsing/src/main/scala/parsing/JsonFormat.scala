package se.obkson.wreckage.parsing

import sjsonnew.shaded.scalajson.ast.{JValue, JNull, JTrue, JFalse, JNumber, JString, JArray, JObject}
import scala.util.Try

/* Type class for converting json AST to value of type T */
trait JsonFormat[T] {
  def read(ast: JValue): T
}

object JsonFormat {
  def fromJson[T](ast: JValue)(implicit fmt: JsonFormat[T]): Try[T] = Try(fmt.read(ast))
  def deserializationError(e: String) = throw new Exception(e)

  /* Implementation of JsonFormat for basic types */

  implicit object JsonFormatBoolean extends JsonFormat[Boolean] {
    def read(ast: JValue): Boolean = ast match {
      case JTrue => true
      case JFalse => false
      case x => deserializationError("Expected Boolean as JTrue or JFalse, but got " + x)
    }
  }

  implicit object JsonFormatInt extends JsonFormat[Int] {
    def read(ast: JValue): Int = ast match {
      case JNumber(value) => try {
          value.toInt
        } catch {
          case e: Exception => deserializationError("Malformed Int " + value)
        }
      case x => deserializationError("Expected Int as JNumber, but got " + x)
    }
  }

  implicit object JsonFormatLong extends JsonFormat[Long] {
    def read(ast: JValue): Long = ast match {
      case JNumber(value) => try {
          value.toLong
        } catch {
          case e: Exception => deserializationError("Malformed Long " + value)
        }
      case x => deserializationError("Expected Long as JNumber, but got " + x)
    }
  }

  implicit object JsonFormatString extends JsonFormat[String] {
    def read(ast: JValue): String = ast match {
      case JString(value) => value
      case x => deserializationError("Expected String as JString, but got " + x)
    }
  }

  implicit def jsonFormatList[T](implicit fmt: JsonFormat[T]): JsonFormat[List[T]] = new JsonFormat[List[T]] {
    def read(ast: JValue): List[T] = ast match {
      case JArray(vector) => (vector map fmt.read).toList
      case x => deserializationError("Expected List as JArray, but got " + x)
    }
  }

  implicit def jsonFormatOption[T](implicit fmt: JsonFormat[T]): JsonFormat[Option[T]] = new JsonFormat[Option[T]] {
    def read(ast: JValue): Option[T] = ast match {
      case JNull => None
      case x => Some(fmt.read(x))
    }
  }
}
