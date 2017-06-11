package wreckage.builder
import benchmarking._

import java.nio.file.{Path, Paths, Files, StandardCopyOption}

abstract class JMHProjectBuilder {
  // Default
  val groupId = List("se","obkson","wreckage","benchmarks")
  val name = this.getClass.getSimpleName.split("\\$")(0).toLowerCase

  // Abstract
  def pom: String
  def managedDependencies: Seq[ManagedDependency]
  def unmanagedDependencies: Seq[UnmanagedDependency]
  def sourceFiles: Seq[SourceFile]
  def srcfolder: Seq[String] // e.g. src, main, scala

  // Implemented
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
    val srcroot = projectroot.resolve(Paths.get(".", srcfolder:_*))
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

    // Create local repository
    val reporoot = projectroot.resolve(Paths.get("repo"))
    Logger.info(s"""creating ${reporoot.normalize}""")
    Files.createDirectories(reporoot)

    unmanagedDependencies.foreach{ dep =>
      val fullPathSeq = dep.groupId ++ List(dep.artifactId, dep.version)
      val fullPath = reporoot.resolve(Paths.get(".", fullPathSeq:_*))
      val jarPath = fullPath.resolve(Paths.get(s"${dep.artifactId}-${dep.version}.jar"))
      Files.createDirectories(fullPath)
      Files.copy(dep.jarlocation, jarPath, StandardCopyOption.REPLACE_EXISTING)
      Logger.info(s"""creating ${jarPath.normalize}""")
    }

    // Create Maven pom
    val pomPath = projectroot.resolve(Paths.get("pom.xml"))
    FileHelper.createFile(pomPath, pom)

    true
  }

  def main(args: Array[String]){
    if (args.length < 1) {
      Logger.error("No output directory provided")
      Logger.error("")
      Logger.error("Usage: ./generator outputdir [localrepo]")

      sys.exit(1)
    }
    val odStr = args(0)
    val odAbsPath = Paths.get(odStr).toAbsolutePath().normalize()

    generate(odAbsPath)
  }

}

abstract class ScalaJMHProjectBuilder extends JMHProjectBuilder {

  def managedDependencies: Seq[ManagedDependency] = List(
      //ManagedDependency(List("org","scala-lang"),"scala-library",scalaVersion), // already included by pom
      ManagedDependency(List("org","scala-lang"),"scala-reflect",scalaVersion),
      ManagedDependency(List("org","scala-lang"),"scala-compiler",scalaVersion)
    )
  def unmanagedDependencies: Seq[UnmanagedDependency]= List()
  def sourceFiles: Seq[SourceFile]
  def srcfolder = List("src","main","scala")

  def pomPath = "/scala-pom.xml"

  // Scala specific Abstract:
  def scalaVersion: String

  // Implemented for Scala:
  lazy val pom = {
    val deps = managedDependencies ++ unmanagedDependencies
    val pomTmpl = FileHelper.getResourceForClassAsString(pomPath, getClass)
    val pomStr = FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> this.name,
          "{{scalaVersion}}" -> this.scalaVersion,
          "{{dependencies}}" -> deps.map(_.toXML).mkString("\n"))
    )
    pomStr
  }
}

abstract class DottyJMHProjectBuilder extends JMHProjectBuilder {

  def dottyBuildVersion: String // e.g. 0.1.1-bin-20170506-385178d-NIGHTLY

  def srcfolder = List("src","main","scala")

  lazy val pom = {
    import java.util.regex.Pattern

    // TODO implement escaping in replace function instead
    val sources = sourceFiles.map{src =>
      s"<argument>$${project.basedir}/${srcfolder.mkString("/")}/${src.pkg.mkString("/")}/${src.name}</argument>"
    }.mkString("\n")

    val deps = managedDependencies ++ unmanagedDependencies

    val pomTmpl = FileHelper.getResourceForClassAsString("/dotty-pom.xml", getClass)
    val pomStr = FileHelper.replace(pomTmpl,
      Map("{{name}}"            -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"         -> groupId.mkString("."),
          "{{artifactId}}"      -> this.name,
          "{{dependencies}}"    -> deps.map(_.toXML).mkString("\n"),
          "{{sources}}"         -> sources
      )
    )
    pomStr
  }
}

