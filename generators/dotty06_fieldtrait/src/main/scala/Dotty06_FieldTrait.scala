package scalanative
import java.nio.file.Paths
import scala.collection.immutable.Set

import wreckage.builder._, benchmarking._

object Dotty06_FieldTrait extends BenchmarkAndLibraryGenerator with DottyLanguage {

  val name = "dotty06_fieldtrait"

  lazy val library = new RecordLibrary {
    val name = s"dotty06_fieldtrait_lib"

    val pkg = List("se", "obkson", "wreckage", name)

    val output = Dependency(List("se", "obkson", "wreckage"), s"${name}_0.6", "0.1")

    def decl(tpe: RecordType): String = {
      val baseFields = tpe.parent match {
        case Some(pTpe) => pTpe.fields
        case None => List()
      }
      val baseDecls = tpe.parent match {
        case Some(pTpe) => List(pTpe.alias)
        case None => List()
      }
      // filter out the fields not already taken care of by the parent interface
      val interfaceFields = Set[(String, String)](tpe.fields: _*) -- Set[(String, String)](baseFields: _*)
      val interfaceDecls = interfaceFields.map{ case (l, t) => s"Field_${l}_${t}" }.toList.sorted
      // implement both parent and the field interfaces
      val implements = (baseDecls ++ interfaceDecls).mkString(" with ")
      val params = tpe.fields.map{ case (l, t) => s"$l: $t" }.mkString(",")

      s"""case class ${tpe.alias}($params) extends $implements"""
    }

    def baseDecl(tpe: RecordType) = {
      val getters = tpe.fields.map{ case (l, t) => s"  def $l: $t" }.mkString("\n")
      Some(
        s"""|trait ${tpe.alias} {
            |$getters
            |}""".stripMargin
      )
    }

    def fieldDecl(label: String, tpe: String) = Some(
      s"""|trait Field_${label}_${tpe} {
          |  def $label: $tpe
          |}""".stripMargin
    )
  }

  lazy val syntax = new RecordSyntax {
    def dependencies = List(library.output)

    def imports = List(s"${library.pkg.mkString(".")}._")

    // nominal typing
    def tpe(tpe: RecordType): String = tpe.alias

    // call constructor in lib
    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. CC2(f1=1, f2=2)
      s"""${tpe.alias}(${fields.map{ case (l, v) => s"$l=$v" }.mkString(", ")})"""
    }

    // dot notation
    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }

    // TODO!!!
    def increment(prefix: String, field: String): String = ???
  }

  lazy val benchmarks = List[Benchmark](
    ScalaRTCreationSize,
    ScalaRTAccessPolymorphism
  )
}
