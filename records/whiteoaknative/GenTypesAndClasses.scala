
def getter_class(size: Int): String = {
  val fields = (1 to size).map{ idx => s"_f$idx" }.mkString("",",",";")
  val constructor_params = (1 to size).map{ idx => s"int f$idx" }.mkString(",")
  val constructor_assign = (1 to size).map{ idx => s"_f$idx = f$idx" }.mkString("",";",";")
  val getters = (1 to size).map{ idx =>
    s"  public int f$idx() {return _f$idx;}"
  }.mkString("\n")

  s"""|public class RecordClass$size {
      |  private int $fields
      |
      |  public RecordClass$size($constructor_params) {
      |    $constructor_assign
      |  }
      |
      |$getters
      |}""".stripMargin
}

def getter_class_poly(index: Int): String = {

  val es = (1 until index).map{ j => s"f$j" }
  val fs = List( "g1" )
  val gs = (1 to (32-index)).map{ j => s"h$j" }

  val fields: Seq[String] = es ++ fs ++ gs
  val private_fields = fields.map{ k => s"_$k" }.mkString("",",",";")

  val constructor_params = fields.map{ f => s"int $f" }.mkString(",")
  val constructor_assign = fields.map{ f => s"_$f = $f" }.mkString("",";",";")
  val getters = fields.map{ f =>
    s"  public int $f() {return _$f;}"
  }.mkString("\n")

  s"""|public class RecordClassPoly32_$index {
      |  private int $private_fields
      |
      |  public RecordClassPoly32_$index($constructor_params) {
      |    $constructor_assign
      |  }
      |
      |$getters
      |}""".stripMargin
}

def struct_type(size: Int): String = {
  val getters = (1 to size).map{ idx => s"public int f$idx(); "}.mkString

  s"""|public struct RecordStruct$size {
      |$getters
      |}""".stripMargin
}

def struct_type_poly(size: Int, index: Int): String = {
  val es = (1 until index).map{ j => s"f$j" }
  val fs = List( "g1" )
  val gs = (1 to (size-index)).map{ j => s"h$j" }
  val fields: Seq[String] = es ++ fs ++ gs

  val getters = fields.map{ f =>
    s"  public int $f();"
  }.mkString("\n")

  s"""|public struct RecordStructPoly${size}_${index} {
      |$getters
      |}""".stripMargin
}

val sizes = (1 :: (2 to 32 by 2).toList)

val getterClasses = sizes.map{ getter_class(_) }.mkString("\n")
val structTypes = sizes.map{ struct_type(_) }.mkString("\n")
val getterClassesPoly = (1 to 32).map{ getter_class_poly(_) }.mkString("\n")
val structTypesPoly = (1 to 1).map{ struct_type_poly(1, _) }.mkString("\n")

println(
s"""|package whiteoaknative;
    |
    |/************** RECORD TYPES *********************/
    |$structTypes
    |
    |/************** RECORD CLASSES *******************/
    |$getterClasses
    |
    |/************** RECORD TYPES POLY *********************/
    |$structTypesPoly
    |
    |/************** RECORD CLASSES POLY *******************/
    |$getterClassesPoly
    |""".stripMargin
)
