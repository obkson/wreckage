package wreckage.builder

import java.io.{BufferedWriter}
import java.nio.file.{Files, Path, Paths,
  NoSuchFileException, StandardOpenOption}
import scala.io.Source

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
      val escaped = v.flatMap {
          case '$' => "\\$"
          case '{' => "\\{"
          case '(' => "\\("
          case c => s"$c"
      }
      val rexp = Pattern.quote( k ).r
      rexp.replaceAllIn(templInProgress, escaped)
    }
  }

}


