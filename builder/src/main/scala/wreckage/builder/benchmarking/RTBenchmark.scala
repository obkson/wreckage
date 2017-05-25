package wreckage.builder
package benchmarking

trait RTBenchmark extends Benchmark{

  /* Real Time Benchmark specific source generator */
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val methods = inputs.map{ input => method(input, recSyntax) }.mkString("\n")

    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{imports}}"       -> imports(recSyntax),
          "{{name}}"          -> this.name,
          "{{state}}"         -> state(recSyntax),
          "{{methods}}"       -> methods)
    )
    src
  }

  /* implementations of this trait must provide: */
  def template: String
  def filename_extension: String
  def inputs: Seq[Int]
  def name: String
  def imports(recSyntax: RecordSyntax): String
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String

}

trait ScalaRTBenchmark extends RTBenchmark {
  def template = "/JMHScalaRTTemplate.scala"
  def filename_extension = "scala"
  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map{imp => s"import $imp"}.mkString("\n")

  /* implementations of this trait must still provide: */
  def name: String
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String
}

trait JavaRTBenchmark extends RTBenchmark {
  val template = "/JMHJavaRTTemplate.java"
  def filename_extension = "java"
  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map( imp => s"import $imp;").mkString("\n")

  /* implementations of this trait must still provide: */
  def name: String
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String
}

trait WhiteoakRTBenchmark extends RTBenchmark {
  val template = "/JMHWhiteoakRTTemplate.wo"
  def filename_extension = "wo"
  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map( imp => s"import $imp;").mkString("\n")

  /* implementations of this trait must still provide: */
  def name: String
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String
}
