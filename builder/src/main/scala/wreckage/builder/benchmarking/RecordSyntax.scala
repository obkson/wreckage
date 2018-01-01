package wreckage.builder.benchmarking

trait RecordSyntax {
  def imports: Seq[String]
  def decl(name: String, fields: Seq[(String, String)]): String
  def create(name: String, fields: Seq[(String, String)]): String
  def tpe(name: String, fields: Seq[(String, String)]): String
  def tpeCarrier(fields: Seq[(String, String)]) = ""
  def access(prefix: String, field: String): String
  def increment(prefix: String, field: String)
}
