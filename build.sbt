name := "wreckage"

version := "1.0"

scalaVersion := "2.12.4"

lazy val builder = (project in file("builder"))

lazy val scala212_caseclass = (project in file("generators/scala212_caseclass")).dependsOn(builder)

//lazy val records_compossible = (project in file("records/compossible"))
