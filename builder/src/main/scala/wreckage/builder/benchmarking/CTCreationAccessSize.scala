package wreckage.builder.benchmarking

trait ScalaCTCreationAccessSize extends ScalaCTBenchmark {
  val name = "CTCreationAccessSize"

  val inputs = List(1,50,100,150,200,250)

  def snippet(input: Int, recSyntax: RecordSyntax): String = {
    val imports = recSyntax.imports.map{imp => s"import $imp"}.mkString("\n")

    val fields = (1 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }

    val access = (1 to input).map{ idx =>
      s"""  val f$idx = ${recSyntax.access("r",s"f$idx")}"""
    }.mkString("\n")

    s"""|$imports
        |class C {
        |  val r = ${recSyntax.create(fields)}
        |$access
        |}
        |""".stripMargin
  }
}

case object ScalaCTCreationAccessSize extends ScalaCTCreationAccessSize

trait ScalaCTCreationAccessLast extends ScalaCTBenchmark {
  val name = "CTCreationAccessLast"

  val inputs = List(1,10,20,30,40,50)

  def snippet(input: Int, recSyntax: RecordSyntax): String = {
    val imports = recSyntax.imports.map{imp => s"import $imp"}.mkString("\n")

    val fields = (1 to input).map{ idx =>
      (s"f$idx", s"$idx")
    }

    s"""|$imports
        |class C {
        |  val r = ${recSyntax.create(fields)}
        |  val f = ${recSyntax.access("r", s"f$input")}
        |}
        |""".stripMargin
  }
}

case object ScalaCTCreationAccessLast extends ScalaCTCreationAccessLast

