$ErrorActionPreference = "Stop"

$backendScript = Join-Path $PSScriptRoot "run-backend.ps1"
$frontendScript = Join-Path $PSScriptRoot "run-frontend.ps1"

$backendProcess = Start-Process -FilePath "powershell.exe" `
    -ArgumentList "-NoProfile", "-ExecutionPolicy", "Bypass", "-File", $backendScript `
    -PassThru

try {
    Start-Sleep -Seconds 8
    & $frontendScript
}
finally {
    if ($backendProcess -and -not $backendProcess.HasExited) {
        cmd /c "taskkill /PID $($backendProcess.Id) /T /F >NUL 2>&1"
    }
}
