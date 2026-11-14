[CmdletBinding()]
param([switch]$BuildOnly, [string]$JavaHome = $env:JAVA_HOME)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $java = 'java'
    $javac = 'javac'
    if ($JavaHome) {
        $java = Join-Path $JavaHome 'bin/java'
        $javac = Join-Path $JavaHome 'bin/javac'
    }
    $classes = Join-Path $PSScriptRoot 'out/console'
    if (Test-Path $classes) { Remove-Item $classes -Recurse -Force }
    New-Item -ItemType Directory -Path $classes -Force | Out-Null
    $sources = Get-ChildItem src -Recurse -Filter '*.java' | Sort-Object FullName
    $paths = [string[]]@($sources | ForEach-Object { '"' + $_.FullName.Replace('\', '/') + '"' })
    $sourceList = Join-Path $PSScriptRoot 'out/sources.txt'
    [IO.File]::WriteAllLines($sourceList, $paths, (New-Object Text.UTF8Encoding($false)))
    & $javac --release 17 -encoding UTF-8 -d $classes "@$sourceList"
    if ($LASTEXITCODE -ne 0) { throw 'Bien dich that bai. Hay kiem tra JDK/JAVA_HOME.' }
    Write-Host 'Build OK: out/console'
    if (-not $BuildOnly) {
        & $java '-Dfile.encoding=UTF-8' -cp $classes Main
        if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    }
} finally {
    Pop-Location
}
