package dottynative

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

object Records__Dotty_0_6_SNAPSHOT extends DottyJMHProjectBuilder {

  val artifactId = this.name
  val dottyBuildVersion = "0.6.0-bin-SNAPSHOT"
  val dottyBuildDir = "/Users/obkson/code/records/dev/dotty/dist-bootstrapped/target/pack/lib"
  override val pomPath = "/dotty-local-pom.xml"

  override def managedDependencies = super.managedDependencies ++ List(
    ManagedDependency(List("org","scala-lang"), "scala-library", "2.12.4"),
    ManagedDependency(List("org","scala-lang","modules"), "scala-asm", "6.0.0-scala-1"),
    ManagedDependency(List("com","typesafe","sbt"), "sbt-interface", "0.13.15")
  )

  def unmanagedDependencies = List(
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-library_0.6", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-library_0.6-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-compiler_0.6", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-compiler_0.6-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("ch","epfl","lamp"), "dotty-interfaces", dottyBuildVersion,
      Paths.get(dottyBuildDir, s"dotty-interfaces-$dottyBuildVersion.jar")),
    UnmanagedDependency(List("se", "obkson", "wreckage"), "parsing_2.12", "0.1",
      Paths.get("parsing/target/scala-2.12/parsing_2.12-0.1.jar").toAbsolutePath())
  )

  object Syntax extends RecordSyntax {

    val imports = List("dotty.records._")

    def decl(name: String, fields: Seq[(String, String)]): String = {
      s"""type $name = ${tpe(name, fields)}"""
    }

    def create(name: String, fields: Seq[(String, String)]): String = {
      // e.g. Record(f1=1, f2=2)
      fields.map{ case (k, v) => s"$k=$v" }
        .mkString(s"Record(", ", ",")")
    }

    def tpe(name: String, fields: Seq[(String, String)]): String = {
      // e.g. Record { val f1: Int; val f2: Int }
      fields.map{ case (k, v) => s"val $k: $v" }
        .mkString(s"Record { ","; ", " }")
    }

    def access(prefix: String, field: String): String = {
      // e.g. rec.f2
      s"""$prefix.$field"""
    }

    def increment(prefix: String, field: String) = {
      s"""$prefix ++ Record($field=$prefix.$field+1)"""
    }
  }

  val pkg = List("benchmarks")
  val features: Seq[Benchmark] = List(
    ScalaRTCreationSize,
    ScalaRTAccessFields,
    ScalaRTUpdateSize,
    ScalaRTCaseStudy
    /*
    new ScalaRTCaseStudy {
      override def methodBody(recSyntax: RecordSyntax) = {
        val emptyUserStats = recSyntax.create("UserStats", ("email", "email") :: ("monday", "0") ::
          ("tuesday", "0") :: ("wednesday", "0") :: ("thursday", "0") :: ("friday", "0") ::
          ("saturday", "0") :: ("sunday", "0") :: Nil)

        s"""|commits.foreach(obj => {
            |  val email = ${recSyntax.access(recSyntax.access(recSyntax.access("obj", "commit"), "author"), "email")}
            |  val dow = ${recSyntax.access(recSyntax.access(recSyntax.access("obj", "commit"), "author"), "date")}
            |    .atZone(ZoneId.systemDefault())
            |    .getDayOfWeek().getValue()
            |  val oldStats = table.getOrElse(email, $emptyUserStats): UserStats
            |  def update(oldStats: UserStats, dow: Int)(implicit
            |    mo_ev: Ext[UserStats, Record{val monday: Int}],
            |    tu_ev: Ext[UserStats, Record{val tuesday: Int}],
            |    we_ev: Ext[UserStats, Record{val wednesday: Int}],
            |    th_ev: Ext[UserStats, Record{val thursday: Int}],
            |    fr_ev: Ext[UserStats, Record{val friday: Int}],
            |    sa_ev: Ext[UserStats, Record{val saturday: Int}],
            |    su_ev: Ext[UserStats, Record{val sunday: Int}]
            |  ) = dow match {
            |    case 1 => ${recSyntax.increment("oldStats", "monday")}
            |    case 2 => ${recSyntax.increment("oldStats", "tuesday")}
            |    case 3 => ${recSyntax.increment("oldStats", "wednesday")}
            |    case 4 => ${recSyntax.increment("oldStats", "thursday")}
            |    case 5 => ${recSyntax.increment("oldStats", "friday")}
            |    case 6 => ${recSyntax.increment("oldStats", "saturday")}
            |    case 7 => ${recSyntax.increment("oldStats", "sunday")}
            |  }
            |  val newStats = update(oldStats, dow)
            |  table = table + (email->newStats)
            |})""".stripMargin
      }
    }
    */
  )
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))
}
