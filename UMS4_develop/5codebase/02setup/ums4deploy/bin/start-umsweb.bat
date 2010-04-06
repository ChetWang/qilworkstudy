@echo off
set CURRENT_DIR=%cd%
set JAVA_HOME=%CURRENT_DIR%\..\runtime
set CATALINA_HOME=%CURRENT_DIR%\..\webserver
%CURRENT_DIR%\..\webserver\bin\catalina.bat run
