package wreckage.builder
import benchmarking._

import scala.collection.immutable.HashMap
import java.nio.file.{Path, Paths}

trait BenchmarkGenerator extends Generator {
  this: Language =>

  // Still abstract from parent
  def benchmarks: List[Benchmark]

  // Implementations must provide
  def name: String
  def syntax: RecordSyntax

  // Default
  def groupId = List("se", "obkson", "wreckage")
  def version = "0.1"
  def pkg = List(name)

  // This is the mixed in behaviour a BenchmarkGenerator provides:
  def genBenchmarks(odAbsPath: Path, jarMap: Map[String, Path]): Boolean = {

    val sourceFiles = benchmarks.map { benchmark =>
      val name = s"${benchmark.name}.${this.fileExt}"
      val content = benchmark.source(pkg, syntax)
      SourceFile(pkg, name, content)
    }

    val dependencies = syntax.dependencies ++ benchmarks.flatMap(_.dependencies)
    val artifactId = name
    val pomContent = this.benchmarkPomContent(name, groupId, artifactId, version, dependencies, sourceFiles)
    val project = MavenProject(name, sourceFiles, jarMap, pomContent, this.srcFolder)
    project.generate(odAbsPath)
  }

  def getJarMap(jarArgs: Seq[String]): Map[String, Path] = {
      val jarTpls = jarArgs.map { jarMapping =>
        val Array(target, source) = jarMapping.split("=")
        Tuple2(target, Paths.get(source).toAbsolutePath().normalize())
      }
      HashMap[String, Path](jarTpls: _*)
  }

  def main(args: Array[String]){
    if (args.length < 1) {
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


