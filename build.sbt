name := "wreckage"

version := "1.0"

scalaVersion := "2.12.4"

lazy val builder = (project in file("builder"))

lazy val scala212_caseclass = (project in file("generators/scala212_caseclass")).dependsOn(builder)

lazy val scala212_compossible = (project in file("generators/scala212_compossible")).dependsOn(builder)

lazy val scala212_shapeless233 = (project in file("generators/scala212_shapeless233")).dependsOn(builder)

lazy val dotty06_records = (project in file("generators/dotty06_records")).dependsOn(builder)

lazy val dotty06_caseclass = (project in file("generators/dotty06_caseclass")).dependsOn(builder)
