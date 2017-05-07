#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi

# Shapeless need lots of stack space
MAVEN_OPTS="-Xss4m"

#(cd "$GENDIR/scala/scalarecords_0_3__scala_2_11_8" && MAVEN_OPTS="$MAVEN_OPTS" mvn clean install)
(cd "$GENDIR/scala/scalarecords_0_4__scala_2_11_8" && MAVEN_OPTS="$MAVEN_OPTS" mvn clean install)
(cd "$GENDIR/scala/shapeless_2_3_2__scala_2_11_8" && MAVEN_OPTS="$MAVEN_OPTS" mvn clean install)

#(cd "$GENDIR/whiteoak" && vagrant ssh -c "cd /vagrant/whiteoaknative_1_0__whiteoak_2_1 && jenv exec mvn clean install")
