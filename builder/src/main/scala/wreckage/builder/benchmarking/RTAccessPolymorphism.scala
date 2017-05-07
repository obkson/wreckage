package wreckage.builder.benchmarking

trait ScalaRTAccessPolymorphism extends ScalaBenchmark {
  val name = "RTAccessPolymorphism"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    // Create a record with 8 fields
    val fields: Seq[(String, String)] = (1 to 8).map{ idx =>
      (s"f$idx", s"$idx")
    }
    val rec = recSyntax.create(fields)

    // Create an Array of such records
    val rs = (1 to inputs.max).map( _ => rec )
      .mkString("Array(\n  ", ",\n  ",")")

    val ret = s"""
      |val rs = $rs
      |var i = -1
      |""".stripMargin
    ret
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |def poly_deg$input = ${methodBody(input, recSyntax)}
       |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    s"""{
       |  i = (i + 1) % ${input}
       |  ${recSyntax.access("rs(i)", s"f8")}
       |}
       |""".stripMargin
  }
}

case object ScalaRTAccessPolymorphism extends ScalaRTAccessPolymorphism
