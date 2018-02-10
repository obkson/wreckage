package wreckage.builder

import java.nio.file.{Path, Paths, Files, StandardCopyOption}

case class MavenProject(
  name: String,
  sources: Seq[SourceFile],
  jarMap: Map[String, Path],
  pomContent: String,
  srcFolder: Seq[String],
) {

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
    val projectroot = odPath.resolve(Paths.get(name))
    Logger.info(s"""creating ${projectroot.normalize}""")
    FileHelper.createDirClean(projectroot)

    // Create Maven Scala folder structure
    val srcRoot = projectroot.resolve(Paths.get(".", srcFolder:_*))
    Logger.info(s"""creating ${srcRoot.normalize}""")
    Files.createDirectories(srcRoot)

    // Create source files
    sources.foreach{ src =>
      val srcDir  = srcRoot.resolve(Paths.get(".", src.pkg:_*))
      val srcPath = srcDir.resolve(Paths.get(src.name))
      Files.createDirectories(srcDir)
      Logger.info(s"""creating ${srcPath.normalize}""")
      FileHelper.createFile(srcPath, src.content)
    }

    // Create local repository
    val repoRoot = projectroot.resolve(Paths.get("repo"))
    Logger.info(s"""creating ${repoRoot.normalize}""")
    Files.createDirectories(repoRoot)

    jarMap.foreach { case (targetPath, absSourcePath) =>
      val absTargetPath = repoRoot.resolve(Paths.get(targetPath))
      val dirPath = absTargetPath.getParent()
      Files.createDirectories(dirPath)
      Files.copy(absSourcePath, absTargetPath, StandardCopyOption.REPLACE_EXISTING)
      Logger.info(s"""creating ${absTargetPath.normalize}""")
    }

    // Create Maven pom
    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    FileHelper.createFile(pomPath, pomContent)

    true
  }
}
