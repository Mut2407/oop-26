#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"
mkdir -p out/console
find src -name '*.java' -print > out/sources.txt
java -m jdk.compiler/com.sun.tools.javac.Main --release 17 -Xlint:all -encoding UTF-8 -d out/console @out/sources.txt
exec java -Dfile.encoding=UTF-8 -cp out/console Main "$@"
