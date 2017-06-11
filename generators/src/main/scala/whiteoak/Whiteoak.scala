package whiteoak

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object WhiteoakNative__Whiteoak_2_1 extends WhiteoakJMHProjectBuilder {

  val whiteoakVersion = "2.1"
  val artifactId = this.name

  val unmanagedDependencies = List(
    UnmanagedDependency(List("se","obkson","wreckage","records"), "whiteoaknative_2.1", "0.1",
      Paths.get("records/whiteoaknative/target/whiteoak-2.1/whiteoaknative_2.1-0.1.jar").toAbsolutePath()),

    UnmanagedDependency(List("net","sourceforge","whiteoak"), "whiteoak", "2.1",
      Paths.get("whiteoak/whiteoak-2.1.jar").toAbsolutePath())
  )
  val managedDependencies = List()

  // whiteoak syntax
  object Syntax extends RecordSyntax {

    val imports = List("whiteoaknative.*")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. RecordClass4(1,2,3,4)
      fields.map{ case (_, v) => v }.mkString(s"new RecordClass${fields.length}(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      // e.g. RecordStruct4
      s"RecordStruct${fields.length}"
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f4()
      s"""$prefix.$field()"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    WhiteoakRTCreationSize,
    WhiteoakRTAccessFields
  )

  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

