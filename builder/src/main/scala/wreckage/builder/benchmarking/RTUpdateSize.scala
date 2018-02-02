package wreckage.builder.benchmarking


// Scala Version

object ScalaRTUpdateSize extends ScalaRTBenchmark {
  val name = "RTUpdateSize"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  def state(recSyntax: RecordSyntax): String = {
    inputs.map{input =>
      val fields: Seq[(String, String)] = (1 to input).map{ idx =>
        (s"f$idx", s"$idx")
      }
      // let the accessed records be mutable vars in benchmarking state to prevent constant folding
      s"""|${recSyntax.decl(s"Rec${fields.size}", fields.map(f => (f._1, "Int")))}
          |var r_$input = ${recSyntax.create(s"Rec${fields.size}", fields)}
          |""".stripMargin
    }.mkString("\n")
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    // return accessed value to prevent dead code elimination
    s"""@Benchmark
       |def update_f$input = ${recSyntax.increment(s"r_$input", s"f$input")}
       |""".stripMargin
  }
}

// Java Version TODO
