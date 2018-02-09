package wreckage.builder
import benchmarking._

import scala.collection.immutable.HashMap
import java.nio.file.{Path, Paths, Files, StandardCopyOption}

trait BenchmarkGenerator {

  // Abstract
  def name: String
  def benchmarks: List[Benchmark]
  def syntax: RecordSyntax
  def srcFolder: Seq[String]
  def fileExt: String
  def pomContent: String

  // Default
  def groupId = List("se", "obkson", "wreckage")
  def version = "0.1"
  def pkg = List(name)

  // Implemented
  lazy val sourceFiles = benchmarks.map { benchmark =>
    val name = s"${benchmark.name}.${fileExt}"
    val content = benchmark.source(pkg, syntax)
    SourceFile(pkg, name, content)
  }

  def genBenchmarks(odAbsPath: Path, jarMap: Map[String, Path]): Boolean = {

    // Validate output directory
    if (!Files.exists(odAbsPath)) {
      Logger.error(s"""${odAbsPath} does not exist""")
      return false
    }
    if (!Files.isDirectory(odAbsPath)) {
      Logger.error(s"""${odAbsPath} is not a directory""")
      return false
    }

    // Create project root
    val projectroot = odAbsPath.resolve(Paths.get(name))
    Logger.info(s"""creating ${projectroot.normalize}""")
    FileHelper.createDirClean(projectroot)

    // Create Maven standard folder structure
    val srcRoot = projectroot.resolve(Paths.get(".", srcFolder:_*))
    Logger.info(s"""creating ${srcRoot.normalize}""")
    Files.createDirectories(srcRoot)

    // Create source files
    sourceFiles.foreach{ src =>
      val srcDir  = srcRoot.resolve(Paths.get(".", src.pkg:_*))
      val srcPath = srcDir.resolve(Paths.get(src.name))
      Files.createDirectories(srcDir)
      Logger.info(s"""creating ${srcPath.normalize}""")
      FileHelper.createFile(srcPath, src.content)
    }

    // Create local repository
    val repoRoot = projectroot.resolve(Paths.get("repo"))
    Logger.info(s"""creating ${repoRoot.normalize}""")
    Files.createDirectories(repoRoot)

    jarMap.foreach { case (targetPath, sourcePath) =>
      val absSourcePath = sourcePath.toAbsolutePath().normalize()
      val absTargetPath = repoRoot.resolve(Paths.get(targetPath))
      val dirPath = absTargetPath.getParent()
      Files.createDirectories(dirPath)
      Files.copy(absSourcePath, absTargetPath, StandardCopyOption.REPLACE_EXISTING)
      Logger.info(s"""creating ${absTargetPath.normalize}""")
    }

    // Create Maven pom
    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    FileHelper.createFile(pomPath, pomContent)

    true
  }

  def getJarMap(args: Seq[String]): Map[String, Path] = {
      val jarTpls = args.map { jarMapping =>
        val Array(unique, path) = jarMapping.split("=")
        Tuple2(unique, Paths.get(path).toAbsolutePath().normalize())
      }
      HashMap[String, Path](jarTpls: _*)
  }

  def main(args: Array[String]){
    if (args.length < 2) {
      Logger.error("Usage: ./generator outdir [jars...]")
      sys.exit(1)
    } else {
      val outputdir = args(0)
      val odAbsPath = Paths.get(outputdir).toAbsolutePath().normalize()
      val jarMap = getJarMap(args.slice(1, args.length))

      genBenchmarks(odAbsPath, jarMap)
    }
  }
}


trait ScalaBenchmarkGenerator extends BenchmarkGenerator {
  // Still abstract
  def name: String
  def benchmarks: List[Benchmark]
  def syntax: RecordSyntax

  // Scala-specific stuff
  val scalaVersion = "2.12.4"

  // Implemented for parent
  def srcFolder = List("src", "main", "scala")
  def fileExt = "scala"

  lazy val pomContent = {
    val deps = syntax.dependencies
    val pomTmplPath = "/scala-bench-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> name,
          "{{scalaVersion}}" -> scalaVersion,
          "{{dependencies}}" -> deps.map(_.toXML).mkString("\n")
      )
    )
  }
}


trait DottyBenchmarkGenerator extends BenchmarkGenerator {
  // Still abstract
  def name: String
  def benchmarks: List[Benchmark]
  def syntax: RecordSyntax

  // Implemented for parent
  def srcFolder = List("src", "main", "scala")
  def fileExt = "scala"

  lazy val pomContent = {
    val deps = syntax.dependencies
    val sources = sourceFiles.map { src =>
      s"<argument>$${project.basedir}/${srcFolder.mkString("/")}/${src.pkg.mkString("/")}/${src.name}</argument>"
    }.mkString("\n")
    val pomTmplPath = "/dotty-bench-pom.tmpl"
    val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)
    FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> name,
          "{{dependencies}}" -> deps.map(_.toXML).mkString("\n"),
          "{{sources}}"      -> sources
      )
    )
  }
}
