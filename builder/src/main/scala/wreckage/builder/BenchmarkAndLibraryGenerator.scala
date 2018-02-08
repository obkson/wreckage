package wreckage.builder
import benchmarking._

import scala.collection.immutable.HashMap
import java.nio.file.{Path, Paths}

trait BenchmarkAndLibraryGenerator extends BenchmarkGenerator {

  def library: RecordLibrary

  def generateLibrary(odAbsPath: Path) {

    val sources: List[SourceFile] = {
      benchmarks.flatMap(_.types).map { recTpe =>
        val pkg = library.pkg
        val name = s"${recTpe.alias}.${fileExt}"
        val content =
          s"""|package ${pkg.mkString(".")}
              |${library.decl(recTpe)}
              |""".stripMargin

        SourceFile(pkg, name, content)
      }
    }

    val pomContent = {
      val pomTmplPath = "/scala-lib-pom.tmpl"
      val pomTmpl = FileHelper.getResourceForClassAsString(pomTmplPath, getClass)

      FileHelper.replace(pomTmpl,
        Map("{{name}}"         -> s"Record Library for ${this.name}",
            "{{groupId}}"      -> library.output.groupId.mkString("."),
            "{{artifactId}}"   -> library.output.artifactId,
            "{{version}}"      -> library.output.version,
            "{{scalaVersion}}" -> "2.12.4"
        )
      )
    }

    val project = MavenProject(library.name, library.output.groupId, library.output.artifactId, library.output.version,
                               sources, unmanagedDependencies=HashMap.empty, pomContent, srcFolder)
    project.generate(odAbsPath)
  }

  override def main(args: Array[String]){
    if (args.length < 2) {
      Logger.error("Usage: ./generator product outputdir [argument...]")
      Logger.error("")
      Logger.error("PRODUCTS:")
      Logger.error("  library")
      Logger.error("  benchmarks")
      sys.exit(1)
    } else {
      val product = args(0)
      val outputdir = args(1)
      val odAbsPath = Paths.get(outputdir).toAbsolutePath().normalize()
      product match {
        case "library" => {
          generateLibrary(odAbsPath)
        }
        case "benchmarks" => {
          val jarMap = getJarMap(args.slice(2, args.length))
          genBenchmarks(odAbsPath, jarMap)
        }
      }
    }
  }
}
