package dottynative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object CaseClass__Dotty_0_6_SNAPSHOT extends DottyJMHProjectBuilder {

  val artifactId = this.name
  val dottyBuildVersion = "0.6.0-bin-SNAPSHOT"
  val dottyBuildDir = "/Users/obkson/code/records/dev/dotty/dist-bootstrapped/target/pack/lib"
  override val pomPath = "/dotty-local-pom.xml"

  override def managedDependencies = super.managedDependencies ++ List(
    ManagedDependency(List("org","scala-lang"), "scala-library", "2.12.4"),
    ManagedDependency(List("org","scala-lang","modules"), "scala-asm", "6.0.0-scala-1"),
    ManagedDependency(List("com","typesafe","sbt"), "sbt-interface", "0.13.15")
  )

  def unmanagedDependencies = List(
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-library_0.6", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-library_0.6-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-compiler_0.6", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-compiler_0.6-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-interfaces", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-interfaces-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("se", "obkson", "wreckage"), "parsing_2.12", "0.1",
      Paths.get("parsing/target/scala-2.12/parsing_2.12-0.1.jar").toAbsolutePath())
  )

  object Syntax extends RecordSyntax {

    def imports = List()

    def decl(name: String, fields: Seq[(String, String)]): String = {
      fields.map{ case (k, v) => s"""$k: $v""" }.mkString(s"case class $name(", ", ", ")")
    }

    def create(name: String, fields: Seq[(String, String)]): String = {
      // e.g. CaseClass4(f1=1, f2=2, f3=3, f4=4)
      fields.map{ case (k, v) => s"""$k=$v""" }.mkString(s"${tpe(name, fields)}(", ", ",")")
    }

    // nominal typing
    def tpe(name: String, fields: Seq[(String, String)]): String = name

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }

    def increment(prefix: String, field: String): String = {
      s"""$prefix.copy($field=${access(prefix, field)}+1)"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTUpdateSize,
    ScalaRTCaseStudy
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}
