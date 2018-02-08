package wreckage.builder
package benchmarking

trait RTBenchmark extends Benchmark {

  /* Real Time Benchmark specific source generator */
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val methods = inputs.map{ input => method(input, recSyntax) }.mkString("\n")

    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{name}}"          -> this.name,
          "{{imports}}"       -> imports(recSyntax),
          "{{state}}"         -> state(recSyntax),
          "{{methods}}"       -> methods)
    )
    src
  }

  /* implementations of this trait must provide: */
  def name: String
  def template: String

  def inputs: Seq[Int]
  def types: Seq[RecordType]

  def state(recSyntax: RecordSyntax): String
  def imports(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String
}

trait ScalaRTBenchmark extends RTBenchmark {
  def template = "/JMHScalaRTTemplate.scala"
  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map(imp => s"import $imp").mkString("\n")

  /* implementations of this trait must still provide: */
  def name: String
  def inputs: Seq[Int]
  def types: Seq[RecordType]

  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String
}
