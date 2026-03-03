@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
set "WRAPPER_DIR=%SCRIPT_DIR%.mvn\wrapper"
set "MAVEN_VERSION=3.9.11"
set "MAVEN_DIST_NAME=apache-maven-%MAVEN_VERSION%"
set "MAVEN_DIST_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/%MAVEN_DIST_NAME%-bin.zip"
set "MAVEN_DIR=%WRAPPER_DIR%\%MAVEN_DIST_NAME%"
set "MAVEN_BIN=%MAVEN_DIR%\bin\mvn.cmd"
set "MAVEN_ZIP=%WRAPPER_DIR%\%MAVEN_DIST_NAME%-bin.zip"

if exist "%MAVEN_BIN%" goto run_local_maven

where mvn >NUL 2>NUL
if %ERRORLEVEL%==0 goto run_system_maven

echo Maven was not found on PATH. Downloading %MAVEN_DIST_NAME%...

if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"

powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%MAVEN_DIST_URL%' -OutFile '%MAVEN_ZIP%'"
if not %ERRORLEVEL%==0 goto download_failed

powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%WRAPPER_DIR%' -Force"
if not %ERRORLEVEL%==0 goto extract_failed

if exist "%MAVEN_ZIP%" del /Q "%MAVEN_ZIP%"

if exist "%MAVEN_BIN%" goto run_local_maven

echo Failed to locate Maven executable at %MAVEN_BIN%.
exit /b 1

:run_local_maven
"%MAVEN_BIN%" %*
exit /b %ERRORLEVEL%

:run_system_maven
mvn %*
exit /b %ERRORLEVEL%

:download_failed
echo Failed to download Maven from:
echo %MAVEN_DIST_URL%
exit /b 1

:extract_failed
echo Failed to extract Maven archive:
echo %MAVEN_ZIP%
exit /b 1
