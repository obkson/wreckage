package shapeless

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

abstract class Shapeless extends ScalaJMHProjectBuilder {

  // shapeless syntax
  object Syntax extends RecordSyntax {

    val imports = List("shapeless._", "syntax.singleton._", "record._")

    def decl(name: String, fields: Seq[(String, String)]): String = {
      s"""type $name = ${tpe(name, fields)}"""
    }

    def create(name: String, fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s"""('$k->>$v)""" }.mkString("", " :: "," :: HNil")
    }

    def tpe(name: String, fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s"""'$k->$v""" }.mkString("Record.`",", ","`.T")
    }

    def access(prefix: String, field: String): String = {
      s"""$prefix.get('$field)"""
    }

    def increment(prefix: String, field: String) = {
      s"""$prefix.updateWith('$field)(_ + 1)"""
    }
  }

  // Abstract
  val scalaVersion: String

  val pkg = List("benchmarks")
  val features = List(
    ScalaCTCreationAccessSize,
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTUpdateSize,
    ScalaRTCaseStudy
  )

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

object Shapeless_2_3_2__Scala_2_12_3 extends Shapeless {
  val scalaVersion = "2.12.3"
  override val managedDependencies = super.managedDependencies ++ List(
      ManagedDependency(List("com","chuusai"), "shapeless_2.12", "2.3.2")
    )
  override val unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("se", "obkson", "wreckage"), "parsing_2.12", "0.1",
      Paths.get("parsing/target/scala-2.12/parsing_2.12-0.1.jar").toAbsolutePath())
  )
}
