package wreckage.builder.benchmarking


// Scala Version

trait ScalaRTAccessSize extends ScalaRTBenchmark {
  val name = "RTAccessSize"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  def state(recSyntax: RecordSyntax): String = {
    inputs.map{input =>
      val fields: Seq[(String, String)] = (1 to input).map{ idx =>
        (s"f$idx", s"$idx")
      }
      // let the accessed records be mutable vars in benchmarking state to prevent constant folding
      s"""|${recSyntax.tpeCarrier(fields)}
          |var r_$input = ${recSyntax.create(fields)}
          |""".stripMargin
    }.mkString("\n")
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // return accessed value to prevent dead code elimination
    s"""@Benchmark
       |def access_f$input = ${recSyntax.access(s"r_$input", s"f$input")}
       |""".stripMargin
  }

}

case object ScalaRTAccessSize extends ScalaRTAccessSize


// Java Version

trait JavaRTAccessSize extends JavaRTBenchmark {
  val name = "RTAccessSize"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

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

// Whiteoak Version

case object WhiteoakRTAccessSize extends JavaRTAccessSize {
  override def filename_extension = "wo"
}
