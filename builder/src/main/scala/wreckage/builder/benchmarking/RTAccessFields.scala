package wreckage.builder.benchmarking

object RTAccessFields extends Benchmark {
  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
    s"""val r = ${recSyntax.create(fields)}"""
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |def access_f$input = ${methodBody(input, recSyntax)}
       |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    recSyntax.access("r", s"f$input")
  }
}


