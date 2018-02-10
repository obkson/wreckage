package scalanative
import java.nio.file.Paths

import wreckage.builder._, benchmarking._

object Scala212_Shapeless233 extends BenchmarkGenerator with ScalaLanguage {

  val name = "scala212_shapeless233"

  lazy val syntax = new RecordSyntax {
    def dependencies = List(
      Dependency(List("com","chuusai"), "shapeless_2.12", "2.3.3")
    )

    val imports = List("shapeless._", "syntax.singleton._", "record._")

    // Shapeless' path-dependent type syntax trick
    def tpe(tpe: RecordType): String = {
      // e.g. Record.`'f1->Int, 'f2->Int`.T
      tpe.fields.map{ case (k, v) => s"""'$k->$v""" }.mkString("Record.`",", ","`.T")
    }

    // No need for type alias to construct
    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. ('f1->>1) :: ('f2->>2) :: HNil
      fields.map{ case (k, v) => s"""('$k->>$v)""" }.mkString("", " :: "," :: HNil")
    }

    // getter notation
    def access(prefix: String, field: String): String = {
      // e.g. rec.get('f2)
      s"""$prefix.get('$field)"""
    }

    // special "updateWith" method
    def increment(prefix: String, field: String) = {
      s"""$prefix.updateWith('$field)(_ + 1)"""
    }
  }

  lazy val benchmarks = List[Benchmark](
    ScalaRTCreationSize,
    ScalaRTAccessFields
  )
}
