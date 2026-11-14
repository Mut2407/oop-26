[CmdletBinding()]
param([string]$JavaHome = $env:JAVA_HOME)
$ErrorActionPreference = 'Stop'
$testBuild = Join-Path ([IO.Path]::GetTempPath()) ('oop26-tests-' + [guid]::NewGuid())
Push-Location $PSScriptRoot
try {
    $java = if ($JavaHome) { Join-Path $JavaHome 'bin/java' } else { (Get-Command java -ErrorAction Stop).Source }
    $javac = if ($JavaHome) { Join-Path $JavaHome 'bin/javac' } else { 'javac' }
    New-Item -ItemType Directory -Path $testBuild | Out-Null
    $sources = Get-ChildItem src, tests -Recurse -Filter '*.java' | Sort-Object FullName
    $sourceList = Join-Path $testBuild 'sources.txt'
    $paths = [string[]]@($sources | ForEach-Object { '"' + $_.FullName.Replace('\', '/') + '"' })
    [IO.File]::WriteAllLines($sourceList, $paths, (New-Object Text.UTF8Encoding($false)))
    $classes = Join-Path $testBuild 'classes'
    & $javac --release 8 -Xlint:all -encoding UTF-8 -d $classes "@$sourceList"
    if ($LASTEXITCODE -ne 0) { throw 'Bien dich tests that bai.' }
    foreach ($test in Get-ChildItem tests -Filter '*Test.java' | Sort-Object Name) {
        & $java '-Dfile.encoding=UTF-8' -ea -cp $classes $test.BaseName
        if ($LASTEXITCODE -ne 0) { throw ('Test that bai: ' + $test.BaseName) }
    }
    Write-Host 'PASS: 4 test programs'
} finally {
    if (Test-Path -LiteralPath $testBuild) { Remove-Item -LiteralPath $testBuild -Recurse -Force }
    Pop-Location
}
