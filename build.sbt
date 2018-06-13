name := "wreckage"

version := "1.0"

scalaVersion := "2.12.4"

lazy val builder = (project in file("builder"))

lazy val parsing = (project in file("parsing"))

lazy val scala211_fieldtraitgeneric = (project in file("generators/scala211_fieldtraitgeneric")).dependsOn(builder)

lazy val scala212_caseclass = (project in file("generators/scala212_caseclass")).dependsOn(builder)

lazy val scala212_anonref = (project in file("generators/scala212_anonref")).dependsOn(builder)

lazy val scala212_compossible = (project in file("generators/scala212_compossible")).dependsOn(builder)

lazy val scala212_shapeless233 = (project in file("generators/scala212_shapeless233")).dependsOn(builder)

lazy val dotty08_records = (project in file("generators/dotty08_records")).dependsOn(builder)

lazy val dotty08_caseclass = (project in file("generators/dotty08_caseclass")).dependsOn(builder)

lazy val dotty06_fieldtrait = (project in file("generators/dotty06_fieldtrait")).dependsOn(builder)

lazy val dotty06_fieldtraitgeneric = (project in file("generators/dotty06_fieldtraitgeneric")).dependsOn(builder)

lazy val java18_fieldinterface = (project in file("generators/java18_fieldinterface")).dependsOn(builder)
