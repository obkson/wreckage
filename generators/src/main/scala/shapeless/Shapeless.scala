package shapeless

import wreckage.builder._, benchmarking._

abstract class Shapeless extends ScalaJMHProjectBuilder {

  // shapeless syntax
  object Syntax extends RecordSyntax {

    val imports = List("shapeless._", "syntax.singleton._", "record._")

    def create(fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s"""("$k"->>$v)""" }.mkString("", " :: "," :: HNil")
    }

    def tpe(fields: Seq[(String, String)]): String = ??? // TODO

    def access(prefix: String, field: String): String = {
      s"""$prefix("$field")"""
    }
  }

  // Abstract
  val scalaVersion: String
  val dependencies: Seq[Dependency]

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTAccessFields,
    ScalaRTAccessPolymorphism
  )

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

object Shapeless_2_3_2__Scala_2_11_8 extends Shapeless {
  val scalaVersion = "2.11.8"
  val dependencies = List(
      Dependency("com.chuusai", "shapeless_2.11", "2.3.2")
    )
}

