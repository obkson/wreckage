package wreckage.builder.benchmarking


// Scala / Dotty Version

trait ScalaRTCreationSize extends ScalaRTBenchmark {
  val name = "RTCreationSize"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  // put a mutable value in benhmarking state...
  def state(recSyntax: RecordSyntax): String = "var x = 1"

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // ... and let the record depend on it to avoid constant folding
    val fields: Seq[(String, String)] = ("f1", "x") :: (2 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }.toList

    val rec = recSyntax.create(fields)

    // return the created record to prevent dead code elimination
    s"""@Benchmark
       |def create_f$input = $rec
       |""".stripMargin
  }
}

case object ScalaRTCreationSize extends ScalaRTCreationSize


// Java Version

trait JavaRTCreationSize extends JavaRTBenchmark {
  val name = "RTCreationSize"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  // put a mutable value in benhmarking state...
  def state(recSyntax: RecordSyntax): String = "int x = 1;"

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // ... and let the record depend on it to avoid constant folding
    val fields: Seq[(String, String)] = ("f1", "x") :: (2 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }.toList

    val tpe = recSyntax.tpe(fields)
    val rec = recSyntax.create(fields)

    // return the created record to prevent dead code elimination
    s"""@Benchmark
       |public $tpe create_f$input() {
       |  return $rec;
       |}""".stripMargin
  }
}
case object JavaRTCreationSize extends JavaRTCreationSize

// Whiteoak Version

case object WhiteoakRTCreationSize extends JavaRTCreationSize {
  override def filename_extension = "wo"
}

