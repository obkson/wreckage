package wreckage.builder
package benchmarking

trait Benchmark extends {

  /* Implementations of this trait must provide: */
  def name: String
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String
  def types: Seq[RecordType]
  def dependencies: Seq[Dependency] = Nil

}
