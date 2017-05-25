package wreckage.builder
package benchmarking

trait CTBenchmark extends Benchmark {

  /* Compile Time Benchmark specific source generator */
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{name}}"          -> name,
          "{{snippets}}"      -> snippets(recSyntax),
          "{{methods}}"       -> methods)
    )
    src
  }

  /* implementations of this trait must provide: */
  def template: String
  def filename_extension: String
  def name: String
  def inputs: Seq[Int]
  def snippets(recSyntax: RecordSyntax): String
  def methods: String

}

trait ScalaCTBenchmark extends CTBenchmark {
  val template = "/JMHScalaCTTemplate.scala"
  val filename_extension = "scala"

  def snippets(recSyntax: RecordSyntax) = {
    inputs.map{ input =>
      s"""|val snippet_s$input = List(new BatchSourceFile("<stdin>", \"\"\"
          |${snippet(input, recSyntax) }
          |\"\"\" ))
          |""".stripMargin
      }.mkString("\n")
  }

  def methods = inputs.map{ input =>
      s"""|@Benchmark
          |def compile_s$input = run.compileSources(snippet_s$input)
          |""".stripMargin
    }.mkString("\n")

  /* implementations of this trait must still provide: */
  def name: String
  def inputs: Seq[Int]
  def snippet(input: Int, recSyntax: RecordSyntax): String
}


/*
case class CTCreate(override val pig: GuineaPig) extends CompileTimeBenchmark {
  override def inputSeq = 5 to 30 by 5

  override def source(input: Int): String = {
    val multiplier = 1
    val fields = (1 to input) map (x => (s"f$x", x))
    val records = (0 until multiplier) map {x => 
      s"""val rec${x} = ${pig.create(fields)}"""
    }

    s"""|${pig.importStatements}
        |
        |object A {
        |  ${records.mkString("\n  ")}
        |}
        |""".stripMargin
  }
}

case class CTCreateAndAccess(override val pig: GuineaPig) extends CompileTimeBenchmark {
  override def inputSeq = List(1,2,3,4,5,10,20,35)

  override def source(input: Int): String = {
    val multiplier = 1
    val fields = (1 to inputSeq.max) map (x => (s"f$x", x))
    val accesses = (0 until multiplier) map {x =>
      s"val v${x} = ${pig.access("r", s"f${input}")}"
    }

    s"""|${pig.importStatements}
        |
        |object A {
        |  def f() {
        |    val r = ${pig.create(fields)}
        |    ${accesses.mkString("\n    ")}
        |  }
        |}
        |""".stripMargin
  }
}

case class CTFromCaseClassAndAccess(override val pig: GuineaPig) extends CompileTimeBenchmark {
  override def inputSeq = List(1,10,20,30)

  override def source(input: Int): String = {
    val multiplier = 1
    val fields = (1 to inputSeq.max) map (x => (s"f$x", x))
    val accesses = (0 until multiplier) map {x =>
      s"val v${x} = ${pig.access("r", s"f${input}")}"
    }

    s"""|${pig.importStatements}
        |import caseclass._
        |
        |object A {
        |  def f() {
        |    val lgen = LabelledGeneric[CaseClass${input}]
        |    val c = new CaseClass${input}(${(1 to input) map (_ => "1") mkString(",")})
        |    val r = lgen.to(c)
        |    val v = r('f${input})
        |    //r.updated('f${input}, ${input})
        |  }
        |}
        |""".stripMargin
  }
}

case class CTImport(override val pig: GuineaPig) extends CompileTimeBenchmark {
  override def inputSeq = 5 to 30 by 5

  override def lib = pig.lib

  override def globals = {
    val records = inputSeq map {input =>
      val fields = (1 to input) map (x => (s"f$x", x))
      s"""val rec${input} = ${pig.create(fields)}"""
    }

    s"""|${pig.importStatements}
        |${records.mkString("\n")}
        |""".stripMargin
  }

  override def source(input: Int): String = {
    val multiplier = 10
    val records = (0 until multiplier) map {x => 
      s"""val rec${x} = ${packagePath}.${name}.rec${input}"""
    }

    s"""|${pig.importStatements}
        |
        |object A {
        |  ${records.mkString("\n  ")}
        |}
        |""".stripMargin
  }
}

case class CTImportAndAccess(override val pig: GuineaPig) extends CompileTimeBenchmark {
  override def inputSeq = 5 to 30 by 5

  override def lib = pig.lib
  override def globals = {
    val fields = (1 to inputSeq.max) map (x => (s"f$x", x))

    s"""|${pig.importStatements}
        |val rec = ${pig.create(fields)}
        |""".stripMargin
  }

  override def source(input: Int): String = {
    val multiplier = 10
    val accesses = (0 until multiplier) map {x =>
      s"val v${x} = ${pig.access("r", s"f${input}")}"
    }

    s"""|${pig.importStatements}
        |
        |object A {
        |  def f() {
        |    val r = ${packagePath}.${name}.rec
        |    ${accesses.mkString("\n    ")}
        |  }
        |}
        |""".stripMargin
  }*/
