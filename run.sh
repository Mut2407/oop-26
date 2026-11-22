#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"

mkdir -p out/console
find src -name '*.java' -print > out/sources.txt

# Bien dich ma nguon
"${JAVAC:-javac}" -encoding UTF-8 -d out/console @out/sources.txt
echo "Build OK: out/console"

# Kiem tra neu stdin khong phai la Terminal (chay trong Output pane)
if [ ! -t 0 ]; then
    echo "Dang mo cua so Terminal de thao tac menu..."
    if command -v gnome-terminal &>/dev/null; then
        gnome-terminal -- bash -c "cd '$PWD' && exec java -Dfile.encoding=UTF-8 -cp out/console Main; read -p 'Nhan Enter de thoat...'"
        exit 0
    elif command -v xterm &>/dev/null; then
        xterm -e "bash -c 'cd \"$PWD\" && exec java -Dfile.encoding=UTF-8 -cp out/console Main; read -p \"Nhan Enter de thoat...\"'"
        exit 0
    elif [ "$(uname)" = "Darwin" ]; then
        osascript -e "tell application \"Terminal\" to do script \"cd \\\"$PWD\\\" && exec java -Dfile.encoding=UTF-8 -cp out/console Main\""
        exit 0
    fi
fi

# Chay truc tiep trong Terminal
exec java -Dfile.encoding=UTF-8 -cp out/console Main "$@"
