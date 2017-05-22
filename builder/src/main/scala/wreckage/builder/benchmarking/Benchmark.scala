package wreckage.builder
package benchmarking

trait Benchmark extends {
  val name: String
  val inputs: Seq[Int]
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String
  val template: String
  def imports(recSyntax: RecordSyntax): String

  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val methods = inputs.map{ input => method(input, recSyntax) }.mkString("\n")

    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"     -> pkg.mkString("."),
          "{{imports}}" -> imports(recSyntax),
          "{{name}}"    -> this.name,
          "{{state}}"   -> state(recSyntax),
          "{{methods}}" -> methods)
    )
    src
  }
  def sourceFile(pkg: Seq[String], recSyntax: RecordSyntax): SourceFile
}

trait ScalaBenchmark extends Benchmark {
  val template = "/JMHScalaTemplate.scala"

  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map{imp => s"import $imp"}.mkString("\n")

  def sourceFile(pkg: Seq[String], recSyntax: RecordSyntax): SourceFile = {
    SourceFile(pkg, s"${this.name}.scala", source(pkg, recSyntax))
  }
}

trait WhiteoakBenchmark extends Benchmark {
  val template = "/JMHWhiteoakTemplate.wo"

  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map( imp => s"import $imp;").mkString("\n")

  def sourceFile(pkg: Seq[String], recSyntax: RecordSyntax): SourceFile = {
    SourceFile(pkg, s"${this.name}.wo", source(pkg, recSyntax))
  }
}
