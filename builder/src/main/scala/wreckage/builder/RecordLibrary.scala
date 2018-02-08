package wreckage.builder

trait RecordLibrary {
  def name: String
  def pkg: List[String]
  def output: Dependency
  def decl(tpe: RecordType): String
}
