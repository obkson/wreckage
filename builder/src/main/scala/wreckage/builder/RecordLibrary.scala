package wreckage.builder

trait RecordLibrary {
  def name: String
  def pkg: List[String]
  def output: Dependency
  def decl(tpe: RecordType): String
  def baseDecl(tpe: RecordType): Option[String]
  def fieldDecl(label: String, tpe: String): Option[String]
}
