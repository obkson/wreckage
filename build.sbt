name := "wreckage"

version := "1.0"

scalaVersion := "2.11.8"

lazy val generators = (project in file("generators"))

//lazy val generators = (project in file("generators")).dependsOn(builder)
//
//lazy val builder = (project in file("builder"))
