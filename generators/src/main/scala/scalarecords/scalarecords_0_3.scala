package scalarecords.scalarecords_0_3

import java.io.{BufferedWriter}
import java.nio.file.{Files, Path, Paths,
  NoSuchFileException, FileAlreadyExistsException, StandardOpenOption}

object Logger {
  def error(msg: String){
    println(msg)
  }
  def info(msg: String){
    println(msg)
  }
  def debug(msg: String){
    println(msg)
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

/******************* BENCHMARK GENERATOR ************/
object ScalaRecords_0_3__Scala_2_11_8 {

  val name = this.getClass.getSimpleName.split("\\$")(0).toLowerCase
  val scalaVersion = "2.11.8"

  def replace(template: String, map: Map[String, String]): String = {
    import java.util.regex.Pattern

    map.foldLeft(template){ case (templInProgress: String, (k: String, v: String)) => 
      val rexp = Pattern.quote( k ).r
      rexp.replaceAllIn(templInProgress, v)
    }
  }

  val dependencies = List(Dependency("ch.epfl.lamp","scala-records_2.11","0.3").toXML)
    .mkString("\n")

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
    Logger.info(s"""creating ${projectroot}""")
    FileHelper.createDirClean(projectroot)

    // Create Maven Scala folder structure
    val srcroot = projectroot.resolve(Paths.get("src","main","scala"))
    Logger.info(s"""creating ${srcroot}""")
    Files.createDirectories(srcroot)

    // Create source files
    import scala.io.Source
    val srcStream = getClass.getResourceAsStream("/scalarecords/RTAccessFields.scala")
    val srcStr = Source.fromInputStream(srcStream).getLines.mkString("\n")

    val src = SourceFile(List("benchmarks"), "RTAccessFields.scala", srcStr)

    val srcDir  = srcroot.resolve(Paths.get(".", src.pkg:_*))
    val srcPath = srcDir.resolve(Paths.get(src.name))
    Files.createDirectories(srcDir)
    createFile(srcPath, src.content)

    // Create Maven pom
    val pomTmplStream = getClass.getResourceAsStream("/scala-pom.xml")
    val pomTmpl = Source.fromInputStream(pomTmplStream).getLines.mkString("\n")

    val pom = replace(pomTmpl,
      Map("{{scalaVersion}}" -> this.scalaVersion,
          "{{dependencies}}" -> this.dependencies)
    )

    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    createFile(pomPath, pom)

    true
  }

  def main(args: Array[String]){
    if (args.length < 2) {
      Logger.error("Must provide an output directory")
      sys.exit(1)
    }
    val odStr = args(1)

    val odAbsPath = Paths.get(odStr).toAbsolutePath().normalize()

    generate(odAbsPath)
  }
}
