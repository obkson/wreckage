package wreckage.builder
import benchmarking._

import scala.collection.immutable.{HashMap, Set}
import java.nio.file.{Path, Paths}

trait LibraryGenerator extends Generator {
  this: Language =>

  // Still abstract from parent
  def benchmarks: List[Benchmark]

  // Implementations must provide
  def library: RecordLibrary

  // This is the mixed in behaviour a LibraryGenerator provides:
  def generateLibrary(odAbsPath: Path): Boolean = {

    val sources: List[SourceFile] = {
      val pkg = library.pkg
      val recordFiles = benchmarks.flatMap(_.types).map { recTpe =>
        val name = s"${recTpe.alias}.${this.fileExt}"
        val content =
          s"""|package ${pkg.mkString(".")}
              |${library.decl(recTpe)}
              |""".stripMargin
        SourceFile(pkg, name, content)
      }
      // find set of unique parents
      val parents = Set[(RecordType)](benchmarks.flatMap(_.types).flatMap(_.parent): _*)
      val parentFiles = parents.map { pTpe =>
        val name = s"${pTpe.alias}.${this.fileExt}"
        val content =
          s"""|package ${pkg.mkString(".")}
              |${library.baseDecl(pTpe)}
              |""".stripMargin
        SourceFile(pkg, name, content)
      }
      recordFiles ++ parentFiles
    }
    val name = library.name
    val groupId = library.output.groupId
    val artifactId = library.output.artifactId
    val version = library.output.version

    val pomContent = this.libraryPomContent(name, groupId, artifactId, version, List(), sources)
    val project = MavenProject(name, sources, jarMap=HashMap.empty, pomContent, this.srcFolder)
    project.generate(odAbsPath)
  }

  def main(args: Array[String]){
    if (args.length < 1) {
      Logger.error("Usage: ./generator outputdir")
      sys.exit(1)
    }
    val outputdir = args(0)
    val odAbsPath = Paths.get(outputdir).toAbsolutePath().normalize()
    generateLibrary(odAbsPath)
  }
}
