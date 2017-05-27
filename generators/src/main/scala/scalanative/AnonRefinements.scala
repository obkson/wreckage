package scalanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object AnonRefinements__Scala_2_11_8 extends ScalaJMHProjectBuilder {

  val scalaVersion = "2.11.8"

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("scala.language.reflectiveCalls")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. new {val f1=1; val f2=2; val f3=3; val f4=4;}
      fields.map{ case (k, v) => s"""val $k=$v""" }.mkString(s"new {", "; ","}")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. AnyRef{val f1: Int; val f2: Int; val f3: Int; val f4: Int;}
      fields.map{ case (k, v) => s"""val $k: Int""" }.mkString(s"AnyRef{", "; ","}")
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }
  }

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
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}
