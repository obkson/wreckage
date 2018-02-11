package wreckage.builder
package benchmarking

// Scala / Dotty version

case object ScalaRTAccessPolymorphism extends ScalaRTBenchmark {
  val name = "RTAccessPolymorphism"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def types = (1 to inputs.max) map { index => subType(index) }

  def baseType = RecordType(s"RTAccessPolymorphism_Base", None, List(("g", "Int")))
  def subType(index: Int): RecordType = {
    val fields: Seq[(String, String)] =
      ((1 until index).map( idx => (s"f$idx", "Int") )) ++
      List( ("g", "Int") ) ++
      ((index+1 to inputs.max).map( idx => (s"f$idx", "Int") ))
    RecordType(s"RTAccessPolymorphism_SubType$index", Some(baseType), fields)
  }

  def state(recSyntax: RecordSyntax): String = {
    // Create an Array containing records of various subtypes of the base type with field "f: Int"
    val rs = (1 to inputs.max).map { index =>
      val tpe = subType(index)
      val args: Seq[(String, String)] =
        ((1 until index) map { idx => (s"f$idx", s"$idx") }) ++
        List( ("g", s"$index") ) ++
        ((index+1 to inputs.max) map { idx => (s"f$idx", s"$idx") })
      recSyntax.create(tpe, args)
    }.mkString(s"Array[${recSyntax.tpe(baseType)}](\n  ", ",\n  ",")")

    s"""${recSyntax.tpeCarrier(baseType)} // e.g. Compossible can place a type carrier here
       |val rs = $rs
       |var i = -1
       |""".stripMargin
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |def poly_deg$input = {
       |  i = (i + 1) % ${input}
       |  ${recSyntax.access("rs(i)", s"g")}
       |}
       |""".stripMargin
  }
}

// Java version

case object JavaRTAccessPolymorphism extends JavaRTBenchmark {
  val name = "RTAccessPolymorphism"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  override def imports(recSyntax: RecordSyntax)
    = super.imports(recSyntax) + "\n" + "import java.util.ArrayList;"

  def types = (1 to inputs.max) map { index => subType(index) }

  def baseType = RecordType(s"RTAccessPolymorphism_Base", None, List(("g", "Int")))
  def subType(index: Int): RecordType = {
    val gpref = (1 until index) map { idx => (s"f$idx", "Int") }
    val field = List( ("g", "Int") )
    val gpost = (index+1 to inputs.max) map { idx => (s"f$idx", "Int") }
    val fields: Seq[(String, String)] = gpref ++ field ++ gpost
    RecordType(s"RTAccessPolymorphism_SubType$index", Some(baseType), fields)
  }

  def state(recSyntax: RecordSyntax): String = {
    // Create an Array containing records of various subtypes of the base type with field "f: Int"
    val rs = (1 to inputs.max).map { index =>
      val gpref = (1 until index) map { idx => (s"f$idx", s"$idx") }
      val field = List( ("g", s"$index") )
      val gpost = (index+1 to inputs.max) map { idx => (s"f$idx", s"$idx") }
      val tpe = subType(index)
      val args: Seq[(String, String)] = gpref ++ field ++ gpost
      recSyntax.create(tpe, args)
    }

    s"""|${recSyntax.tpeCarrier(baseType)} // Place type carrier here
        |ArrayList<${recSyntax.tpe(baseType)}> rs;
        |int i;
        |
        |@Setup(Level.Trial)
        |public void prepare() {
        |  rs = new ArrayList<${recSyntax.tpe(baseType)}>(${inputs.max});
        |${rs.map{ r => s"  rs.add($r);" }.mkString("\n")}
        |  i = -1;
        |}
        |""".stripMargin
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |public int poly_deg$input() throws Exception {
       |  i = (i + 1) % $input;
       |  return ${recSyntax.access("rs.get(i)", s"g")};
       |}
       |""".stripMargin
  }
}
