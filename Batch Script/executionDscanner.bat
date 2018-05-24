echo off
set arg1=%1
set arg2=%2
set arg3=%3
set arg4=%4


dependency-check.bat --project %arg1% --enableExperimental --scan %arg2% --out %arg3% --format %arg4%