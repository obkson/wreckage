package whiteoak

import wreckage.builder._, benchmarking._

object WhiteoakNative_1_0__Whiteoak_2_1 extends WhiteoakJMHProjectBuilder {

  val dependencies = List(
    Dependency("whiteoaknative","whiteoaknative","1.0")
  )

  // whiteoak syntax
  object Syntax extends RecordSyntax {

    val imports = List("whiteoaknative.*") // native approach, no imports

    def create(fields: Seq[(String, String)]): String = {
      val max = fields.map(_._1.split("f")(1).toInt).max
      fields.map{ case (_, v) => v }.mkString(s"new RecordClass$max(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      val max = fields.map(_._1.split("f")(1).toInt).max
      return s"RecordStruct$max"
    }

    def access(prefix: String, field: String): String = {
      s"""$prefix.$field()"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    WhiteoakRTAccessFields
    //WhiteoakRTAccessPolymorphism
  )

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

