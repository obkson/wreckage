package wreckage.builder
package benchmarking


// Scala Version

case object ScalaRTCreationSize extends ScalaRTBenchmark {
  val name = "RTCreationSize"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  // one record type for each input size
  def types = inputs map typeForInput

  def typeForInput(input: Int): RecordType = {
    val fields = (1 to input) map { idx => (s"f$idx", "Int") }
    RecordType(s"RTCreationSize_Rec$input", None, fields)
  }

  // put a mutable value in benhmarking state...
  def state(recSyntax: RecordSyntax): String = "var x = 1"

  def method(input: Int, recSyntax: RecordSyntax): String = {
    def body(input: Int): String = {
      // ... and let the record depend on it to avoid constant folding
      val fields = ("f1", "x") :: (2 to input).map{ idx =>
        (s"f$idx", s"$idx")
      }.toList
      val recTpe = typeForInput(input)
      recSyntax.create(recTpe, fields)
    }

    // return the created record to prevent dead code elimination
    s"""|@Benchmark
        |def create_f$input = ${body(input)}
        |""".stripMargin
  }
}
