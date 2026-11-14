#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"
test_build=$(mktemp -d /tmp/oop26-tests.XXXXXX)
trap 'rm -rf "$test_build"' EXIT
find src tests -name '*.java' -print > "$test_build/sources.txt"
"${JAVAC:-javac}" --release 8 -Xlint:all -encoding UTF-8 -d "$test_build/classes" @"$test_build/sources.txt"
count=0
for test_source in tests/*Test.java; do
    java -ea -Dfile.encoding=UTF-8 -cp "$test_build/classes" "$(basename "$test_source" .java)"
    count=$((count + 1))
done
printf 'PASS: %s test programs\n' "$count"
