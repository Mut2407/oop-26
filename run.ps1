[CmdletBinding()]
param([switch]$BuildOnly, [string]$JavaHome = $env:JAVA_HOME, [switch]$InTerminal)
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
    if (-not (Test-Path $classes)) {
        New-Item -ItemType Directory -Path $classes -Force | Out-Null
    }

    $sources = Get-ChildItem src -Recurse -Filter '*.java' | Sort-Object FullName
    $paths = [string[]]@($sources | ForEach-Object { '"' + $_.FullName.Replace('\', '/') + '"' })
    $sourceList = Join-Path $PSScriptRoot 'out/sources.txt'
    [IO.File]::WriteAllLines($sourceList, $paths, (New-Object Text.UTF8Encoding($false)))

    & $javac -encoding UTF-8 -d $classes "@$sourceList"
    if ($LASTEXITCODE -ne 0) { throw 'Bien dich that bai. Hay kiem tra JDK/JAVA_HOME.' }
    Write-Host 'Build OK: out/console'

    if (-not $BuildOnly) {
        # Neu dang chay trong Output pane cua IDE (khong the nhap lieu ban phim)
        if ([Console]::IsInputRedirected -and -not $InTerminal) {
            Write-Host 'Phat hien dang chay tren Output (khong the nhap lieu).' -ForegroundColor Yellow
            Write-Host 'Dang mo cua so Terminal rieng de ban thao tac menu...' -ForegroundColor Cyan
            $scriptPath = $MyInvocation.MyCommand.Path
            Start-Process powershell -ArgumentList "-NoExit", "-ExecutionPolicy", "Bypass", "-File", "`"$scriptPath`"", "-InTerminal"
            return
        }

        # Chay truc tiep trong Terminal hien tai
        & $java '-Dfile.encoding=UTF-8' -cp $classes Main
    }
} finally {
    Pop-Location
}
