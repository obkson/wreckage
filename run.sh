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
    BENCH=$1
    DATAFILE="$(pwd)/$DATADIR/$BENCH/$FEATURE.json"

    # 0 warmups, 5 iterations in 1 thread in 5 consequtive forks of a jvm
    CMD="java -jar target/benchmarks.jar -wi 0 -i 5 -t 1 -f 5 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$BENCH" && $CMD)
}

run_steadystate "dotty06_caseclass"
run_steadystate "dotty06_records"
run_steadystate "scala212_caseclass"
run_steadystate "scala212_compossible"
run_steadystate "scala212_shapeless233"
