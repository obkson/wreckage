package wreckage.builder.benchmarking


// Scala / Dotty Version

trait ScalaRTCreationFields extends ScalaBenchmark {
  val name = "RTCreationFields"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = ""

  def method(input: Int, recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }
    val rec = recSyntax.create(fields)

    s"""@Benchmark
       |def create_f$input = $rec
       |""".stripMargin
  }
}

case object ScalaRTCreationFields extends ScalaRTCreationFields


// Whiteoak Version

trait WhiteoakRTCreationFields extends WhiteoakBenchmark {
  val name = "RTCreationFields"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = ""

  def method(input: Int, recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }
    val tpe = recSyntax.tpe(fields)
    val rec = recSyntax.create(fields)

    s"""@Benchmark
       |public $tpe create_f$input() {
       |  return $rec;
       |}""".stripMargin
  }

}

case object WhiteoakRTCreationFields extends WhiteoakRTCreationFields

