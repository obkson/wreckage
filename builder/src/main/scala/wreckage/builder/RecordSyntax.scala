package wreckage.builder

trait RecordSyntax {
  def dependencies: Seq[Dependency]
  def imports: Seq[String]
  def tpe(tpe: RecordType): String
  def create(tpe: RecordType, fields: Seq[(String, String)]): String
  def access(prefix: String, field: String): String
  def increment(prefix: String, field: String): String
}