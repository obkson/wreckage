
def field_class(size: Int): String = {
  val fields = (1 to size).map{ idx => s"f$idx" }.mkString("",",",";")
  val constructor_params = (1 to size).map{ idx => s"int _f$idx" }.mkString(",")
  val constructor_assign = (1 to size).map{ idx => s"f$idx = _f$idx" }.mkString("",";",";")

  s"""|package javanative;
      |
      |public class FieldClass$size {
      |  public int $fields
      |
      |  public FieldClass$size($constructor_params) {
      |    $constructor_assign
      |  }
      |}""".stripMargin
}

val src = field_class(args(0).toInt)
println(src)
