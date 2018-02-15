package wreckage.builder

trait Language {
  def srcFolder: Seq[String]
  def fileExt: String

  def benchmarkPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String
  def libraryPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String
}


trait ScalaLanguage extends Language {

  def srcFolder = List("src", "main", "scala")
  def fileExt = "scala"

  // default
  val scalaVersion = "2.12.4"

  def benchmarkPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String = {
    val pomTmplPath = "/scala-bench-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> artifactId,
          "{{dependencies}}" -> dependencies.map(_.toXML).mkString("\n"),
          "{{scalaVersion}}" -> scalaVersion,
      )
    )
  }

  def libraryPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String = {
    val pomTmplPath = "/scala-lib-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"Record Library for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> artifactId,
          "{{version}}"      -> version,
          "{{dependencies}}" -> dependencies.map(_.toXML).mkString("\n"),
          "{{scalaVersion}}" -> scalaVersion
      )
    )
  }
}

trait Scala211Language extends ScalaLanguage {
  override val scalaVersion = "2.11.8"
}

trait DottyLanguage extends Language {

  def srcFolder = List("src", "main", "scala")
  def fileExt = "scala"

  def sourceXML(sources: Seq[SourceFile]): String = sources.map { src =>
    s"<argument>$${project.basedir}/${srcFolder.mkString("/")}/${src.pkg.mkString("/")}/${src.name}</argument>"
  }.mkString("\n")

  def benchmarkPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String = {
    val pomTmplPath = "/dotty-bench-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> artifactId,
          "{{dependencies}}" -> dependencies.map(_.toXML).mkString("\n"),
          "{{sources}}"      -> sourceXML(sources)
      )
    )
  }

  def libraryPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String = {
    val pomTmplPath = "/dotty-lib-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"Record Library for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> artifactId,
          "{{version}}"      -> version,
          "{{dependencies}}" -> dependencies.map(_.toXML).mkString("\n"),
          "{{sources}}"      -> sourceXML(sources)
      )
    )
  }
}


trait JavaLanguage extends Language {

  def srcFolder = List("src", "main", "java")
  def fileExt = "java"

  def benchmarkPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String = {
    val pomTmplPath = "/java-bench-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> artifactId,
          "{{javaVersion}}"  -> "1.8",
          "{{dependencies}}" -> dependencies.map(_.toXML).mkString("\n")
      )
    )
  }

  def libraryPomContent(name: String, groupId: Seq[String], artifactId: String, version: String, dependencies: Seq[Dependency], sources: Seq[SourceFile]): String = {
    val pomTmplPath = "/java-lib-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"Record Library for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> artifactId,
          "{{version}}"      -> version,
          "{{dependencies}}" -> dependencies.map(_.toXML).mkString("\n"),
      )
    )
  }
}
