#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi

(cd records/compossible && sbt package)

sbt -J-Xss8m "project generators" \
"runMain javanative.JavaFieldReflection__Java_1_8 $GENDIR/java" \
"runMain scalanative.AnonRefinements__Scala_2_11_8 $GENDIR/scala" \
"runMain scalanative.InterfaceRecord__Scala_2_11_8 $GENDIR/scala" \
"runMain scalanative.CaseClass__Scala_2_11_8 $GENDIR/scala" \
"runMain scalarecords.ScalaRecords_0_4__Scala_2_11_8 $GENDIR/scala" \
"runMain compossible.Compossible_0_2__Scala_2_11_8 $GENDIR/scala" \
"runMain scalanative.HashMapRecord__Scala_2_11_8 $GENDIR/scala" \
"runMain scalanative.ArrayRecord__Scala_2_11_8 $GENDIR/scala" \
"runMain scalanative.ListRecord__Scala_2_11_8 $GENDIR/scala" \
#"runMain javanative.JavaMethodReflection__Java_1_8 $GENDIR/java" \
#"runMain scalanative.CaseClass__Scala_2_11_8 $GENDIR/scala" \
#"runMain shapeless.Shapeless_2_3_2__Scala_2_11_8 $GENDIR/scala" \
#"runMain shapeless.Shapeless_2_3_0__Scala_2_11_8 $GENDIR/scala" \
#"runMain shapeless.Shapeless_2_2_5__Scala_2_11_8 $GENDIR/scala" \
#"runMain shapeless.Shapeless_2_0_0__Scala_2_11_8 $GENDIR/scala" \
#"runMain scalarecords.ScalaRecords_0_3__Scala_2_11_8 $GENDIR/scala" \
#"runMain whiteoak.WhiteoakNative__Whiteoak_2_1 $GENDIR/whiteoak" \
#"runMain dottynative.CaseClass__Dotty_0_1 $GENDIR/dotty"
