package compossible

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

abstract class Compossible extends ScalaJMHProjectBuilder {

  // scala-records syntax
  object Syntax extends RecordSyntax {

    val imports = List("org.cvogt.compossible._")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. (Record f1 1 f2 2)
      fields.map{ case (k, v) => s"$k $v" }.mkString("(Record ", " ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = ??? // Not expressible inline

    def access(prefix: String, field: String): String = {
      // e.g. rec.f2
      s"""$prefix.$field"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaCTCreationSize,
    ScalaCTCreationAccessLast,
    ScalaCTCreationAccessSize,
    ScalaRTCreationFields,
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism
  )

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

object Compossible_0_2__Scala_2_11_8 extends Compossible {
  val scalaVersion = "2.11.8"
  override val unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("org","cvogt"), "compossible_2.11", "0.2-SNAPSHOT",
      Paths.get("records/compossible/target/scala-2.11/compossible_2.11-0.2-SNAPSHOT.jar").toAbsolutePath())
  )
}

