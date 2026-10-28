#!/usr/bin/env sh
set -eu
mkdir -p out
find src/main/java -name '*.java' -print > sources.txt
javac --release 17 -Xlint:all -d out @sources.txt
echo 'Build successful. Run: java -cp out minibank.MiniBank'
