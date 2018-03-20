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

#run_steadystate "dotty06_caseclass" "RTAccessFields"
#run_steadystate "dotty06_records" "RTAccessFields"
#run_steadystate "dotty06_fieldtraitgeneric" "RTAccessFields"
#run_steadystate "scala212_caseclass" "RTAccessFields"
#run_steadystate "scala212_anonref" "RTAccessFields"
#run_steadystate "scala212_compossible" "RTAccessFields"
run_steadystate "scala212_shapeless233" "RTAccessFields"

#run_steadystate "dotty06_records" "RTCaseStudyReadUpdate"
#run_steadystate "dotty06_caseclass" "RTCaseStudyReadUpdate"
#run_steadystate "scala212_compossible" "RTCaseStudyReadUpdate"
#run_steadystate "scala212_shapeless233" "RTCaseStudyReadUpdate"

#run_steadystate "dotty06_records" "RTCaseStudyRead"
#run_steadystate "dotty06_caseclass" "RTCaseStudyRead"
#run_steadystate "scala212_compossible" "RTCaseStudyRead"
#run_steadystate "scala212_shapeless233" "RTCaseStudyRead"


#run_steadystate "dotty06_records" "RTCaseStudyUpdate"
#run_steadystate "dotty06_caseclass" "RTCaseStudyUpdate"
#run_steadystate "scala212_compossible" "RTCaseStudyUpdate"
#run_steadystate "scala212_shapeless233" "RTCaseStudyUpdate"
