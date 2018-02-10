package scalanative
import java.nio.file.Paths

import wreckage.builder._, benchmarking._

object Dotty06_CaseClass extends BenchmarkAndLibraryGenerator with DottyLanguage {

  val name = "dotty06_caseclass"

  lazy val library = new RecordLibrary {
    val name = s"dotty06_caseclass_lib"

    val pkg = List("se", "obkson", "wreckage", name)

    val output = Dependency(List("se", "obkson", "wreckage"), s"${name}_0.6", "0.1")

    def decl(tpe: RecordType): String = {
      // e.g. case class CC2(f1: Int, f2: Int)
      s"""case class ${tpe.alias}(${tpe.fields.map{ case (l, t) => s"$l: $t" }.mkString(", ")})"""
    }
  }

  lazy val syntax = new RecordSyntax {
    def dependencies = List(library.output)

    def imports = List(s"${library.pkg.mkString(".")}._")

    // nominal typing
    def tpe(tpe: RecordType): String = tpe.alias

    // call constructor in lib
    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. CC2(f1=1, f2=2)
      s"""${tpe.alias}(${fields.map{ case (k, v) => s"$k=$v" }.mkString(", ")})"""
    }

    // dot notation
    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }

    // use case class built-in copy method
    def increment(prefix: String, field: String): String = {
      s"""$prefix.copy($field=${access(prefix, field)}+1)"""
    }
  }

  lazy val benchmarks = List[Benchmark](
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTUpdateSize
  )
}
