package {{pkg}}

/* JMH Imports */
import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

/* For parsing */
import scala.io.Source
import sjsonnew.shaded.scalajson.ast.{JValue, JNull, JTrue, JFalse, JNumber, JString, JArray, JObject}
import jawn.{ Parser, AsyncParser }
import scala.util.{Try, Success, Failure}

import se.obkson.wreckage.parsing.ScalaJsonFacade._
import se.obkson.wreckage.parsing.JsonFormat
import se.obkson.wreckage.parsing.JsonFormat.{ fromJson, deserializationError }

import scala.collection.immutable.HashMap
import java.time.{Instant, ZoneId, LocalDate}

/* Record lib imports */
{{imports}}

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
class {{name}} {

/************** Json Format for benchmark specific types ***************/

implicit object JsonFormatInstant extends JsonFormat[Instant] {
  def read(ast: JValue): Instant = ast match {
    case JString(value) => try {
        Instant.parse(value)
      } catch {
        case e: Exception => deserializationError("Malformed Date " + value)
      }
    case x => deserializationError("Expected Date as JString, but got " + x)
  }
}

/************** Json Format for record types ***************/

{{typeCarriers}}

{{formats}}

  /************* SETUP *************************/

  // list of json-encoded objects
  var lines = List[String]()

  @Setup(Level.Trial)
  def prepare = {
    val path = sys.env("COMMITS")
    val source = Source.fromFile(path)
    try
      lines = source.getLines().toList // force read BufferedSource to memory
    finally
      source.close()
  }

  def parse(lines: Seq[String]): List[CommitEvent] = {
    // parsed and typed CommitEvents
    var commits: List[CommitEvent] = Nil
    var counter = 0

    val p = Parser.async(mode = AsyncParser.ValueStream)

    // JValue -> CommitEvent (put on commits stack as side-effect)
    def sink(j: JValue): Unit = fromJson[CommitEvent](j) match {
      case Success(p: CommitEvent) =>
        //println(p.id)
        commits = p :: commits
        counter = counter + 1
        // if ((counter % 100) == 0)
        //   println(counter)
      case Failure(e) => throw new Exception(s"CommitEvent conversion failed, $e")
    }

    def absorb(str: String) = p.absorb(str) match {
      case Right(js) => js.foreach(sink)
      case Left(e) => throw new Exception(s"Parsing error, $e")
    }

    lines.foreach(absorb)
    p.finish().right.map(_.foreach(sink))

    return commits
  }

  /******************** BENCHMARK *************************/

  @Benchmark
  def calc_stats: (Int, HashMap[String, UserStats]) = {
    var table = HashMap.empty[String, UserStats]

    {{method_body}}

    // println(table.keys.size)
    // println(table.get("odersky@gmail.com"))
    return (table.keys.size, table)
  }
}
