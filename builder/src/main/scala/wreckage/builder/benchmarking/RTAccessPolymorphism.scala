package wreckage.builder.benchmarking

trait ScalaRTAccessPolymorphism extends ScalaRTBenchmark {
  val name = "RTAccessPolymorphism"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  def state(recSyntax: RecordSyntax): String = {
    val baseFields = ("g1", "1") :: Nil

    // Create an Array of such records
    val rs = (1 to inputs.max).map{ i => 

      val es = (1 until i).map{ j =>
        (s"f$j", s"$j")
      }
      val fs = List( ("g1", s"$i") )
      val gs = (1 to (inputs.max-i)).map{ j =>
        (s"h$j", s"${i+j}")
      }
      val fields: Seq[(String, String)] = es ++ fs ++ gs

      recSyntax.create(s"Rec${fields.size}", fields)

    }.mkString(s"Array[${recSyntax.tpe("Base", baseFields)}](\n  ", ",\n  ",")")

    val ret = s"""
      |${recSyntax.decl("Base", baseFields)} // e.g. Compossible can place a type carrier here
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
       |  ${recSyntax.access("rs(i)", s"g1")}
       |}
       |""".stripMargin
  }
}

case object ScalaRTAccessPolymorphism extends ScalaRTAccessPolymorphism


// Java Version

trait JavaRTAccessPolymorphism extends JavaRTBenchmark {
  val name = "RTAccessPolymorphism"

  val inputs: Seq[Int] = List(1,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32)

  override def imports(recSyntax: RecordSyntax)
    = super.imports(recSyntax) + "\n" + "import java.util.ArrayList;"

  def state(recSyntax: RecordSyntax): String = {

    val lubDecl = recSyntax.decl("Base", List( ("g1","1") ))
    val lub = recSyntax.tpe("Base", List( ("g1","1") ))

    val rs = (1 to inputs.max).map{ i => 

      val es = (1 until i).map{ j =>
        (s"f$j", s"$j")
      }
      val fs = List( ("g1", s"$i") )
      val gs = (1 to (inputs.max-i)).map{ j =>
        (s"h$j", s"${i+j}")
      }
      val fields: Seq[(String, String)] = es ++ fs ++ gs

      recSyntax.create(s"Rec${fields.size}", fields)
    }

    val decl = s"""
         |$lubDecl
         |ArrayList<$lub> rs;
         |int i;
         |""".stripMargin

    val prepare = s"""
      |@Setup(Level.Trial)
      |public void prepare() {
      |  rs = new ArrayList<$lub>(${inputs.max});
      |  ${rs.map( rec => s"""rs.add($rec);""" ).mkString("\n  ")}
      |  i = -1;
      |}
      |""".stripMargin

    decl + prepare
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |public int poly_deg$input() throws Exception {
       |  i = (i + 1) % ${input};
       |  return ${recSyntax.access("rs.get(i)", s"g1")};
       |}
       |""".stripMargin
  }
}
case object JavaRTAccessPolymorphism extends JavaRTAccessPolymorphism

// Whiteoak Version

case object WhiteoakRTAccessPolymorphism extends JavaRTAccessPolymorphism {
  override def filename_extension = "wo"
}


