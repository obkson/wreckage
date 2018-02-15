package scalanative
import java.nio.file.Paths
import scala.collection.immutable.Set

import wreckage.builder._, benchmarking._

object Dotty06_FieldTraitGeneric extends BenchmarkAndLibraryGenerator with DottyLanguage {

  val name = "dotty06_fieldtraitgeneric"

  lazy val library = new RecordLibrary {
    val name = s"dotty06_fieldtraitgeneric_lib"

    val pkg = List("se", "obkson", "wreckage", name)

    val output = Dependency(List("se", "obkson", "wreckage"), s"${name}_0.6", "0.1")

    def decl(tpe: RecordType): String = {
      val traitDecls = tpe.fields.map{ case (l, t) => s"Field_${l}[$t]" }.mkString("extends ", " with ", "")
      val params = tpe.fields.map{ case (l, t) => s"$l: $t" }.mkString(",")

      val imports = tpe.fields.flatMap(fld => fld match {
        case (_, "Instant") => Some("import java.time.Instant")
        case _ => None
      }).mkString("\n")

      s"""|$imports
          |case class ${tpe.alias}($params) $traitDecls
          |""".stripMargin
    }

    def baseDecl(tpe: RecordType) = None

    def fieldDecl(label: String, tpe: String) = Some(
      s"""|trait Field_${label}[T] {
          |  def $label: T
          |}""".stripMargin
    )
  }

  lazy val syntax = new RecordSyntax {
    def dependencies = List(library.output)

    def imports = List(s"${library.pkg.mkString(".")}._")

    override def tpeCarrier(tpe: RecordType) = tpe.alias match {
      case "BaseCommitEvent"
         | "BaseCommit"
         | "BaseCommitIdentity" => s"type ${tpe.alias} = AnyRef"
      case _ => ""
    }

    // type erasure
    def tpe(tpe: RecordType): String = "AnyRef"

    // call constructor in lib
    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. CC2(f1=1, f2=2)
      s"""${tpe.alias}(${fields.map{ case (l, v) => s"$l=$v" }.mkString(", ")})"""
    }

    // cast to trait before access
    def access(prefix: String, label: String, tpe: String): String = {
      // e.g. rec.asInstanceOf[Field_f4_[Int]].f4
      s"""$prefix.asInstanceOf[Field_${label}[$tpe]].$label"""
    }

    def increment(prefix: String, field: String): String = {
      s"$prefix.copy($field=${access(prefix, field, "Int")}+1)"
    }
  }

  lazy val benchmarks = List[Benchmark](
    ScalaRTCaseStudyComplete,
    ScalaRTCaseStudyCompleteSubtyped,
    ScalaRTCreationSize,
    ScalaRTAccessSize,
    ScalaRTAccessFields,
    ScalaRTAccessPolymorphism
  )
}
