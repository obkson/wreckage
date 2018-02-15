package scalanative
import java.nio.file.Paths

import wreckage.builder._, benchmarking._

object Scala212_AnonRef extends BenchmarkGenerator with ScalaLanguage {

  val name = "scala212_anonref"

  lazy val syntax = new RecordSyntax {
    def dependencies = List()

    val imports = List()

    def tpe(tpe: RecordType): String = {
      // e.g. AnyRef {val f1: Int; val f2: Int; val f3: Int; val f4: Int}
      tpe.fields.map{ case (l, t) => s"val $l: $t" }.mkString("AnyRef {", "; ", "}")
    }

    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. new {val f1=1; val f2=2; val f3=3; val f4=4}
      fields.map{ case (k, v) => s"""val $k=$v""" }.mkString(s"new {", "; ","}")
    }

    // dot notation
    def access(prefix: String, label: String, tpe: String): String = {
      // e.g. rec.f2
      s"""$prefix.$label"""
    }

    // Not supported
    def increment(prefix: String, field: String): String = ???
  }

  lazy val benchmarks = List[Benchmark](
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism
    // ScalaRTUpdateSize // Not supported!
  )
}
