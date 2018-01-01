package {{pkg}}

/* JMH Imports */
import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

/* For parsing */
import scala.io.Source
import scala.collection.mutable
import sjsonnew.shaded.scalajson.ast.{JValue, JNull, JTrue, JFalse, JNumber, JString, JArray, JObject}
import jawn.{ Parser, AsyncParser, MutableFacade }
import scala.util.{Try, Success, Failure}

import scala.collection.immutable.HashMap
import java.time.{Instant, ZoneId, LocalDate}

/* Record lib imports */
{{imports}}

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
class {{name}} {

  {{declarations}}

  // Contains pre-parsed commits from json when the benchmark begins
  var commits = List[CommitEvent]()

  @Setup(Level.Trial)
  def prepare = {
    println("Setup, parsing...")
    commits = parse("../../resources/commits.json")
  }

  @Benchmark
  def calc_stats: (Int, HashMap[String, UserStats]) = {
    var table = HashMap.empty[String, UserStats]
    // Given commits and an empty table, fill it with stats

{{method_body}}

    // println(table.keys.size)
    // println(table.get("odersky@gmail.com"))
    return (table.keys.size, table)
  }

  /***************** Support ******************/

  def parse(path: String): List[CommitEvent] = {
    // parsed and typed CommitEvents
    var commits: List[CommitEvent] = Nil
    var counter = 0

    val p = Parser.async(mode = AsyncParser.UnwrapArray)

    // JValue -> CommitEvent (put on commits stack as side-effect)
    def sink(j: JValue): Unit = fromJson[CommitEvent](j) match {
      case Success(p: CommitEvent) =>
        //println(p.id)
        commits = p :: commits
        counter = counter + 1
        if ((counter % 10000) == 0)
          println(counter)
      case Failure(e) => throw new Exception(s"CommitEvent conversion failed, $e")
    }

    def absorb(str: String) = p.absorb(str) match {
      case Right(js) => js.foreach(sink)
      case Left(e) => throw new Exception(s"Parsing error, $e")
    }

    def finish() = {
      p.finish().right.map(_.foreach(sink))
    }

    // lazy iterator of values
    val source = Source.fromFile(path)
    try
      source.getLines().foreach(absorb)
    finally
      source.close()

    return commits
  }


  /* Facade connecting jawn Parser with scalajson AST */

  implicit val parserFacade: MutableFacade[JValue] = new MutableFacade[JValue] {
    def jnull() = JNull
    def jfalse() = JFalse
    def jtrue() = JTrue
    def jnum(s: CharSequence, decIndex: Int, expIndex: Int) = JNumber(s.toString).get
    def jstring(s: CharSequence) = JString(s.toString)
    def jarray(vs: mutable.ArrayBuffer[JValue]) = JArray(vs.toVector)
    def jobject(vs: mutable.Map[String, JValue]) = JObject(vs.toMap)
  }


  /* Type class for converting json AST to value of type T */

  trait JsonFormat[T] {
    def read(ast: JValue): T
  }
  def fromJson[T](ast: JValue)(implicit fmt: JsonFormat[T]): Try[T] = Try(fmt.read(ast))
  def deserializationError(e: String) = throw new Exception(e)


  /* Implementation of JsonFormat for basic types */

  implicit object JsonFormatInt extends JsonFormat[Int] {
    def read(ast: JValue): Int = ast match {
      case JNumber(value) => try {
          value.toInt
        } catch {
          case e: Exception => deserializationError("Malformed Int " + value)
        }
      case x => deserializationError("Expected Int as JNumber, but got " + x)
    }
  }

  implicit object JsonFormatLong extends JsonFormat[Long] {
    def read(ast: JValue): Long = ast match {
      case JNumber(value) => try {
          value.toLong
        } catch {
          case e: Exception => deserializationError("Malformed Long " + value)
        }
      case x => deserializationError("Expected Long as JNumber, but got " + x)
    }
  }

  implicit object JsonFormatString extends JsonFormat[String] {
    def read(ast: JValue): String = ast match {
      case JString(value) => value
      case x => deserializationError("Expected String as JString, but got " + x)
    }
  }

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

  implicit def jsonFormatList[T](implicit fmt: JsonFormat[T]): JsonFormat[List[T]] = new JsonFormat[List[T]] {
    def read(ast: JValue): List[T] = ast match {
      case JArray(vector) => (vector map fmt.read).toList
      case x => deserializationError("Expected List as JArray, but got " + x)
    }
  }

  implicit def jsonFormatOption[T](implicit fmt: JsonFormat[T]): JsonFormat[Option[T]] = new JsonFormat[Option[T]] {
    def read(ast: JValue): Option[T] = ast match {
      case JNull => None
      case x => Some(fmt.read(x))
    }
  }

  /************** Json Format for record types ***************/

  implicit object AuthorFormat extends JsonFormat[Author] {
    def read(ast: JValue): Author = ast match {
      case JObject(map) => {
        val Success(date) = fromJson[Instant](map("date"))
        val Success(email) = fromJson[String](map("email"))
        {{create_author}}
      }
      case x => deserializationError("Expected Author as JObject, but got " + x)
    }
  }

  implicit object CommitFormat extends JsonFormat[Commit] {
    def read(ast: JValue): Commit = ast match {
      case JObject(map) => {
        val Success(author) = fromJson[Author](map("author"))
        {{create_commit}}
      }
      case x => deserializationError("Expected Commit as JObject, but got " + x)
    }
  }

  implicit object CommitEventFormat extends JsonFormat[CommitEvent] {
    def read(ast: JValue): CommitEvent = ast match {
      case JObject(map) => {
        val Success(sha) = fromJson[String](map("sha"))
        val Success(commit) = fromJson[Commit](map("commit"))
        {{create_commit_event}}
      }
      case x => deserializationError("Expected CommitEvent as JObject, but got " + x)
    }
  }

}
