package scalanative
import java.nio.file.Paths

import wreckage.builder._, benchmarking._

object CaseClass__Scala_2_12_3 extends ScalaJMHProjectBuilder {

  override val unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("se", "obkson", "wreckage"), "parsing_2.12", "0.1",
      Paths.get("parsing/target/scala-2.12/parsing_2.12-0.1.jar").toAbsolutePath())
  )

  // shapeless syntax
  object Syntax extends RecordSyntax {

    def imports = List()

    def decl(name: String, fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s"""$k: $v""" }.mkString(s"case class $name(", ", ", ")")
    }

    def create(name: String, fields: Seq[(String, String)]): String = {
      // e.g. CaseClass4(f1=1, f2=2, f3=3, f4=4)
      fields.map{ case (k, v) => s"""$k=$v""" }.mkString(s"${tpe(name, fields)}(", ", ",")")
    }

    // nominal typing
    def tpe(name: String, fields: Seq[(String, String)]): String = name

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }

    def increment(prefix: String, field: String): String = {
      s"""$prefix.copy($field=${access(prefix, field)}+1)"""
    }
  }

  val scalaVersion = "2.12.3"

  val pkg = List("benchmarks")
  val features = List(
    ScalaCTCreationAccessSize,
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTUpdateSize,
    ScalaRTCaseStudy
  )

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))
}
