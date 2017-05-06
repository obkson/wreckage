package scalarecords

import java.io.{BufferedWriter}
import java.nio.file.{Files, Path, Paths,
  NoSuchFileException, FileAlreadyExistsException, StandardOpenOption}
import scala.io.Source

object Logger {
  def error(msg: String){
    println("[ERROR] "+msg)
  }
  def info(msg: String){
    println("[INFO]  "+msg)
  }
  def debug(msg: String){
    println("[DEBUG] "+msg)
  }
}

object FileHelper {
  import scala.collection.JavaConverters._

  def deleteDirContent(dir: Path){
    Files.walk(dir)
      .sorted(Ordering[Path].reverse)
      .iterator().asScala
      .foreach { file =>
        if (file != dir) Files.delete(file)
      }
  }

  def deleteDirRecur(dir: Path){
    Files.walk(dir)
      .sorted(Ordering[Path].reverse)
      .iterator().asScala
      .foreach(Files.delete)
  }

  def createDirClean(dir: Path){
    if (Files.exists(dir)) {
      deleteDirContent(dir)
    } else {
      Files.createDirectory(dir)
    }
  }

  def createFile(path: Path, content: String){
    val bw: BufferedWriter = Files.newBufferedWriter(path,
                                                     StandardOpenOption.CREATE,
                                                     StandardOpenOption.TRUNCATE_EXISTING)
    bw.write(content)
    bw.close()
  }

  def getResourceForClassAsString(name: String, cls: Class[_]): String = {
    try {
      val tmplStream = cls.getResourceAsStream(name)
      Source.fromInputStream(tmplStream).getLines.mkString("\n")
    } catch {
      case e: NullPointerException => {
        Logger.error(s"Can't open template $name")
        throw new NoSuchFileException(name)
      }
    }
  }

  def replace(template: String, map: Map[String, String]): String = {
    import java.util.regex.Pattern

    map.foldLeft(template){ case (templInProgress: String, (k: String, v: String)) => 
      val rexp = Pattern.quote( k ).r
      rexp.replaceAllIn(templInProgress, v)
    }
  }

}

/****************** DEPENDENCY *******************/
case class Dependency(groupId: String, artifactId: String, version: String) {
  def toXML = s"""
        <dependency>
            <groupId>$groupId</groupId>
            <artifactId>$artifactId</artifactId>
            <version>$version</version>
        </dependency>
    """
}

/******************* SOURCE FILE *******************/
case class SourceFile(pkg: Seq[String], name: String,content: String)

/******************** RECORD LIBRARY ***************/
trait RecordSyntax {
  def imports: Seq[String]
  def create(fields: Seq[(String, String)]): String
  def access(prefix: String, field: String): String
}

/****************** BENCHMARK **********************/
trait Benchmark {
  val name = this.getClass.getSimpleName.split("\\$")(0)

  val inputs: Seq[Int]
  def state(recSyntax: RecordSyntax): String
  def method(input: Int, recSyntax: RecordSyntax): String

  def source(pkg: Seq[String], recSyntax: RecordSyntax): String = {
    val imports = recSyntax.imports.mkString("import ", ", ", "")
    val methods = inputs.map{ input => method(input, recSyntax) }.mkString("\n")

    val tmplStr = FileHelper.getResourceForClassAsString("/JMHBenchmark.scala", getClass)

    val src = FileHelper.replace(tmplStr,
      Map("{{pkg}}"     -> pkg.mkString("."),
          "{{imports}}" -> imports,
          "{{name}}"    -> this.name,
          "{{state}}"   -> state(recSyntax),
          "{{methods}}" -> methods)
    )
    src
  }

  def sourceFile(pkg: Seq[String], recSyntax: RecordSyntax): SourceFile = {
    SourceFile(pkg, s"${this.name}.scala", source(pkg, recSyntax))
  }
}

case object RTAccessFields extends Benchmark {
  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    val fields: Seq[(String, String)] = (1 to inputs.max).map{ idx =>
      (s"f$idx", s"$idx")
    }
    s"""val r = ${recSyntax.create(fields)}"""
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |def access_f$input = ${methodBody(input, recSyntax)}
       |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    recSyntax.access("r", s"f$input")
  }
}

