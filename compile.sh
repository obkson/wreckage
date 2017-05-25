#!/usr/bin/env bash

GENDIR=$1
if [ -z "$GENDIR" ]; then
  echo "$0: no base directory given" >&2
  echo "" >&2
  echo "usage: $0 basedir" >&2
  exit 1
fi

# Must purge cached version of our locally produced record jars
rm -r ~/.m2/repository/se/obkson/wreckage/records
rm -r ~/.m2/repository/org/cvogt

#(cd "$GENDIR/whiteoak" && vagrant ssh -c "rm -r ~/.m2/repository/se/obkson/wreckage/records && cd /vagrant/whiteoaknative__whiteoak_2_1 && jenv exec mvn clean install")

(cd "$GENDIR/java/javamethodreflection__java_1_8" && mvn clean install)
(cd "$GENDIR/java/javafieldreflection__java_1_8" && mvn clean install)

#(cd "$GENDIR/scala/caseclass__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/anonrefinements__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/scalarecords_0_3__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/scalarecords_0_4__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/compossible_0_2__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/shapeless_2_3_2__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/shapeless_2_2_5__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/shapeless_2_3_0__scala_2_11_8" && mvn clean install)
#(cd "$GENDIR/scala/shapeless_2_0_0__scala_2_11_8" && mvn clean install)

#(cd "$GENDIR/dotty/caseclass__dotty_0_1" && mvn clean install)
#(cd "$GENDIR/dotty/selreclist__dotty_0_1" && mvn clean install)

