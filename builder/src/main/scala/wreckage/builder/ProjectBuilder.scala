package wreckage.builder
import benchmarking._

import java.nio.file.{Path, Paths, Files}

abstract class ScalaJMHProjectBuilder {

  // Default
  val groupId = List("benchmarks")
  val name = this.getClass.getSimpleName.split("\\$")(0).toLowerCase

  // Abstract
  val scalaVersion: String
  val dependencies: Seq[Dependency]
  val sourceFiles: Seq[SourceFile]

  // Implementated
  def generate(odPath: Path): Boolean = {
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
    val pomTmpl = FileHelper.getResourceForClassAsString("/scala-pom.xml", getClass)

    val pom = FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> this.name,
          "{{scalaVersion}}" -> this.scalaVersion,
          "{{dependencies}}" -> this.dependencies.map(_.toXML).mkString("\n"))
    )

    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    FileHelper.createFile(pomPath, pom)

    true
  }

  def main(args: Array[String]){
    if (args.length < 1) {
      Logger.error("Must provide an output directory")
      sys.exit(1)
    }
    val odStr = args(0)

    val odAbsPath = Paths.get(odStr).toAbsolutePath().normalize()

    generate(odAbsPath)
  }
}
