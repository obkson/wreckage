package scalarecords

import wreckage.builder._, benchmarking._

abstract class ScalaRecords extends ScalaJMHProjectBuilder {

  val pkg = List("benchmarks")
  val features = List(
    ScalaCTCreationSize,
    ScalaCTCreationAccessLast,
    ScalaCTCreationAccessSize,
    ScalaRTCreationFields,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism
  )

  def syntax: RecordSyntax

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, syntax))

}

object ScalaRecords_0_3__Scala_2_11_8 extends ScalaRecords {
  val scalaVersion = "2.11.8"
  override val managedDependencies = super.managedDependencies ++ List(
      ManagedDependency(List("ch","epfl","lamp"),"scala-records_2.11","0.3")
    )

  // scala-records 0.3 syntax
  def syntax = new RecordSyntax {

    val imports = List("records._")

    def create(fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s""""$k"->$v""" }.mkString("Rec(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = ???

    def access(prefix: String, field: String): String = {
      s"""$prefix.$field"""
    }
  }
}

object ScalaRecords_0_4__Scala_2_11_8 extends ScalaRecords {
  val scalaVersion = "2.11.8"
  override val managedDependencies = super.managedDependencies ++ List(
      ManagedDependency(List("ch","epfl","lamp"),"scala-records_2.11","0.4")
    )

  // scala-records 0.4 syntax
  def syntax = new RecordSyntax {

    val imports = List("records._")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. Rec("f1"->1, "f2"->2, "f3"->3, "f4"->4)
      fields.map{ case (k, v) => s""""$k"->$v""" }.mkString("Rec(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. Rec[{def f1: Int; def f2: Int; def f3: Int; def f4: Int}]
      fields.map{ case (k, v) => s"""def $k: Int""" }.mkString(s"Rec[{", "; ","}]")
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }
  }
}
