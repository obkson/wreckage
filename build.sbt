name := "wreckage"

version := "1.0"

scalaVersion in ThisBuild := "2.11.8"

lazy val builder = (project in file("builder"))

lazy val records_compossible = (project in file("records/compossible"))

lazy val generators = (project in file("generators")).dependsOn(builder)

// Make sure that all unmanaged JARS we need are packaged before we run any generators
(compile in Runtime in generators) := {

  import sys.process._
  import java.io.File
  // Build java native
  Process("./build.sh", new File("./records/javanative")).!
  // Build whiteoak
  // ...on second thought, don't. It would ssh into vagrant on every build...

  // and run compile
  (compile in Runtime in generators).value
}

