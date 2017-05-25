package {{pkg}};
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

{{imports}}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class {{name}} {

{{state}}

{{methods}}

}

