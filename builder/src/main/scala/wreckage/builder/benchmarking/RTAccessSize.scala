package wreckage.builder.benchmarking


// Scala Version

trait ScalaRTAccessSize extends ScalaRTBenchmark {
  val name = "RTAccessSize"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    inputs.map{input =>
      val fields: Seq[(String, String)] = (1 to input).map{ idx =>
        (s"f$idx", s"$idx")
      }
      s"""val r_$input = ${recSyntax.create(fields)}"""
    }.mkString("\n")
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |def access_f$input = ${recSyntax.access(s"r_$input", s"f$input")}
       |""".stripMargin
  }

}

case object ScalaRTAccessSize extends ScalaRTAccessSize


// Java Version

trait JavaRTAccessSize extends JavaRTBenchmark {
  val name = "RTAccessSize"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    inputs.map{input =>
      val fields: Seq[(String, String)] = (1 to input).map{ idx =>
        (s"f$idx", s"$idx")
      }
      s"""${recSyntax.tpe(fields)} r_$input = ${recSyntax.create(fields)};"""
    }.mkString("\n")
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""|@Benchmark
        |public int access_f$input() throws Exception {
        |  return ${recSyntax.access(s"r_$input", s"f$input")};
        |}""".stripMargin
  }

}

case object JavaRTAccessSize extends JavaRTAccessSize
