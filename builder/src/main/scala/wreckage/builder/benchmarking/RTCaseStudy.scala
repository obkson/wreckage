package wreckage.builder
package benchmarking

/* Currently only for Scala / Dotty */
trait ScalaRTCaseStudy extends Benchmark {

  def template: String
  def filename_extension = "scala"

  val commitEvent = RecordType("CommitEvent", None,
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
  val commit = RecordType("Commit", None,
    ("committer", "CommitIdentity") ::
    ("message", "String") ::
    ("tree", "Tree") ::
    ("url", "String") ::
    ("comment_count", "Int") ::
    ("verification", "Verification") ::
    ("author", "CommitIdentity") ::
    Nil
  )
  val commitIdentity = RecordType("CommitIdentity", None,
    ("name", "String") ::
    ("email", "String") ::
    ("date", "Instant") ::
    Nil
  )
  val tree = RecordType("Tree", None,
    ("sha", "String") ::
    ("url", "String") ::
    Nil
  )
  val verification = RecordType("Verification", None,
    ("verified", "Boolean") ::
    ("reason", "String") ::
    ("signature", "Option[String]") ::
    ("payload", "Option[String]") ::
    Nil
  )
  val user = RecordType("User", None,
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
  val shortCommit = RecordType("ShortCommit", None,
    ("sha", "String") ::
    ("url", "String") ::
    ("html_url", "String") ::
    Nil
  )
  val userStats = RecordType("UserStats", None,
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

  def source(pkg: Seq[String], recSyntax: RecordSyntax): String

  def types = List(userStats, shortCommit, user, verification, tree, commitIdentity, commit, commitEvent)

  override val dependencies = List(
    Dependency(List("se", "obkson", "wreckage"), "parsing_2.12", "0.1"),
    Dependency(List("com", "eed3si9n"), "shaded-scalajson_2.12", "1.0.0-M4"),
    Dependency(List("org", "spire-math"), "jawn-parser_2.12", "0.11.0")
  )
}

case object ScalaRTCaseStudyComplete extends ScalaRTCaseStudy {

  def name = "RTCaseStudyComplete"
  def template = "/JMHScalaRTCaseStudyTemplate_Complete.scala"

  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {

    def imports = recSyntax.imports.map(imp => s"import $imp").mkString("\n")

    def typeCarriers = types.map(tpe => recSyntax.tpeCarrier(tpe)).mkString("\n")

    val formats = {
      def jsonFormat(tpe: RecordType) = {
        val fieldExtractors = tpe.fields map { case (name, tpe) => s"""val Success($name) = fromJson[$tpe](map("$name"))""" }
        s"""implicit object ${tpe.alias}Format extends JsonFormat[${recSyntax.tpe(tpe)}] {
           |  def read(ast: JValue): ${recSyntax.tpe(tpe)} = ast match {
           |    case JObject(map) => {
           |      ${fieldExtractors.mkString("\n      ")}
           |      ${recSyntax.create(tpe, tpe.fields.map{ case (name, _) => (name, name) })}
           |    }
           |    case x => deserializationError("Expected ${tpe.alias} as JObject, but got " + x)
           |  }
           |}""".stripMargin
      }
      List(shortCommit, user, verification, tree, commitIdentity, commit, commitEvent)
        .map(jsonFormat)
        .mkString("\n")
    }

    val methodBody = {
      val emptyUserStats = recSyntax.create(userStats, ("email", "email") :: ("monday", "0") ::
        ("tuesday", "0") :: ("wednesday", "0") :: ("thursday", "0") :: ("friday", "0") ::
        ("saturday", "0") :: ("sunday", "0") :: Nil)

      s"""|val commits = parse(lines)
          |commits.foreach(obj => {
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

    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)
    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{name}}"          -> name,
          "{{typeCarriers}}"  -> typeCarriers,
          "{{imports}}"       -> imports,
          "{{formats}}"       -> formats,
          "{{method_body}}"   -> methodBody
      )
    )
    src
  }

}

case object ScalaRTCaseStudyCompleteSubtyped extends ScalaRTCaseStudy {

  def name = "RTCaseStudyCompleteSubtyped"
  def template = "/JMHScalaRTCaseStudyTemplate_Complete.scala"

  val baseCommitEvent = RecordType("BaseCommitEvent", None, ("commit", "BaseCommit") :: Nil)
  val baseCommit = RecordType("BaseCommit", None, ("author", "BaseCommitIdentity") :: Nil)
  val baseCommitIdentity = RecordType("BaseCommitIdentity", None, ("email", "String") :: ("date", "Instant") :: Nil)

  override val commitEvent = RecordType("CommitEvent", Some(baseCommitEvent),
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
  override val commit = RecordType("Commit", Some(baseCommit),
    ("committer", "CommitIdentity") ::
    ("message", "String") ::
    ("tree", "Tree") ::
    ("url", "String") ::
    ("comment_count", "Int") ::
    ("verification", "Verification") ::
    ("author", "CommitIdentity") ::
    Nil
  )
  override val commitIdentity = RecordType("CommitIdentity", Some(baseCommitIdentity),
    ("name", "String") ::
    ("email", "String") ::
    ("date", "Instant") ::
    Nil
  )


  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {

    def imports = recSyntax.imports.map(imp => s"import $imp").mkString("\n")

    def typeCarriers = (baseCommitIdentity :: baseCommit :: baseCommitEvent :: types).map(tpe => recSyntax.tpeCarrier(tpe)).mkString("\n")

    val formats = {
      def jsonFormat(tpe: RecordType) = {
        val fieldExtractors = tpe.fields map { case (name, tpe) => s"""val Success($name) = fromJson[$tpe](map("$name"))""" }
        s"""implicit object ${tpe.alias}Format extends JsonFormat[${recSyntax.tpe(tpe)}] {
           |  def read(ast: JValue): ${recSyntax.tpe(tpe)} = ast match {
           |    case JObject(map) => {
           |      ${fieldExtractors.mkString("\n      ")}
           |      ${recSyntax.create(tpe, tpe.fields.map{ case (name, _) => (name, name) })}
           |    }
           |    case x => deserializationError("Expected ${tpe.alias} as JObject, but got " + x)
           |  }
           |}""".stripMargin
      }
      List(shortCommit, user, verification, tree, commitIdentity, commit, commitEvent)
        .map(jsonFormat)
        .mkString("\n")
    }

    val methodBody = {
      val emptyUserStats = recSyntax.create(userStats, ("email", "email") :: ("monday", "0") ::
        ("tuesday", "0") :: ("wednesday", "0") :: ("thursday", "0") :: ("friday", "0") ::
        ("saturday", "0") :: ("sunday", "0") :: Nil)

      s"""|val commits: Seq[${recSyntax.tpe(baseCommitEvent)}] = parse(lines)
          |commits.foreach(obj => {
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

    val tmplStr = FileHelper.getResourceForClassAsString(template, getClass)
    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"           -> pkg.mkString("."),
          "{{name}}"          -> name,
          "{{typeCarriers}}"  -> typeCarriers,
          "{{imports}}"       -> imports,
          "{{formats}}"       -> formats,
          "{{method_body}}"   -> methodBody
      )
    )
    src
  }

}
