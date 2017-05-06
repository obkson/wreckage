#!/usr/bin/env bash

GENDIR=$1
FEATURE=$2
DATADIR=$3

function run {
    BENCH=$1

    CMD="java -jar target/benchmarks.jar -wi 3 -i 3 -t 1 -f 2 $FEATURE -rf json -rff $DATADIR/$BENCH/$FEATURE.json"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$BENCH" && $CMD)
}

run "scalarecords_0_3__scala_2_11_8"
# run "scalarecords_0_4__scala_2_11_8"
