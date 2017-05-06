package wreckage.builder
package benchmarking

trait Benchmark {
  val name = this.getClass.getSimpleName.split("\\$")(0)

  val inputs: Seq[Int]
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String

  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val imports = recSyntax.imports.mkString("import ", ", ", "")
    val methods = inputs.map{ input => method(input, recSyntax) }.mkString("\n")

    val tmplStr = FileHelper.getResourceForClassAsString("/JMHBenchmark.scala", getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"     -> pkg.mkString("."),
          "{{imports}}" -> imports,
          "{{name}}"    -> this.name,
          "{{state}}"   -> state(recSyntax),
          "{{methods}}" -> methods)
    )
    src
  }

  def sourceFile(pkg: Seq[String], recSyntax: RecordSyntax): SourceFile = {
    SourceFile(pkg, s"${this.name}.scala", source(pkg, recSyntax))
  }
}

