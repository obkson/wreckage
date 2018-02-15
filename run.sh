#!/usr/bin/env bash

GENDIR=$1
DATADIR=$2

if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir datadir" >&2
  exit 1
fi
if [ -z "$DATADIR" ]; then
  echo "$0: no data output directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir datadir" >&2
  exit 1
fi

function run_steadystate {
    BENCH=$1
    FEATURE=$2
    DATAFILE="$(pwd)/$DATADIR/$BENCH/$FEATURE.json"

    # 0 warmups, 20 iterations in 1 thread in 10 consequtive forks of a jvm
    CMD="java -jar target/benchmarks.jar -wi 0 -i 20 -t 1 -f 10 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$BENCH" && $CMD)
}

run_steadystate "dotty06_fieldtraitgeneric" "RTAccessPolymorphism"
run_steadystate "scala212_compossible" "RTAccessPolymorphism"
run_steadystate "scala212_anonref" "RTAccessPolymorphism"


run_steadystate "dotty06_caseclass" "RTUpdateSize"
run_steadystate "dotty06_records" "RTUpdateSize"
run_steadystate "scala212_compossible" "RTUpdateSize"
run_steadystate "scala212_shapeless233" "RTUpdateSize"

run_steadystate "dotty06_caseclass" "RTAccessPolymorphism"
