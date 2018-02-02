package compossible

import java.nio.file.Paths
import wreckage.builder._, benchmarking._

abstract class Compossible extends ScalaJMHProjectBuilder {

  // scala-records syntax
  object Syntax extends RecordSyntax {

    val imports = List("org.cvogt.compossible._")

    def decl(name: String, fields: Seq[(String, String)]): String = {
      // e.g. val rt_Rec2 = (RecordType f1[Int] & f2[Int] &)
      //      type Rec2 = rt_Rec2.Type
      val rt = fields.map{ case (k, t) => s"$k[$t]" }.mkString("(RecordType ", " & "," &)")
      s"""val rt_$name = $rt
         |type $name = rt_$name.Type""".stripMargin
    }

    def create(name: String, fields: Seq[(String, String)]): String = {
      // e.g. (Record f1 1 f2 2)
      fields.map{ case (k, v) => s"$k $v" }.mkString("(Record ", " ",")")
    }

    // use declared type alias for type carrier
    def tpe(name: String, fields: Seq[(String, String)]): String = name

    def access(prefix: String, field: String): String = {
      // e.g. rec.f2
      s"""$prefix.$field"""
    }

    def increment(prefix: String, field: String): String = {
      s"""$prefix $field ($prefix.$field + 1)"""
    }
  }

  val pkg = List("benchmarks")
  val features = List(
    ScalaCTCreationAccessSize,
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
            |  def update(oldStats: UserStats, dow: Int) = dow match {
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

  // Implemented
  val sourceFiles: Seq[SourceFile] = features.map(_.sourceFile(pkg, Syntax))
}

object Compossible_0_2__Scala_2_12_3 extends Compossible {
  val scalaVersion = "2.12.3"
  override val unmanagedDependencies = super.unmanagedDependencies ++ List(
    UnmanagedDependency(List("org","cvogt"), "compossible_2.12", "0.2-SNAPSHOT",
      Paths.get("records/compossible/target/scala-2.12/compossible_2.12-0.2-SNAPSHOT.jar").toAbsolutePath()),
    UnmanagedDependency(List("se", "obkson", "wreckage"), "parsing_2.12", "0.1",
      Paths.get("parsing/target/scala-2.12/parsing_2.12-0.1.jar").toAbsolutePath())
  )
}
