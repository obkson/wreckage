package scalanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object CaseClass__Scala_2_11_8 extends ScalaJMHProjectBuilder {

  val scalaVersion = "2.11.8"
  val artifactId = this.name

  val unmanagedDependencies = List(
    UnmanagedDependency(List("se","obkson","wreckage","records"), "scalanative_2.11", "0.1",
      Paths.get("records/scalanative/target/scala-2.11/scalanative_2.11-0.1.jar").toAbsolutePath())
  )
  val managedDependencies = List()

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("scalanative._")

    // TODO check byte-code of named arguments!
    def create(fields: Seq[(String, String)]): String = {
      // e.g. CaseClass4(f1=1, f2=2, f3=3, f4=4)
      fields.map{ case (k, v) => s"""$k=$v""" }.mkString(s"${this.tpe(fields)}(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. CaseClass4
      s"CaseClass${fields.length}"
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTAccessFields,
    ScalaRTAccessPolymorphism
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}
