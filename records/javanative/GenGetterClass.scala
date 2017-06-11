
def getter_class(size: Int): String = {
  val fields = (1 to size).map{ idx => s"_f$idx" }.mkString("",",",";")
  val constructor_params = (1 to size).map{ idx => s"int f$idx" }.mkString(",")
  val constructor_assign = (1 to size).map{ idx => s"_f$idx = f$idx" }.mkString("",";",";")
  val getters = (1 to size).map{ idx =>
    s"  public int f$idx() {return _f$idx;}"
  }.mkString("\n")

  s"""|package javanative;
      |
      |public class GetterClass$size {
      |  private int $fields
      |
      |  public GetterClass$size($constructor_params) {
      |    $constructor_assign
      |  }
      |
      |$getters
      |}""".stripMargin
}

val src = getter_class(args(0).toInt)
println(src)
