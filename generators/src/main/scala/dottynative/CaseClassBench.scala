package dottynative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._
object CaseClass__Dotty_0_1 extends DottyJMHProjectBuilder {

  def getLatestDottyBuild = {
    // This is how sbt-dotty plugin does it:
    val Version = """      <version>(0.1\..*-bin.*)</version>""".r
    scala.io.Source
      .fromURL("http://repo1.maven.org/maven2/ch/epfl/lamp/dotty_0.1/maven-metadata.xml")
      .getLines()
      .collect { case Version(version) => version }
      .toSeq
      .last
  }

  val dottyBuildVersion = getLatestDottyBuild // e.g. "0.1.1-bin-20170506-385178d-NIGHTLY"
  val artifactId = this.name

  override def unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("se","obkson","wreckage","records"), "dottynative_0.1", "0.1",
      Paths.get("records/dottynative/target/scala-0.1/dottynative_0.1-0.1.jar").toAbsolutePath())
  )

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("dottynative._")

    // TODO check byte-code of named arguments!
    def create(fields: Seq[(String, String)]): String = {
      // e.g. CaseClass4(f1=1, f2=2, f3=3, f4=4)
      fields.map{ case (k, v) => s"""$k=$v""" }.mkString(s"${this.tpe(fields)}(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. CaseClass4
      s"CaseClass${fields.length}"
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTAccessFields,
    ScalaRTAccessPolymorphism
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}
