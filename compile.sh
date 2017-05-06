#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi

(cd "$1/scalarecords_0_3__scala_2_11_8" && mvn clean install)
# (cd "$1/scalarecords_0_4__scala_2_11_8" && mvn clean install)
