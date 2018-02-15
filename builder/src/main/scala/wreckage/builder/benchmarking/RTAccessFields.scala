package wreckage.builder
package benchmarking

trait RTAccessFields {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,10,20,30)

  def types = {
    val numFields = inputs.max
    val fields = (1 to numFields).map { idx => (s"f$idx", "Int") }
    List(
      RecordType(s"RTAccessFields_Rec$numFields", None, fields)
    )
  }
}

// Scala Version

case object ScalaRTAccessFields extends ScalaRTBenchmark with RTAccessFields {

  def state(recSyntax: RecordSyntax): String = {
    val tpe = types(0)
    val args = (1 to inputs.max).map { idx => (s"f$idx", s"$idx") }

    // let the accessed record be a mutable var in benchmarking state to prevent constant folding
    s"""|${recSyntax.tpeCarrier(tpe)}
        |var r: ${recSyntax.tpe(tpe)} = ${recSyntax.create(tpe, args)}
        |""".stripMargin
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |def access_f$input = ${recSyntax.access("r", s"f$input", "Int")}
        |""".stripMargin
  }
}


// Java Version

case object JavaRTAccessFields extends JavaRTBenchmark with RTAccessFields {

  def state(recSyntax: RecordSyntax): String = {
    val tpe = types(0)
    val args = (1 to inputs.max).map { idx => (s"f$idx", s"$idx") }

    // let the accessed record be a mutable var in benchmarking state to prevent constant folding
    s"${recSyntax.tpe(tpe)} r = ${recSyntax.create(tpe, args)};"
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |public int access_f$input() throws Exception {
        |  return ${recSyntax.access("r", s"f$input", "Int")};
        |}""".stripMargin
  }
}
