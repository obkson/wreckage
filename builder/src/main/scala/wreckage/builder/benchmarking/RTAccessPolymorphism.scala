package wreckage.builder.benchmarking

trait ScalaRTAccessPolymorphism extends ScalaRTBenchmark {
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

trait WhiteoakRTAccessPolymorphism extends WhiteoakRTBenchmark {
  val name = "RTAccessPolymorphism"

  override def imports(recSyntax: RecordSyntax)
    = super.imports(recSyntax) + "\n" + "import java.util.ArrayList;"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    // Create a record with 8 fields
    val fields: Seq[(String, String)] = (1 to 8).map{ idx =>
      (s"f$idx", s"$idx")
    }
    val rec = recSyntax.create(fields)
    val recTpe = recSyntax.tpe(fields)

    val decl = s"""
         |ArrayList<$recTpe> rs;
         |int i;
         |""".stripMargin

    val prepare = s"""
      |@Setup(Level.Trial)
      |public void prepare() {
      |  rs = new ArrayList<$recTpe>(${inputs.max});
      |  ${(1 to inputs.max).map( _ => s"""rs.add($rec);""" ).mkString("\n")}
      |  i = -1;
      |}
      |""".stripMargin

    decl + prepare
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |public int poly_deg$input() ${methodBody(input, recSyntax)}
       |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    s"""{
       |  i = (i + 1) % ${input};
       |  return ${recSyntax.access("rs.get(i)", s"f8")};
       |}
       |""".stripMargin
  }
}

case object WhiteoakRTAccessPolymorphism extends WhiteoakRTAccessPolymorphism
