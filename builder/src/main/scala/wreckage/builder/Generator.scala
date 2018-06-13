package wreckage.builder
import benchmarking._

import java.nio.file.{Path, Paths}
import scala.collection.immutable.{HashMap}

trait Generator {

  def benchmarks: Seq[Benchmark]

  // a little helper needed in both LibraryGenerator and BenchmarkGenerator
  def getJarMap(jarArgs: Seq[String]): Map[String, Path] = {
      val jarTpls = jarArgs.map { jarMapping =>
        val Array(target, source) = jarMapping.split("=")
        Tuple2(target, Paths.get(source).toAbsolutePath().normalize())
      }
      HashMap[String, Path](jarTpls: _*)
  }
}
