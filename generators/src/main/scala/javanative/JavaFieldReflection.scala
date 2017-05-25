package javanative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object JavaFieldReflection__Java_1_8 extends JavaJMHProjectBuilder {

  val artifactId = this.name

  override val unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("se","obkson","wreckage","records"), "javanative_1.8", "0.1",
      Paths.get("records/javanative/target/java-1.8/javanative_1.8-0.1.jar").toAbsolutePath())
  )

  // syntax
  object Syntax extends RecordSyntax {

    val imports = List("javanative.*")

    def create(fields: Seq[(String, String)]): String = {
      // e.g. FieldClass4(1,2,3,4)
      fields.map{ case (_, v) => v }.mkString(s"new FieldClass${fields.length}(", ", ",")")
    }

    def tpe(fields: Seq[(String, String)]): String = {
      s"Object"
    }

    def access(prefix: String, field: String): String = {
      // e.g. (int) rec.getClass.getField("f4").get(rec)
      s"""(int) $prefix.getClass().getField("$field").get($prefix)"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    //JavaRTCreationFields,
    JavaRTAccessSize,
    JavaRTAccessFields
    //JavaRTAccessPolymorphism
  )

  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))

}

