package dottynative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._
object SelRecList__Dotty_0_1 extends Dotty_0_1__JMHProjectBuilder {

  val artifactId = this.name

  override def unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("se","obkson","wreckage","records"), "dottynative_0.1", "0.1",
      Paths.get("records/dottynative/target/scala-0.1/dottynative_0.1-0.1.jar").toAbsolutePath())
  )

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("dottynative._")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. Record(("f1"->1, "f2"->2).asInstanceOf[ SelRecList { val f1: Int; val f2: Int } ]
      fields.map{ case (k, v) => s""""$k"->$v""" }
        .mkString(s"SelRecList(", ", ",s").asInstanceOf[ ${tpe(fields)} ]")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. SelRecList { val f1: Int; val f2: Int }
      fields.map{ case (k, v) => s"val $k: Int" }
        .mkString(s"SelRecList { ","; ", " }")
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f2
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

