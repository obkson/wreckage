package scalanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object ArrayRecord__Scala_2_11_8 extends ScalaJMHProjectBuilder {

  val scalaVersion = "2.11.8"

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("scala.collection.mutable.ArrayBuffer")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. ArrayBuffer[Any](1,2,3,4)
      fields.map{ case (k, v) => s"""$v""" }.mkString(s"ArrayBuffer[Any](", ",",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. ArrayBuffer[Any]
      s"ArrayBuffer[Any]"
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec(3).asInstanceOf[Int] // 3 is the index of f4
      s"""$prefix(${field.stripPrefix("f").stripPrefix("g").toInt-1}).asInstanceOf[Int]"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

