#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"
sources=$(mktemp /tmp/oop26-run-sources.XXXXXX)
trap 'rm -f "$sources"' EXIT
mkdir -p out
find src -name '*.java' -print > "$sources"
java -m jdk.compiler/com.sun.tools.javac.Main -encoding UTF-8 -d out @"$sources"
rm -f "$sources"
exec java -Dfile.encoding=UTF-8 -cp out Main "$@"
