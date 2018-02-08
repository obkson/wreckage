package wreckage.builder
package benchmarking


// Scala Version

case object ScalaRTAccessFields extends ScalaRTBenchmark {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def types = {
    val numFields = inputs.max
    val fields = (1 to numFields).map { idx => (s"f$idx", "Int") }
    List(
      RecordType(s"RTAccessFields_Rec$numFields", None, fields)
    )
  }

  def state(recSyntax: RecordSyntax): String = {
    val tpe = types(0)
    val fields = (1 to inputs.max).map { idx => (s"f$idx", s"$idx") }

    // let the accessed record be a mutable var in benchmarking state to prevent constant folding
    s"var r = ${recSyntax.create(tpe, fields)}"
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {

    def body(input: Int): String = recSyntax.access("r", s"f$input")

    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |def access_f$input = ${body(input)}
        |""".stripMargin
  }

}

/*
// Java Version

trait JavaRTAccessFields extends JavaRTBenchmark {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
    s"""${recSyntax.tpe(s"Rec${fields.size}", fields)} r = ${recSyntax.create(s"Rec${fields.size}", fields)};"""
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""|@Benchmark
        |public int access_f$input() throws Exception {
        |  return ${recSyntax.access("r", s"f$input")};
        |}""".stripMargin
  }
}
case object JavaRTAccessFields extends JavaRTAccessFields


// Whiteoak Version

case object WhiteoakRTAccessFields extends JavaRTAccessFields {
  override def filename_extension = "wo"
}
*/
