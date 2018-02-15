#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi

DOTTY_LIB=~/code/records/dev/dotty/dist-bootstrapped/target/pack/lib
DOTTY_DEPS="\
ch/epfl/lamp/dotty-library_0.6/0.6.0-bin-SNAPSHOT/dotty-library_0.6-0.6.0-bin-SNAPSHOT.jar=$DOTTY_LIB/dotty-library_0.6-0.6.0-bin-SNAPSHOT.jar \
ch/epfl/lamp/dotty-compiler_0.6/0.6.0-bin-SNAPSHOT/dotty-compiler_0.6-0.6.0-bin-SNAPSHOT.jar=$DOTTY_LIB/dotty-compiler_0.6-0.6.0-bin-SNAPSHOT.jar \
ch/epfl/lamp/dotty-interfaces/0.6.0-bin-SNAPSHOT/dotty-interfaces-0.6.0-bin-SNAPSHOT.jar=$DOTTY_LIB/dotty-interfaces-0.6.0-bin-SNAPSHOT.jar"

# purge the local maven dep cache
rm -r ~/.m2/repository/se/obkson/wreckage


# --------- Java 1.8 FIELD INTERFACE -----------

# generate library src
sbt "project java18_fieldinterface" "run library $GENDIR"

# package to jar
(cd $GENDIR/java18_fieldinterface_lib && mvn clean package)

# generate benchmarks
sbt "project java18_fieldinterface" "run benchmarks $GENDIR \
se/obkson/wreckage/java18_fieldinterface_lib/0.1/java18_fieldinterface_lib-0.1.jar=$GENDIR/java18_fieldinterface_lib/target/java18_fieldinterface_lib-0.1.jar"

# package to jar
(cd $GENDIR/java18_fieldinterface && mvn clean package)

#
## --------- Dotty 0.6 FIELD TRAIT GENERIC ---------
#
## generate library src
#sbt "project dotty06_fieldtraitgeneric" "run library $GENDIR $DOTTY_DEPS"
#
## package to jar
#(cd $GENDIR/dotty06_fieldtraitgeneric_lib && mvn clean package)
#
## generate benchmarks
#sbt "project dotty06_fieldtraitgeneric" "run benchmarks $GENDIR $DOTTY_DEPS \
#se/obkson/wreckage/dotty06_fieldtraitgeneric_lib_0.6/0.1/dotty06_fieldtraitgeneric_lib_0.6-0.1.jar=$GENDIR/dotty06_fieldtraitgeneric_lib/target/dotty06_fieldtraitgeneric_lib_0.6-0.1.jar"
#
## package to jar
#(cd $GENDIR/dotty06_fieldtraitgeneric && mvn clean package)


# --------- Dotty 0.6 CASE CLASS ---------

# generate library src
sbt "project dotty06_caseclass" "run library $GENDIR $DOTTY_DEPS"

# package to jar
(cd $GENDIR/dotty06_caseclass_lib && mvn clean package)

# generate benchmarks
sbt "project dotty06_caseclass" "run benchmarks $GENDIR $DOTTY_DEPS \
se/obkson/wreckage/dotty06_caseclass_lib_0.6/0.1/dotty06_caseclass_lib_0.6-0.1.jar=$GENDIR/dotty06_caseclass_lib/target/dotty06_caseclass_lib_0.6-0.1.jar"

# package to jar
(cd $GENDIR/dotty06_caseclass && mvn clean package)


# --------- Dotty 0.6 RECORDS -----------

# generate benchmarks
sbt "project dotty06_records" "run $GENDIR $DOTTY_DEPS"

# package to jar
(cd $GENDIR/dotty06_records && mvn clean package)


# --------- Scala 2.12 CASE CLASS -----------

# generate library src
sbt "project scala212_caseclass" "run library $GENDIR"

# package to jar
(cd $GENDIR/scala212_caseclass_lib && mvn clean package)

# generate benchmarks
sbt "project scala212_caseclass" "run benchmarks $GENDIR \
se/obkson/wreckage/scala212_caseclass_lib_2.12/0.1/scala212_caseclass_lib_2.12-0.1.jar=$GENDIR/scala212_caseclass_lib/target/scala212_caseclass_lib_2.12-0.1.jar"

# package to jar
(cd $GENDIR/scala212_caseclass && mvn clean package)


# --------- Scala 2.12 ANON REFINEMENTS ----------

# generate benchmarks
sbt "project scala212_anonref" "run $GENDIR"

# package to jar
(cd $GENDIR/scala212_anonref && mvn clean package)


# --------- Scala 2.12 COMPOSSIBLE ----------

# package forked compossible into jar
(cd records/compossible && sbt clean package)

# generate benchmarks
sbt "project scala212_compossible" "run $GENDIR \
org/cvogt/compossible_2.12/0.2-SNAPSHOT/compossible_2.12-0.2-SNAPSHOT.jar=./records/compossible/target/scala-2.12/compossible_2.12-0.2-SNAPSHOT.jar"

# package to jar
(cd $GENDIR/scala212_compossible && mvn clean package)


# --------- Scala 2.12 SHAPELESS 2.3.3 ----------

# generate benchmarks
sbt "project scala212_shapeless233" "run $GENDIR"

# package to jar
(cd $GENDIR/scala212_shapeless233 && mvn clean package)