case object RTAccessPolymorphism extends Benchmark {
  val inputs: Seq[Int] = List(1,2,4,8,16,32)

  def state(recSyntax: RecordSyntax): String = {
    // Create a record with 8 fields
    val fields: Seq[(String, String)] = (1 to 8).map{ idx =>
      (s"f$idx", s"$idx")
    }
    val rec = recSyntax.create(fields)

    // Create an Array of such records
    val rs = (1 to inputs.max).map( _ => rec )
      .mkString("Array(\n  ", ",\n  ",")")

    val ret = s"""
      |val rs = $rs
      |var i = -1
      |""".stripMargin
    ret
  }

  def method(input: Int, recSyntax: RecordSyntax): String = {
    s"""@Benchmark
       |def poly_deg$input = ${methodBody(input, recSyntax)}
       |""".stripMargin
  }

  def methodBody(input: Int, recSyntax: RecordSyntax): String = {
    s"""{
       |  i = (i + 1) % ${input}
       |  ${recSyntax.access("rs(i)", s"f8")}
       |}
       |""".stripMargin
  }
}


/******************* BENCHMARK GENERATOR ************/
abstract class ScalaRecords extends RecordSyntax {

  val name = this.getClass.getSimpleName.split("\\$")(0).toLowerCase
  val scalaVersion: String
  val dependencies: Seq[Dependency]

  val imports = List("records._")
  def create(fields: Seq[(String, String)]): String = {
    fields.map{ case (k, v) => s""""$k"->$v""" }.mkString("Rec(", ", ",")")
  }
  def access(prefix: String, field: String): String = {
    s"""$prefix.$field"""
  }

  val pkg = List("benchmarks")

  val features = List(
    RTAccessFields,
    RTAccessPolymorphism
  )

  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, this))

  def generate(odPath: Path): Boolean = {
    // Validate output directory
    if (!Files.exists(odPath)) {
      Logger.error(s"""${odPath} does not exist""")
      return false
    }
    if (!Files.isDirectory(odPath)) {
      Logger.error(s"""${odPath} is not a directory""")
      return false
    }

    // Create project root
    val projectroot = odPath.resolve(Paths.get(this.name))
    Logger.info(s"""creating ${projectroot.normalize}""")
    FileHelper.createDirClean(projectroot)

    // Create Maven Scala folder structure
    val srcroot = projectroot.resolve(Paths.get("src","main","scala"))
    Logger.info(s"""creating ${srcroot.normalize}""")
    Files.createDirectories(srcroot)

    // Create source files
    sourceFiles.foreach{ src =>
      val srcDir  = srcroot.resolve(Paths.get(".", src.pkg:_*))
      val srcPath = srcDir.resolve(Paths.get(src.name))
      Files.createDirectories(srcDir)
      Logger.info(s"""creating ${srcPath.normalize}""")
      FileHelper.createFile(srcPath, src.content)
    }

    // Create Maven pom
    val pomTmpl = FileHelper.getResourceForClassAsString("/scala-pom.xml", getClass)

    val pom = FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"      -> pkg.mkString("."),
          "{{artifactId}}"   -> this.name,
          "{{scalaVersion}}" -> this.scalaVersion,
          "{{dependencies}}" -> this.dependencies.map(_.toXML).mkString("\n"))
    )

    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    FileHelper.createFile(pomPath, pom)

    true
  }

  def main(args: Array[String]){
    if (args.length < 1) {
      Logger.error("Must provide an output directory")
      sys.exit(1)
    }
    val odStr = args(0)

    val odAbsPath = Paths.get(odStr).toAbsolutePath().normalize()

    generate(odAbsPath)
  }
}

object ScalaRecords_0_3__Scala_2_11_8 extends ScalaRecords {
  val scalaVersion = "2.11.8"
  val dependencies = List(
      Dependency("ch.epfl.lamp","scala-records_2.11","0.3")
    )
}

object ScalaRecords_0_4__Scala_2_11_8 extends ScalaRecords {
  val scalaVersion = "2.11.8"
  val dependencies = List(
      Dependency("ch.epfl.lamp","scala-records_2.11","0.4")
    )
}
