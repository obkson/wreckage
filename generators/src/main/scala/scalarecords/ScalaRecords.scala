package scalarecords

import wreckage.builder._, benchmarking._

abstract class ScalaRecords extends ScalaJMHProjectBuilder {

  // scala-records syntax
  object Syntax extends RecordSyntax {

    val imports = List("records._")

    def create(fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s""""$k"->$v""" }.mkString("Rec(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = ??? // TODO!

    def access(prefix: String, field: String): String = {
      s"""$prefix.$field"""
    }
  }

  // Abstract
  val scalaVersion: String
  val managedDependencies: Seq[ManagedDependency]
  val unmanagedDependencies: Seq[UnmanagedDependency]

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTCreationFields,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism
  )

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

object ScalaRecords_0_3__Scala_2_11_8 extends ScalaRecords {
  val scalaVersion = "2.11.8"
  val managedDependencies = List(
      ManagedDependency(List("ch","epfl","lamp"),"scala-records_2.11","0.3")
    )
  val unmanagedDependencies = List()
}

object ScalaRecords_0_4__Scala_2_11_8 extends ScalaRecords {
  val scalaVersion = "2.11.8"
  val managedDependencies = List(
      ManagedDependency(List("ch","epfl","lamp"),"scala-records_2.11","0.4")
    )
  val unmanagedDependencies = List()
}
