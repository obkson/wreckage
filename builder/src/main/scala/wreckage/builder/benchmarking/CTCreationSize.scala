package wreckage.builder.benchmarking

trait ScalaCTCreationSize extends ScalaCTBenchmark {
  val name = "CTCreationSize"

  val inputs = List(1,50,100,150,200,250)

  def snippet(input: Int, recSyntax: RecordSyntax): String = {
    val imports = recSyntax.imports.map{imp => s"import $imp"}.mkString("\n")

    val fields: Seq[(String, String)] = (1 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }

    s"""|$imports
        |class C {
        |  val r = ${recSyntax.create(s"Rec${fields.size}", fields)}
        |}
        |""".stripMargin
  }
}

case object ScalaCTCreationSize extends ScalaCTCreationSize
