$ErrorActionPreference = "Stop"

if (Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue) {
    Write-Error "Port 8080 is already in use. Stop the existing backend process first."
    exit 1
}

Set-Location (Join-Path $PSScriptRoot "backend")
powershell -NoProfile -ExecutionPolicy Bypass -File .\run-backend.ps1
