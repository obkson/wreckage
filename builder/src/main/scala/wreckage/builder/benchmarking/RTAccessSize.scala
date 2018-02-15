package wreckage.builder
package benchmarking


trait RTAccessSize {
  val name = "RTAccessSize"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  // one record type for each input size
  def types = inputs map typeForInput

  def typeForInput(input: Int): RecordType = {
    val fields = (1 to input) map { idx => (s"f$idx", "Int") }
    RecordType(s"RTAccessSize_Rec$input", None, fields)
  }
}

// Scala Version

case object ScalaRTAccessSize extends ScalaRTBenchmark with RTAccessSize {

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
    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |def access_f$input = ${recSyntax.access(s"r_$input", s"f$input")}
        |""".stripMargin
  }
}


// Java Version

case object JavaRTAccessSize extends JavaRTBenchmark with RTAccessSize {

  def state(recSyntax: RecordSyntax): String = {
    inputs.map{input =>
      val tpe = typeForInput(input)
      val args = (1 to input).map { idx => (s"f$idx", s"$idx") }
      s"${recSyntax.tpe(tpe)} r_$input = ${recSyntax.create(tpe, args)};"
    }.mkString("\n")
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // return accessed value to prevent dead code elimination
    s"""|@Benchmark
        |public int access_f$input() throws Exception {
        |  return ${recSyntax.access(s"r_$input", s"f$input")};
        |}""".stripMargin
  }
}