abstract class Dotty_0_1__JMHProjectBuilder extends DottyJMHProjectBuilder {
  def getLatestDottyBuild = {
    // This is how sbt-dotty plugin does it:
    val Version = """      <version>(0.1\..*-bin.*)</version>""".r
    scala.io.Source
      .fromURL("http://repo1.maven.org/maven2/ch/epfl/lamp/dotty_0.1/maven-metadata.xml")
      .getLines()
      .collect { case Version(version) => version }
      .toSeq
      .last
  }

  val dottyBuildVersion = getLatestDottyBuild // e.g. "0.1.1-bin-20170506-385178d-NIGHTLY"

  def managedDependencies = List(
    ManagedDependency(List("ch","epfl","lamp"), "dotty-compiler_0.1", dottyBuildVersion),
    ManagedDependency(List("ch","epfl","lamp"), "dotty-library_0.1", dottyBuildVersion),
    ManagedDependency(List("ch","epfl","lamp"), "dotty-interfaces", dottyBuildVersion),
    ManagedDependency(List("org","scala-lang"), "scala-library", "2.11.11"),
    ManagedDependency(List("org","scala-lang"), "scala-reflect", "2.11.11"),
    ManagedDependency(List("org","scala-lang","modules"), "scala-asm", "5.1.0-scala-2"),
    ManagedDependency(List("com","typesafe","sbt"), "sbt-interface", "0.13.15")
  )

  def unmanagedDependencies = List[UnmanagedDependency]()
}

abstract class JavaJMHProjectBuilder extends JMHProjectBuilder {

  def managedDependencies: Seq[ManagedDependency] = List()
  def unmanagedDependencies: Seq[UnmanagedDependency]= List()
  def sourceFiles: Seq[SourceFile]
  def srcfolder = List("src","main","java")

  def pomPath = "/java-pom.xml"

  def javaVersion = "1.8" // default

  lazy val pom = {
    val deps = managedDependencies ++ unmanagedDependencies
    val pomTmpl = FileHelper.getResourceForClassAsString(pomPath, getClass)
    val pomStr = FileHelper.replace(pomTmpl,
      Map("{{name}}"         -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"      -> groupId.mkString("."),
          "{{artifactId}}"   -> this.name,
          "{{javaVersion}}"  -> this.javaVersion,
          "{{dependencies}}" -> deps.map(_.toXML).mkString("\n"))
    )
    pomStr
  }
}



abstract class WhiteoakJMHProjectBuilder extends JMHProjectBuilder {

  // TODO add dependencies here, like dotty above
  // Keep these abstract
  val managedDependencies: Seq[ManagedDependency]
  val unmanagedDependencies: Seq[UnmanagedDependency]
  val sourceFiles: Seq[SourceFile]
  def srcfolder = List("src","main","whiteoak")

  def compilationUnits = {
    // Whiteoak compiler only take one source file at a time
    // have call it separately for each source
    sourceFiles.map{src =>
      s"""
                    <execution>
                        <id>Compile ${src.name}</id>
                        <phase>process-sources</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>java</executable>
                            <arguments>
                                <argument>-classpath</argument>
                                <classpath/>
                                <argument>whiteoak.tools.openjdk.compiler.WocMain</argument>
                                <argument>-d</argument>
                                <argument>$${project.basedir}/target/classes/</argument>
                                <argument>$${project.basedir}/${srcfolder.mkString("/")}/${src.pkg.mkString("/")}/${src.name}</argument>"
                            </arguments>
                        </configuration>
                    </execution>
      """
    }.mkString("\n")
  }

  // Implemented for Whiteoak:
  lazy val pom = {
    import java.util.regex.Pattern

    val deps = managedDependencies ++ unmanagedDependencies

    val pomTmpl = FileHelper.getResourceForClassAsString("/whiteoak-pom.xml", getClass)
    val pomStr = FileHelper.replace(pomTmpl,
      Map("{{name}}"            -> s"JMH Benchmarks for ${this.name}",
          "{{groupId}}"         -> groupId.mkString("."),
          "{{artifactId}}"      -> this.name,
          "{{dependencies}}"    -> deps.map(_.toXML).mkString("\n"),
          "{{compilationUnits}}"-> compilationUnits
      )
    )
    pomStr
  }

}
