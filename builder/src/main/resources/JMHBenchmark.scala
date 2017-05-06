package {{pkg}}
import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

{{imports}}

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
class {{name}} {

{{state}}

{{methods}}

}
