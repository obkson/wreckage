package wreckage.builder.benchmarking


// Scala Version

trait ScalaRTAccessFields extends ScalaRTBenchmark {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
      s"""|${recSyntax.tpeCarrier(fields)}
          |val r: ${recSyntax.tpe(fields)} = ${recSyntax.create(fields)}
          |""".stripMargin
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
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

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

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

trait WhiteoakRTAccessFields extends WhiteoakRTBenchmark {
  val name = "RTAccessFields"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
    s"""${recSyntax.tpe(fields)} r = ${recSyntax.create(fields)};"""
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""|@Benchmark
        |public int access_f$input() ${methodBody(input, recSyntax)}
        |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    s"""|{
        |  return ${recSyntax.access("r", s"f$input")};
        |}""".stripMargin
  }
}

case object WhiteoakRTAccessFields extends WhiteoakRTAccessFields
