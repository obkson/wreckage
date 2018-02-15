package wreckage.builder
package benchmarking

trait RTCreationSize {
  val name = "RTCreationSize"
  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  // one record type for each input size
  def types = inputs map typeForInput

  def typeForInput(input: Int): RecordType = {
    val fields = (1 to input) map { idx => (s"f$idx", "Int") }
    RecordType(s"RTCreationSize_Rec$input", None, fields)
  }
}

// Scala / Dotty Version

case object ScalaRTCreationSize extends ScalaRTBenchmark with RTCreationSize {

  // put a mutable value in benhmarking state...
  def state(recSyntax: RecordSyntax): String = "var x = 1"

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // ... and let the record depend on it to avoid constant folding
    val args = ("f1", "x") :: (2 to input).map{ idx => (s"f$idx", s"$idx") }.toList
    val recTpe = typeForInput(input)
    val rec = recSyntax.create(recTpe, args)

    // return the created record to prevent dead code elimination
    s"""|@Benchmark
        |def create_f$input = $rec
        |""".stripMargin
  }
}


// Java Version

case object JavaRTCreationSize extends JavaRTBenchmark with RTCreationSize {

  // put a mutable value in benhmarking state...
  def state(recSyntax: RecordSyntax): String = "int x = 1;"

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // ... and let the record depend on it to avoid constant folding
    val args = ("f1", "x") :: (2 to input).map{ idx => (s"f$idx", s"$idx") }.toList
    val recTpe = typeForInput(input)
    val tpe = recSyntax.tpe(recTpe)
    val rec = recSyntax.create(recTpe, args)

    // return the created record to prevent dead code elimination
    s"""@Benchmark
       |public $tpe create_f$input() {
       |  return $rec;
       |}""".stripMargin
  }
}
