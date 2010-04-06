@echo off
title=UMS¿ØÖÆÌ¨
set CURRENT_DIR=%cd%
set JAVA_HOME=%CURRENT_DIR%\..\runtime
set ANT_HOME=%CURRENT_DIR%\ant
set UMS_HOME=%CURRENT_DIR%\..\umsserver
rem echo Current dir = %CURRENT_DIR%
rem echo ANT_HOME=%ANT_HOME%
rem echo UMS_HOME=%UMS_HOME%
%ANT_HOME%\bin\ant.bat -f %UMS_HOME%\build.xml runclient