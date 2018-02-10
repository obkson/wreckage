package wreckage.builder
import benchmarking._

trait Generator {

  def benchmarks: Seq[Benchmark]

}
