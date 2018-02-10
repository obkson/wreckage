package scalanative
import java.nio.file.Paths

import wreckage.builder._, benchmarking._

object Scala212_Compossible extends BenchmarkGenerator with ScalaLanguage {

  val name = "scala212_compossible"

  lazy val syntax = new RecordSyntax {
    def dependencies = List(
      Dependency(List("org","cvogt"), "compossible_2.12", "0.2-SNAPSHOT")
    )

    val imports = List("org.cvogt.compossible._")

    override def tpeCarrier(tpe: RecordType): String = {
      // e.g. (RecordType f1[Int] & f2[Int] &)
      val carrier = tpe.fields.map{ case (k, v) => s"$k[Int]" }.mkString("(RecordType ", " & "," &)")
      s"val carrier_${tpe.alias} = $carrier"
    }

    def tpe(tpe: RecordType): String = {
      // use type carrier
      s"carrier_${tpe.alias}.Type"
    }

    // But no need for type alias to construct
    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. (Record f1 1 f2 2)
      fields.map{ case (k, v) => s"$k $v" }.mkString("(Record ", " ",")")
    }

    // dot notation
    def access(prefix: String, field: String): String = {
      // e.g. rec.f2
      s"""$prefix.$field"""
    }

    // *unsafe* hack: extend with already existing field
    def increment(prefix: String, field: String): String = {
      s"""$prefix $field ($prefix.$field + 1)"""
    }
  }

  lazy val benchmarks = List[Benchmark](
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism,
    ScalaRTUpdateSize
  )
}
