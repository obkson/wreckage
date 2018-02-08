#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi


# --------- Scala 2.12 COMPOSSIBLE ----------

# clear cache
rm -r ~/.m2/repository/org/cvogt
# package forked compossible into jar
(cd records/compossible && sbt clean package)
# generate benchmarks
sbt "project scala212_compossible" "run $GENDIR \
org/cvogt/compossible_2.12/0.2-SNAPSHOT/compossible_2.12-0.2-SNAPSHOT.jar=./records/compossible/target/scala-2.12/compossible_2.12-0.2-SNAPSHOT.jar"
# package to jar
(cd $GENDIR/scala212_compossible && mvn clean package)


# --------- Scala 2.12 CASE CLASS -----------

# clear cache
rm -r ~/.m2/repository/se/obkson/wreckage/scala212_caseclass_lib_2.12
# generate library src
sbt "project scala212_caseclass" "run library $GENDIR"
# package to jar
(cd $GENDIR/scala212_caseclass_lib && mvn clean package)
# generate benchmarks
sbt "project scala212_caseclass" "run benchmarks $GENDIR \
se/obkson/wreckage/scala212_caseclass_lib_2.12/0.1/scala212_caseclass_lib_2.12-0.1.jar=$GENDIR/scala212_caseclass_lib/target/scala212_caseclass_lib_2.12-0.1.jar"
# package to jar
(cd $GENDIR/scala212_caseclass && mvn clean package)
