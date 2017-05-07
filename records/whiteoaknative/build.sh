#!/usr/bin/env bash

CLASSES="target/whiteoak-2.1/classes"

mkdir -p "$CLASSES"
vagrant up
vagrant ssh -c "cd /vagrant &&                            \
                java                                      \
                  -cp lib/whiteoak-2.1.jar                \
                  whiteoak.tools.openjdk.compiler.WocMain \
                    whiteoaknative/*                      \
                    -d $CLASSES"

(cd "$CLASSES" && jar cf ../whiteoaknative_2.1-0.1.jar whiteoaknative)
