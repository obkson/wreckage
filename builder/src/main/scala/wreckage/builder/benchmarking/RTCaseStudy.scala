package wreckage.builder
package benchmarking

/* Currently only for Scala / Dotty */
trait ScalaRTCaseStudy extends Benchmark{

  /* Real Time Benchmark specific source generator */
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {

    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{imports}}"       -> imports(recSyntax),
          "{{declarations}}"  -> declarations(recSyntax),
          "{{create_author}}" -> createAuthor(recSyntax),
          "{{create_commit}}" -> createCommit(recSyntax),
          "{{create_commit_event}}" -> createCommitEvent(recSyntax),
          "{{method_body}}"   -> methodBody(recSyntax)
    )
    src
  }

  def name = "RTCaseStudy"
  def filename_extension = "scala"
  def template = "/JMHScalaRTCaseStudyTemplate.scala"

  def imports(recSyntax: RecordSyntax) = recSyntax.imports.map{imp => s"import $imp"}.mkString("\n")

  def declarations(recSyntax: RecordSyntax) = {
    val authorFields = ("email", "String") :: ("date", "Instant") :: Nil
    val commitFields = ("author", "Author") :: Nil
    val commitEventFields = ("sha", "String") :: ("commit", "Commit") :: Nil

    s"""|${recSyntax.decl("Author", authorFields)}
        |${recSyntax.decl("Commit", commitFields)}
        |${recSyntax.decl("CommitEvent", commitEventFields)}""".stripMargin
  }

  def create_author(recSyntax: RecordSyntax) = 

  def methodBody(recSyntax: RecordSyntax) = {
    s"""|commits.foreach(obj => {
        |  val email = ${recSyntax.access(recSyntax.access(recSyntax.access("obj", "commit"), "author"), "email")}
        |  val dow = ${recSyntax.access(recSyntax.access(recSyntax.access("obj", "commit"), "author"), "date")}
        |    .atZone(ZoneId.systemDefault())
        |    .getDayOfWeek().getValue()
        |  val oldStats = table.getOrElse(email, UserStats(email, 0, 0, 0, 0, 0, 0, 0))
        |  val newStats = dow match {
        |    case 1 => oldStats.copy(monday=oldStats.monday + 1)
        |    case 2 => oldStats.copy(tuesday=oldStats.tuesday + 1)
        |    case 3 => oldStats.copy(wednesday=oldStats.wednesday + 1)
        |    case 4 => oldStats.copy(thursday=oldStats.thursday + 1)
        |    case 5 => oldStats.copy(friday=oldStats.friday + 1)
        |    case 6 => oldStats.copy(saturday=oldStats.saturday + 1)
        |    case 7 => oldStats.copy(sunday=oldStats.sunday + 1)
        |  }
        |  table += email->newStats
        |})""".stripMargin
}
