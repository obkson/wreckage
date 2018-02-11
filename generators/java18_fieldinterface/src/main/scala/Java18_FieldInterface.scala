package scalanative
import java.nio.file.Paths
import scala.collection.immutable.Set

import wreckage.builder._, benchmarking._

object Java18_FieldInterface extends BenchmarkAndLibraryGenerator with JavaLanguage {

  val name = "java18_fieldinterface"

  lazy val library = new RecordLibrary {
    val name = s"java18_fieldinterface_lib"

    val pkg = List("se", "obkson", "wreckage", name)

    val output = Dependency(List("se", "obkson", "wreckage"), name, "0.1")

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
      val implements = (baseDecls ++ interfaceDecls).mkString(", ")

      // inner components of java class:
      val privateFields = tpe.fields.map{ case (l, t) => s"private final ${t.toLowerCase} _$l;" }.mkString("\n  ")
      val constructorParams = tpe.fields.map{ case (l, t) => s"int $l" }.mkString(",")
      val constructorAssign = tpe.fields.map{ case (l, _) => s"_$l = $l;" }.mkString("\n    ")
      val getters = tpe.fields.map{ case (l, t) => s"  public ${t.toLowerCase} $l() {return _$l;}" }.mkString("\n")

      s"""|public class ${tpe.alias} implements $implements {
          |  $privateFields
          |
          |  public ${tpe.alias}($constructorParams) {
          |    $constructorAssign
          |  }
          |
          |$getters
          |}""".stripMargin
    }

    def baseDecl(tpe: RecordType) = {
      val getters = tpe.fields.map{ case (l, t) => s"  public ${t.toLowerCase} $l();" }.mkString("\n")
      Some(
        s"""|public interface ${tpe.alias} {
            |$getters
            |}""".stripMargin
      )
    }

    def fieldDecl(label: String, tpe: String) = Some(
      s"""|public interface Field_${label}_${tpe} {
          |  public ${tpe.toLowerCase()} $label();
          |}""".stripMargin
    )
  }

  lazy val syntax = new RecordSyntax {
    def dependencies = List(library.output)

    def imports = List(s"${library.pkg.mkString(".")}.*")

    // nominal typing
    def tpe(tpe: RecordType): String = tpe.alias

    // call constructor in lib
    def create(tpe: RecordType, fields: Seq[(String, String)]): String = {
      // e.g. new CC2(1, 2)
      s"""new ${tpe.alias}(${fields.map{ kv => kv._2 }.mkString(", ")})"""
    }

    // getter notation
    def access(prefix: String, field: String): String = {
      // e.g. rec.f4()
      s"""$prefix.$field()"""
    }

    // TODO!!!
    def increment(prefix: String, field: String): String = ???
  }

  lazy val benchmarks = List[Benchmark](
    JavaRTCreationSize,
    JavaRTAccessPolymorphism
  )
}
