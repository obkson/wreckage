package wreckage.builder.benchmarking

trait RecordSyntax {
  def imports: Seq[String]
  def create(fields: Seq[(String, String)]): String
  def access(prefix: String, field: String): String
}


