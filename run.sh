#!/usr/bin/env bash

GENDIR=$1
FEATURE=$2
DATADIR=$3

if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir feature datadir" >&2
  exit 1
fi
if [ -z "$FEATURE" ]; then
  echo "$0: no feature given" >&2
  echo "" >&2
  echo "usage: $0 basedir feature datadir" >&2
  exit 1
fi
if [ -z "$DATADIR" ]; then
  echo "$0: no data output directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir feature datadir" >&2
  exit 1
fi

function run_steadystate {
    LANG=$1
    BENCH=$2
    DATAFILE="$(pwd)/$DATADIR/$BENCH/$FEATURE.json"

    CMD="java -jar target/benchmarks.jar -wi 0 -i 20 -t 1 -f 10 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$LANG/$BENCH" && $CMD)
}

function run_singleshot {
    LANG=$1
    BENCH=$2
    DATAFILE="$(pwd)/$DATADIR/$BENCH/$FEATURE.json"

    CMD="java -Xss8m -jar target/benchmarks.jar -wi 0 -i 1 -t 1 -f 10 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$LANG/$BENCH" && $CMD)
}


run_steadystate "java"     "javamethodreflection__java_1_8"
run_steadystate "java"     "javafieldreflection__java_1_8"
#run_singleshot "scala"    "anonrefinements__scala_2_11_8"
#run_singleshot "scala"    "caseclass__scala_2_11_8"
#run_singleshot "scala"    "scalarecords_0_4__scala_2_11_8"
#run_singleshot "scala"    "compossible_0_2__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_3_2__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_2_5__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_3_0__scala_2_11_8"
#run_singleshot "scala"    "scalarecords_0_3__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_0_0__scala_2_11_8"
#run "whiteoak" "whiteoaknative__whiteoak_2_1"
#run "dotty"    "caseclass__dotty_0_1"
#run "dotty"    "selreclist__dotty_0_1"
