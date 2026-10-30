$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

$sources = @(Get-ChildItem -Path 'src' -Recurse -Filter '*.java')
$mainClass = Join-Path $PSScriptRoot 'out/Main.class'
if (-not (Test-Path $mainClass) -or
    @($sources | Where-Object { $_.LastWriteTimeUtc -gt (Get-Item $mainClass).LastWriteTimeUtc }).Count -gt 0) {
    New-Item -ItemType Directory -Path 'out' -Force | Out-Null
    & javac -encoding UTF-8 -d out @($sources.FullName)
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

& java -cp out Main
exit $LASTEXITCODE
