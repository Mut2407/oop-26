[CmdletBinding()]
param(
    [switch]$BuildOnly,
    [string]$JavaHome = $env:JAVA_HOME
)

$ErrorActionPreference = 'Stop'
$sourceList = $null
Push-Location $PSScriptRoot
try {
    # Use the same JDK for compilation and execution, even without javac on PATH.
    if ($JavaHome) {
        $java = Join-Path $JavaHome 'bin/java'
        if ($env:OS -eq 'Windows_NT') {
            $java += '.exe'
        }
        if (-not (Test-Path -LiteralPath $java -PathType Leaf)) {
            throw 'JAVA_HOME/-JavaHome phai tro den thu muc JDK 21 tro len.'
        }
    } else {
        $command = Get-Command java -CommandType Application -ErrorAction SilentlyContinue
        if (-not $command) {
            throw 'Khong tim thay Java. Hay cai JDK 21 tro len va dat JAVA_HOME, hoac truyen -JavaHome.'
        }
        $java = $command.Source
    }

    $sources = @(Get-ChildItem -LiteralPath 'src' -Recurse -File -Filter '*.java' | Sort-Object FullName)
    if ($sources.Count -eq 0) {
        throw 'Khong tim thay source Java trong src/.'
    }

    # This output belongs only to this launcher, separate from the IDE's cache.
    # Always rebuild: file timestamps alone cannot detect mixed/stale class files.
    $classes = Join-Path $PSScriptRoot 'out/console'
    if (Test-Path -LiteralPath $classes) {
        Remove-Item -LiteralPath $classes -Recurse -Force
    }
    New-Item -ItemType Directory -Path $classes -Force | Out-Null

    # An UTF-8 argument file handles spaces, Vietnamese paths and long file lists.
    $sourceList = [System.IO.Path]::GetTempFileName()
    $sourcePaths = @($sources | ForEach-Object { '"' + $_.FullName.Replace('\', '/').Replace('"', '\"') + '"' })
    [System.IO.File]::WriteAllLines($sourceList, [string[]]$sourcePaths, (New-Object System.Text.UTF8Encoding($false)))
    # source/target also works in runtimes that include the compiler but omit ct.sym.
    & $java '-Dfile.encoding=UTF-8' -m jdk.compiler/com.sun.tools.javac.Main -source 21 -target 21 -encoding UTF-8 -classpath $classes -d $classes "@$sourceList"
    if ($LASTEXITCODE -ne 0) {
        throw 'Bien dich that bai; menu chua duoc khoi dong. Xem loi phia tren va dung JDK 21 tro len co module jdk.compiler.'
    }
    Write-Host 'Build OK: out/console'

    if (-not $BuildOnly) {
        & $java '-Dfile.encoding=UTF-8' -cp $classes Main
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    }
} finally {
    if ($sourceList -and (Test-Path -LiteralPath $sourceList)) {
        Remove-Item -LiteralPath $sourceList -Force
    }
    Pop-Location
}
