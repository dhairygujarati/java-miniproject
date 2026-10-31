#!/usr/bin/env sh
set -eu
./build.sh
java -cp out minibank.MiniBank
