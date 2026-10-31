#!/usr/bin/env sh
set -eu
rm -rf out
mkdir -p out
find src/main/java -name '*.java' -print > sources.txt
javac --release 17 -Xlint:all -d out @sources.txt
printf 'Main-Class: minibank.MiniBank\n' > manifest.mf
jar cfm minibank.jar manifest.mf -C out .
echo 'Build successful. Run: java -jar minibank.jar'
