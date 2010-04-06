@echo off
title=UMS Server
set CURRENT_DIR=%cd%
set ANT_HOME=%CURRENT_DIR%\ant
set UMS_HOME=%CURRENT_DIR%\..\umsserver
set JAVA_HOME=%CURRENT_DIR%\..\runtime
rem echo Current dir = %CURRENT_DIR%
rem echo ANT_HOME=%ANT_HOME%
rem echo UMS_HOME=%UMS_HOME%
%ANT_HOME%\bin\ant.bat -f %UMS_HOME%\build.xml runserver