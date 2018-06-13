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

function run_singleshot {
    BENCH=$1
    FEATURE=$2
    DATAFILE="$(pwd)/$DATADIR/$BENCH/$FEATURE.json"

    CMD="java -Xms4g -Xmx8g -jar target/benchmarks.jar -wi 0 -i 1 -t 1 -f 10 $FEATURE -rf json -rff $DATAFILE"
    echo "$CMD"

    mkdir -p $DATADIR/$BENCH
    (cd "$GENDIR/$BENCH" && $CMD)
}

#run_singleshot "dotty08_records" "RTCaseStudyComplete"
#run_singleshot "dotty08_records" "RTCaseStudyReadUpdate"
#run_singleshot "dotty08_records" "RTCaseStudyUpdate"
#run_singleshot "dotty08_records" "RTCaseStudyRead"
#run_singleshot "dotty08_caseclass" "RTCaseStudyComplete"
#run_singleshot "dotty08_caseclass" "RTCaseStudyReadUpdate"
#run_singleshot "dotty08_caseclass" "RTCaseStudyUpdate"
#run_singleshot "dotty08_caseclass" "RTCaseStudyRead"
#run_singleshot "scala212_compossible" "RTCaseStudyComplete"
#run_singleshot "scala212_compossible" "RTCaseStudyReadUpdate"
#run_singleshot "scala212_compossible" "RTCaseStudyUpdate"
#run_singleshot "scala212_compossible" "RTCaseStudyRead"
#run_singleshot "scala212_shapeless233" "RTCaseStudyComplete"
#run_singleshot "scala212_shapeless233" "RTCaseStudyReadUpdate"
#run_singleshot "scala212_shapeless233" "RTCaseStudyUpdate"
run_singleshot "scala212_shapeless233" "RTCaseStudyRead"
#run_singleshot "scala212_compossible" "RTCaseStudyReadUpdate"
#run_singleshot "dotty08_caseclass" "RTCaseStudyReadUpdate"
#run_singleshot "scala212_shapeless233" "RTCaseStudyReadUpdate"
#run_singleshot "scala212_compossible" "RTCaseStudyComplete"
#run_singleshot "dotty08_caseclass" "RTCaseStudyComplete"
#run_singleshot "scala212_shapeless233" "RTCaseStudyComplete"

# Java
#run_steadystate "java"     "javamethodreflection__java_1_8"

# Scala
#run_singleshot "scala"    "anonrefinements__scala_2_11_8"
#run_singleshot "scala"    "caseclass__scala_2_11_8"
#run_singleshot "scala"    "scalarecords_0_4__scala_2_11_8"
#run_singleshot "scala"    "compossible_0_2__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_3_2__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_2_5__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_3_0__scala_2_11_8"
#run_singleshot "scala"    "scalarecords_0_3__scala_2_11_8"
#run_singleshot "scala"    "shapeless_2_0_0__scala_2_11_8"

# Dotty
#run_singleshot "dotty"    "caseclass__dotty_0_1"

# Whiteoak
#run_singleshot "whiteoak" "whiteoaknative__whiteoak_2_1"
