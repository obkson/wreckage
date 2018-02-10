package wreckage.builder
package benchmarking

// Scala version

case object ScalaRTAccessPolymorphism extends ScalaRTBenchmark {
  val name = "RTAccessPolymorphism"

  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def types = (1 to inputs.max) map { index => subType(index) }

  def baseType = RecordType(s"RTAccessPolymorphism_Base", None, List(("f", "Int")))
  def subType(index: Int): RecordType = {
    val fields: Seq[(String, String)] =
      ((1 until index).map( idx => (s"g$idx", "Int") )) ++
      List( ("f", "Int") ) ++
      ((index+1 to inputs.max).map( idx => (s"g$idx", "Int") ))
    RecordType(s"RTAccessPolymorphism_SubType$index", Some(baseType), fields)
  }

  def state(recSyntax: RecordSyntax): String = {
    // Create an Array containing records of various subtypes of the base type with field "f: Int"
    val rs = (1 to inputs.max).map { index => 
      val tpe = subType(index)
      val args: Seq[(String, String)] =
        ((1 until index) map { idx => (s"g$idx", s"$idx") }) ++
        List( ("f", s"$index") ) ++
        ((index+1 to inputs.max) map { idx => (s"g$idx", s"$idx") })
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
       |  ${recSyntax.access("rs(i)", s"f")}
       |}
       |""".stripMargin
  }
}
