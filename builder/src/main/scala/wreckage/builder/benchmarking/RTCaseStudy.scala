package wreckage.builder
package benchmarking

/* Currently only for Scala / Dotty */
trait ScalaRTCaseStudy extends Benchmark{

  def name = "RTCaseStudy"
  def filename_extension = "scala"
  def template = "/JMHScalaRTCaseStudyTemplate.scala"

  case class TypeDeclaration(name: String, fields: List[(String, String)])

  val commitEvent = TypeDeclaration("CommitEvent",
    ("sha", "String") ::
    ("url", "String") ::
    ("html_url", "String") ::
    ("comments_url", "String") ::
    ("author", "Option[User]") ::
    ("committer", "Option[User]") ::
    ("parents", "List[ShortCommit]") ::
    ("commit", "Commit") ::
    Nil
  )
  val commit = TypeDeclaration("Commit",
    ("committer", "CommitIdentity") ::
    ("message", "String") ::
    ("tree", "Tree") ::
    ("url", "String") ::
    ("comment_count", "Int") ::
    ("verification", "Verification") ::
    ("author", "CommitIdentity") ::
    Nil
  )
  val commitIdentity = TypeDeclaration("CommitIdentity",
    ("name", "String") ::
    ("email", "String") ::
    ("date", "Instant") ::
    Nil
  )
  val tree = TypeDeclaration("Tree",
    ("sha", "String") ::
    ("url", "String") ::
    Nil
  )
  val verification = TypeDeclaration("Verification",
    ("verified", "Boolean") ::
    ("reason", "String") ::
    ("signature", "Option[String]") ::
    ("payload", "Option[String]") ::
    Nil
  )
  val user = TypeDeclaration("User",
    ("login", "String") ::
    ("id", "Long") ::
    ("avatar_url", "String") ::
    ("gravatar_id", "String") ::
    ("url", "String") ::
    ("html_url", "String") ::
    ("followers_url", "String") ::
    ("following_url", "String") ::
    ("gists_url", "String") ::
    ("starred_url", "String") ::
    ("subscriptions_url", "String") ::
    ("organizations_url", "String") ::
    ("repos_url", "String") ::
    ("events_url", "String") ::
    ("received_events_url", "String") ::
    ("site_admin", "Boolean") ::
    Nil
  )
  val shortCommit = TypeDeclaration("ShortCommit",
    ("sha", "String") ::
    ("url", "String") ::
    ("html_url", "String") ::
    Nil
  )
  val userStats = TypeDeclaration("UserStats",
    ("email", "String") ::
    ("monday", "Int") ::
    ("tuesday", "Int") ::
    ("wednesday", "Int") ::
    ("thursday", "Int") ::
    ("friday", "Int") ::
    ("saturday", "Int") ::
    ("sunday", "Int") ::
    Nil
  )

  def imports(recSyntax: RecordSyntax) = recSyntax.imports
    .map{imp => s"import $imp"}
    .mkString("\n")

  def declarations(recSyntax: RecordSyntax) = {
    List(userStats, shortCommit, user, verification, tree, commitIdentity, commit, commitEvent)
      .map{decl => recSyntax.decl(decl.name, decl.fields)}
      .mkString("\n")
  }

  def formats(recSyntax: RecordSyntax) = {
    def jsonFormat(decl: TypeDeclaration) = {
      val fieldExtractors = decl.fields map { case (name, tpe) => s"""val Success($name) = fromJson[$tpe](map("$name"))""" }
      s"""implicit object ${decl.name}Format extends JsonFormat[${decl.name}] {
         |  def read(ast: JValue): ${decl.name} = ast match {
         |    case JObject(map) => {
         |      ${fieldExtractors.mkString("\n      ")}
         |      ${recSyntax.create(decl.name, decl.fields.map{ case (name, _) => (name, name) })}
         |    }
         |    case x => deserializationError("Expected ${decl.name} as JObject, but got " + x)
         |  }
         |}""".stripMargin
    }
    List(shortCommit, user, verification, tree, commitIdentity, commit, commitEvent)
      .map(jsonFormat)
      .mkString("\n")
  }

  def methodBody(recSyntax: RecordSyntax) = {
    val emptyUserStats = recSyntax.create("UserStats", ("email", "email") :: ("monday", "0") ::
      ("tuesday", "0") :: ("wednesday", "0") :: ("thursday", "0") :: ("friday", "0") ::
      ("saturday", "0") :: ("sunday", "0") :: Nil)

    s"""|commits.foreach(obj => {
        |  val email = ${recSyntax.access(recSyntax.access(recSyntax.access("obj", "commit"), "author"), "email")}
        |  val dow = ${recSyntax.access(recSyntax.access(recSyntax.access("obj", "commit"), "author"), "date")}
        |    .atZone(ZoneId.systemDefault())
        |    .getDayOfWeek().getValue()
        |  val oldStats = table.getOrElse(email, $emptyUserStats): UserStats
        |  val newStats = dow match {
        |    case 1 => ${recSyntax.increment("oldStats", "monday")}
        |    case 2 => ${recSyntax.increment("oldStats", "tuesday")}
        |    case 3 => ${recSyntax.increment("oldStats", "wednesday")}
        |    case 4 => ${recSyntax.increment("oldStats", "thursday")}
        |    case 5 => ${recSyntax.increment("oldStats", "friday")}
        |    case 6 => ${recSyntax.increment("oldStats", "saturday")}
        |    case 7 => ${recSyntax.increment("oldStats", "sunday")}
        |  }
        |  table = table + (email->newStats)
        |})""".stripMargin
  }

  /* Real Time Benchmark specific source generator */
  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)
    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{name}}"          -> name,
          "{{imports}}"       -> imports(recSyntax),
          "{{declarations}}"  -> declarations(recSyntax),
          "{{formats}}"       -> formats(recSyntax),
          "{{method_body}}"   -> methodBody(recSyntax)
      )
    )
    src
  }
}

case object ScalaRTCaseStudy extends ScalaRTCaseStudy
