package wreckage.builder.benchmarking


// Scala Version

trait ScalaRTAccessFields extends ScalaRTBenchmark {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
    // let the accessed record be a mutable var in benchmarking state to prevent constant folding
    s"""|${recSyntax.tpeCarrier(fields)}
        |var r = ${recSyntax.create(fields)}
        |""".stripMargin
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |def access_f$input = ${methodBody(input, recSyntax)}
        |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    recSyntax.access("r", s"f$input")
  }
}

case object ScalaRTAccessFields extends ScalaRTAccessFields


// Java Version

trait JavaRTAccessFields extends JavaRTBenchmark {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
    s"""${recSyntax.tpe(fields)} r = ${recSyntax.create(fields)};"""
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
