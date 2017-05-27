package scalanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object InterfaceRecord__Scala_2_11_8 extends ScalaJMHProjectBuilder {

  val scalaVersion = "2.11.8"

  override val unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("se","obkson","wreckage","records"), "scalanative_2.11", "0.1",
      Paths.get("records/scalanative/target/scala-2.11/scalanative_2.11-0.1.jar").toAbsolutePath())
  )


  // case class syntax
  object Syntax extends RecordSyntax {

    val imports = List("scalanative._")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. InterfaceRecord4(1,2,3,4)
      val idx = fields.indexWhere(_._1 == "g1")
      if (idx == -1){
        fields.map{ case (k, v) => s"""$v""" }.mkString(s"InterfaceRecord${fields.length}(", ", ",")")
      }else{
        fields.map{ case (k, v) => s"""$v""" }.mkString(s"InterfaceRecordPoly${fields.length}_${idx+1}(", ", ",")")
      }
    }

    def tpe(fields: Seq[(String, String)]): String = {
      val idx = fields.indexWhere(_._1 == "g1")
      if (idx == -1){
        // e.g. InterfaceRecordType4
        s"InterfaceRecordType${fields.length}"
      } else {

        // e.g. InterfaceRecordTypePoly32_4
        s"InterfaceRecordTypePoly${fields.length}_${idx+1}"
      }
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4
      s"""$prefix.$field"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaRTAccessFields,
    ScalaRTAccessSize,
    ScalaRTAccessPolymorphism
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

