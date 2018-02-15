package wreckage.builder
package benchmarking


// Scala Version

case object ScalaRTUpdateSize extends ScalaRTBenchmark {
  val name = "RTUpdateSize"

  val inputs: Seq[Int] = List(33,66,100)

  // one record type for each input size
  def types = inputs map typeForInput

  def typeForInput(input: Int): RecordType = {
    val fields = (1 to input) map { idx => (s"f$idx", "Int") }
    RecordType(s"RTUpdateSize_Rec$input", None, fields)
  }

  def state(recSyntax: RecordSyntax): String = {
    inputs.map{input =>
      val args = (1 to input).map { idx => (s"f$idx", s"$idx") }
      // let the accessed records be mutable vars in benchmarking state to prevent constant folding
      s"""|
          |var r_$input = ${recSyntax.create(typeForInput(input), args)}
          |""".stripMargin
    }.mkString("\n")
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {

    def body(input: Int) = recSyntax.increment(s"r_$input", s"f$input")
    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |def update_f$input = ${body(input)}
        |""".stripMargin
  }
}

