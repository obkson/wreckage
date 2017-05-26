package scalanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object HashMapRecord__Scala_2_11_8 extends ScalaJMHProjectBuilder {

  val scalaVersion = "2.11.8"

  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("scala.collection.immutable.HashMap")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. HashMap[String,Any]("f1"->1,"f2"->2,"f3"->3,"f4"->4)
      fields.map{ case (k, v) => s""""$k"->$v""" }.mkString(s"HashMap[String,Any](", ",",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. HashMap[String,Any]
      s"HashMap[String,Any]"
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec("f4").asInstanceOf[Int]
      s"""$prefix("$field").asInstanceOf[Int]"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTAccessFields,
    ScalaRTAccessSize
    //ScalaRTAccessPolymorphism
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}


