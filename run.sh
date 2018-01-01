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

    # 0 warmups, 20 iterations in 1 thread in 10 consequtive forks of a jvm
    CMD="java -jar target/benchmarks.jar -wi 0 -i 20 -t 1 -f 10 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$LANG/$BENCH" && $CMD)
}

### Existing

# Scala
run_steadystate "scala"     "caseclasses__scala_2_12_3"
#run_steadystate "scala"     "anonrefinements__scala_2_11_8"
#run_steadystate "scala"     "scalarecords_0_4__scala_2_11_8"
#run_steadystate "scala"     "compossible_0_2__scala_2_11_8"
run_steadystate "scala"     "shapeless_2_3_2__scala_2_12_3"

#Dotty
#run_steadystate "dotty"       "selreclist__dotty_0_1"
#run_steadystate "dotty"       "selrechashmap__dotty_0_1"
#run_steadystate "dotty"       "records__dotty_localfork"
run_steadystate "dotty"       "caseclasses__dotty_casestudy"
run_steadystate "dotty"       "records__dotty_casestudy"

#Whiteoak
#run_steadystate "whiteoak"   "whiteoaknative__whiteoak_2_1"

#run_steadystate "scala"     "shapeless_2_3_2__scala_2_12_2"

### Data structures

# Java
#run_steadystate "java"     "javafieldreflection__java_1_8"
#run_steadystate "java"     "javamethodreflection__java_1_8"

# Scala
#run_steadystate "scala"     "arrayrecord__scala_2_11_8"
#run_steadystate "scala"     "listrecord__scala_2_11_8"
#run_steadystate "scala"     "hashmaprecord__scala_2_11_8"
#run_steadystate "scala"     "interfacerecord__scala_2_11_8"
