#!/usr/bin/env bash

CLASSES="target/java-1.8/classes/"

mkdir -p "$CLASSES"

(cd src && javac javanative/* -d ../$CLASSES)

(cd "$CLASSES" && jar cf ../javanative_1.8-0.1.jar javanative)

