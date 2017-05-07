package wreckage.builder
import benchmarking._

import java.nio.file.{Path, Paths, Files}

abstract class JMHProjectBuilder {
  // Default
  val groupId = List("benchmarks")
  val name = this.getClass.getSimpleName.split("\\$")(0).toLowerCase

  // Abstract
  val pom: String
  val dependencies: Seq[Dependency]
  val sourceFiles: Seq[SourceFile]

  // Implemented
  def generate(odPath: Path, localRepo: Option[Path]): Boolean = {
    // Validate output directory
    if (!Files.exists(odPath)) {
      Logger.error(s"""${odPath} does not exist""")
      return false
    }
    if (!Files.isDirectory(odPath)) {
      Logger.error(s"""${odPath} is not a directory""")
      return false
    }

    // Create project root
    val projectroot = odPath.resolve(Paths.get(this.name))
    Logger.info(s"""creating ${projectroot.normalize}""")
    FileHelper.createDirClean(projectroot)

    // TODO Make language specific!
    // Create Maven Scala folder structure
    val srcroot = projectroot.resolve(Paths.get("src","main","scala"))
    Logger.info(s"""creating ${srcroot.normalize}""")
    Files.createDirectories(srcroot)

    // Create source files
    sourceFiles.foreach{ src =>
      val srcDir  = srcroot.resolve(Paths.get(".", src.pkg:_*))
      val srcPath = srcDir.resolve(Paths.get(src.name))
      Files.createDirectories(srcDir)
      Logger.info(s"""creating ${srcPath.normalize}""")
      FileHelper.createFile(srcPath, src.content)
    }

    // Create Maven pom
    val pomStr = FileHelper.replace(pom, Map("{{localrepo}}"->localRepoXML(localRepo)))

    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    FileHelper.createFile(pomPath, pomStr)

    true
  }

  def localRepoXML(localRepo: Option[Path]): String = localRepo match {
    // TODO is there a way to force the path to be Relative to project bas dir?
    case Some(path) => s"""
      <repositories>
          <repository>
              <id>project.local</id>
              <name>project</name>
              <url>file:\\$$\\{project.basedir}/${path}</url>
          </repository>
      </repositories>
      """
    case None => ""
  }

  def main(args: Array[String]){
    if (args.length < 1) {
      Logger.error("No output directory provided")
      Logger.error("")
      Logger.error("Usage: ./generator outputdir [localrepo]")

      sys.exit(1)
    }
    val odStr = args(0)
    val odAbsPath = Paths.get(odStr).toAbsolutePath().normalize()

    val localRepo = if (args.length >= 2)
      Some(Paths.get(args(1)))
    else
      None

    generate(odAbsPath, localRepo)
  }

}

abstract class ScalaJMHProjectBuilder extends JMHProjectBuilder {

  // Keep these abstract
  val dependencies: Seq[Dependency]
  val sourceFiles: Seq[SourceFile]

  // Scala specific Abstract:
  val scalaVersion: String

  // Implemented for Scala:
  lazy val pom = {
    val pomTmpl = FileHelper.getResourceForClassAsString("/scala-pom.xml", getClass)
    val pomStr = FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> this.name,
          "{{scalaVersion}}" -> this.scalaVersion,
          "{{dependencies}}" -> this.dependencies.map(_.toXML).mkString("\n"))
    )
    pomStr
  }
}

abstract class WhiteoakJMHProjectBuilder extends JMHProjectBuilder {

  // Keep these abstract
  val dependencies: Seq[Dependency]
  val sourceFiles: Seq[SourceFile]

  // Implemented for Whiteoak:
  lazy val pom = {
    import java.util.regex.Pattern
    // TODO implement escaping in replace function
    // TODO Let this depend on the "sourceFiles" method
    val sources = "<argument>\\$\\{project.basedir}/src/main/scala/benchmarks/RTAccessFields.wo</argument>"

    val pomTmpl = FileHelper.getResourceForClassAsString("/whiteoak-pom.xml", getClass)
    val pomStr = FileHelper.replace(pomTmpl,
      Map("{{name}}"            -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"         -> groupId.mkString("."),
          "{{artifactId}}"      -> this.name,
          "{{whiteoakVersion}}" -> "2.1",
          "{{dependencies}}"    -> this.dependencies.map(_.toXML).mkString("\n"),
          "{{sources}}"         -> sources
      )
    )
    pomStr
  }

}
