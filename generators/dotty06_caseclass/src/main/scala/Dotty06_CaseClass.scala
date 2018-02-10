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
      val inheritance = tpe.parent match {
        case Some(pTpe) => s"extends ${pTpe.alias}(${pTpe.fields.map(_._1).mkString(", ")})"
        case None => ""
      }
      val parentFields: Set[String] = tpe.parent match {
        case Some(pTpe) => Set(pTpe.fields.map(_._1): _*)
        case None => Set.empty
      }
      val fieldDecls = tpe.fields.map{ case (l, t) =>
        if (parentFields.contains(l))
          s"override val $l: $t"
        else
          s"$l: $t"
      }.mkString(", ")
      // e.g. case class CC2(override val f1: Int, f2: Int) extends BaseClass(f1)
      s"""case class ${tpe.alias}($fieldDecls) $inheritance"""
    }

    def baseDecl(tpe: RecordType) = Some(
      s"""class ${tpe.alias}(${tpe.fields.map{ case (l, t) => s"val $l: $t" }.mkString(", ")})"""
    )

    def fieldDecl(label: String, tpe: String) = None
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
    ScalaRTAccessPolymorphism,
    ScalaRTUpdateSize
  )
}
