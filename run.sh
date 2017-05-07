#!/usr/bin/env bash

GENDIR=$1
FEATURE=$2
DATADIR=$3

function run {
    LANG=$1
    BENCH=$2
    DATAFILE="$(pwd)/$DATADIR/$BENCH/$FEATURE.json"

    CMD="java -jar target/benchmarks.jar -wi 3 -i 3 -t 1 -f 2 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$LANG/$BENCH" && $CMD)
}

# run "scalarecords_0_3__scala_2_11_8"
#run "scala"    "scalarecords_0_4__scala_2_11_8"
#run "scala"    "shapeless_2_3_2__scala_2_11_8"
run "whiteoak" "whiteoaknative__whiteoak_2_1"
#run "scala"    "caseclass__scala_2_11_8"
