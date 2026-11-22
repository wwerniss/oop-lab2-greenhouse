$ErrorActionPreference = 'Stop'
if (-not (Test-Path 'pom.xml')) { throw 'Open PowerShell in the project root.' }
New-Item -ItemType Directory -Force -Path 'target/classes' | Out-Null
$javaFiles = @(Get-ChildItem -Path 'src/main/java' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
if ($javaFiles.Count -eq 0) { throw 'Java sources were not found.' }
& javac --release 17 -encoding UTF-8 -d 'target/classes' @javaFiles
if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed.' }
Copy-Item 'src/main/resources/logging.properties' 'target/classes/logging.properties' -Force
& java -cp 'target/classes' ua.edu.oop.greenhouse.Main
if ($LASTEXITCODE -ne 0) { throw 'Application execution failed.' }
Write-Host 'Created target/flowers-grouped.xml' -ForegroundColor Green
