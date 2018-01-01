package dottynative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object Records__Dotty_LocalFork extends DottyJMHProjectBuilder {

  val artifactId = this.name
  val dottyBuildVersion = "0.4.0-bin-SNAPSHOT"
  val dottyBuildDir = "/Users/obkson/code/records/dev/dotty/dist-bootstrapped/target/pack/lib"
  override val pomPath = "/dotty-local-pom.xml"

  def managedDependencies = List(
    ManagedDependency(List("org","scala-lang"), "scala-library", "2.12.3"),
    ManagedDependency(List("org","scala-lang","modules"), "scala-asm", "5.1.0-scala-2"),
    ManagedDependency(List("com","typesafe","sbt"), "sbt-interface", "0.13.15")
  )

  def unmanagedDependencies = List(
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-library_0.4", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-library_0.4-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-compiler_0.4", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-compiler_0.4-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-interfaces", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-interfaces-$dottyBuildVersion.jar"))
  )

  object Syntax extends RecordSyntax {

    val imports = List("dotty.records._")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. Record(f1=1, f2=2)
      fields.map{ case (k, v) => s"$k=$v" }
        .mkString(s"Record(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. Record { val f1: Int; val f2: Int }
      fields.map{ case (k, v) => s"val $k: Int" }
        .mkString(s"Record { ","; ", " }")
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f2
      s"""$prefix.$field"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTCreationSize
    //ScalaRTAccessFields,
    //ScalaRTAccessSize,
    //ScalaRTAccessPolymorphism
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))
}
