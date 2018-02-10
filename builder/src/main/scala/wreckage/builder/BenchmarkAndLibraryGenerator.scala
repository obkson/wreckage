package wreckage.builder
import benchmarking._

import scala.collection.immutable.HashMap
import java.nio.file.{Path, Paths}

// convenience trait that mixes in both BenchmarkGenerator and LibraryGenerator and provides a combined main method
trait BenchmarkAndLibraryGenerator extends BenchmarkGenerator with LibraryGenerator {
  this: Language =>

  override def main(args: Array[String]){
    if (args.length < 2) {
      Logger.error("Usage: ./generator product outputdir [argument...]")
      Logger.error("")
      Logger.error("PRODUCTS:")
      Logger.error("  library")
      Logger.error("  benchmarks")
      sys.exit(1)
    } else {
      args(0) match {
        case "library" => {
          super[LibraryGenerator].main(args.slice(1, args.length))
        }
        case "benchmarks" => {
          super[BenchmarkGenerator].main(args.slice(1, args.length))
        }
        case x => {
          Logger.error(s"Unknown product $x")
        }
      }
    }
  }
}
