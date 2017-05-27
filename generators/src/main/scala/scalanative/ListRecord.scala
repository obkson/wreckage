package scalanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object ListRecord__Scala_2_11_8 extends ScalaJMHProjectBuilder {

  val scalaVersion = "2.11.8"

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List()

    def create(fields: Seq[(String, String)]): String = {
      // e.g. List[Any](1,2,3,4)
      fields.map{ case (k, v) => s"""$v""" }.mkString(s"List[Any](", ",",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. List[Any]
      s"List[Any]"
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

