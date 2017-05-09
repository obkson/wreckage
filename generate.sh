#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi

(cd records/compossible && sbt package)

sbt "project generators" \
"runMain whiteoak.WhiteoakNative__Whiteoak_2_1 $GENDIR/whiteoak" \
#"runMain compossible.Compossible_0_2__Scala_2_11_8 $GENDIR/scala" \
#"runMain shapeless.Shapeless_2_3_2__Scala_2_11_8 $GENDIR/scala" 
#"runMain scalarecords.ScalaRecords_0_3__Scala_2_11_8 $GENDIR/scala" \
#"runMain scalarecords.ScalaRecords_0_4__Scala_2_11_8 $GENDIR/scala" \
#"runMain scalanative.CaseClass__Scala_2_11_8 $GENDIR/scala" \
#"runMain dottynative.CaseClass__Dotty_0_1 $GENDIR/dotty"
