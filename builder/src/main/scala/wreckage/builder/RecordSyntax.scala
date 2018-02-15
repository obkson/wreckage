package wreckage.builder

trait RecordSyntax {
  def dependencies: Seq[Dependency]
  def imports: Seq[String]
  def tpeCarrier(tpe: RecordType): String = ""
  def tpe(tpe: RecordType): String
  def create(tpe: RecordType, fields: Seq[(String, String)]): String
  def access(prefix: String, label: String, tpe: String): String
  def increment(prefix: String, field: String): String
}
