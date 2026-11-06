#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"
test_build=$(mktemp -d /tmp/oop26-tests.XXXXXX)
trap 'rm -rf "$test_build"' EXIT
find src tests -name '*.java' -print > "$test_build/sources.txt"
java -m jdk.compiler/com.sun.tools.javac.Main -encoding UTF-8 -d "$test_build/classes" @"$test_build/sources.txt"
count=0
for test_source in tests/*Test.java; do
    java -ea -Dfile.encoding=UTF-8 -cp "$test_build/classes" "$(basename "$test_source" .java)"
    count=$((count + 1))
done
printf 'PASS: %s test programs\n' "$count"
if command -v pwsh >/dev/null 2>&1; then
    java -ea -Dfile.encoding=UTF-8 -cp "$test_build/classes" ConsoleLauncherCheck "$(command -v pwsh)"
else
    printf 'SKIP: PowerShell launcher checks (pwsh not installed)\n'
fi
