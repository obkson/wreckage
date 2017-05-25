package wreckage.builder
package benchmarking

trait Benchmark extends {

  def sourceFile(pkg: Seq[String], recSyntax: RecordSyntax): SourceFile = {
    SourceFile(pkg, s"$name.$filename_extension", source(pkg, recSyntax))
  }

  /* Implementations of this trait must provide: */
  def name: String
  def filename_extension: String
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String

}
