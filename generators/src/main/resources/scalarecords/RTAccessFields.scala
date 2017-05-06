package benchmarks
import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

import records._

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
class RTAccessFields {

  val r = Rec("f1" -> 1,"f2" -> 2,"f3" -> 3,"f4" -> 4,"f5" -> 5,"f6" -> 6,"f7" -> 7,"f8" -> 8,"f9" -> 9,"f10" -> 10,"f11" -> 11,"f12" -> 12,"f13" -> 13,"f14" -> 14,"f15" -> 15,"f16" -> 16,"f17" -> 17,"f18" -> 18,"f19" -> 19,"f20" -> 20,"f21" -> 21,"f22" -> 22,"f23" -> 23,"f24" -> 24,"f25" -> 25,"f26" -> 26,"f27" -> 27,"f28" -> 28,"f29" -> 29,"f30" -> 30,"f31" -> 31,"f32" -> 32)

  @Benchmark
  def access_f1 = r.f1

  @Benchmark
  def access_f2 = r.f2

  @Benchmark
  def access_f4 = r.f4

  @Benchmark
  def access_f8 = r.f8

  @Benchmark
  def access_f16 = r.f16

  @Benchmark
  def access_f32 = r.f32

}
