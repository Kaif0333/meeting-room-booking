$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$localJdkPath = Join-Path $repoRoot ".tools\jdk-17.0.18+8"
Set-Location $repoRoot

if (Test-Path $localJdkPath) {
    $env:JAVA_HOME = (Resolve-Path $localJdkPath).Path
    $env:Path = "$env:JAVA_HOME\bin;$env:Path"
}

cmd /c .\mvnw.cmd spring-boot:run
