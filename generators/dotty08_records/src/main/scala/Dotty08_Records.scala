package scalanative
import java.nio.file.Paths

import wreckage.builder._, benchmarking._

object Dotty08_Records extends BenchmarkGenerator with DottyLanguage {

  val name = "dotty08_records"

  lazy val syntax = new RecordSyntax {
    def dependencies = List()

    val imports = List("dotty.records._")

    override def tpeCarrier(tpe: RecordType): String = {
      // e.g. type User = Record{ val name: String }
      s"type ${tpe.alias} = ${this.tpe(tpe)}"
    }

    // Structural typing
    def tpe(tpe: RecordType): String = {
      // e.g. Record { val f1: Int; val f2: Int }
      tpe.fields.map{ case (k, v) => s"val $k: $v" }.mkString(s"Record { ","; ", " }")
    }

    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. Record("f1", 1) + ("f2", 2)
      fields.map{ case (k, v) => s"""("$k", $v)""" }.mkString(s"Record", " + ","")
    }

    // Dot notation
    def access(prefix: String, label: String, tpe: String): String = {
      // e.g. rec.f2
      s"""$prefix.$label"""
    }

    // *Safe*: Extend with already existing field
    def increment(prefix: String, field: String): String = {
      // e.g. rec + ("f1", (rec.f1+1))
      s"""$prefix + ("$field", ($prefix.$field+1))"""
    }
  }

  lazy val benchmarks = List[Benchmark](
    //ScalaRTCaseStudyComplete,
    //ScalaRTCaseStudyReadUpdate,
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism,
    ScalaRTUpdateSize
  )
}
