@echo off

REM ------------------------------------------------------------------------
REM Licensed to the Apache Software Foundation (ASF) under one or more
REM contributor license agreements.  See the NOTICE file distributed with
REM this work for additional information regarding copyright ownership.
REM The ASF licenses this file to You under the Apache License, Version 2.0
REM (the "License"); you may not use this file except in compliance with
REM the License.  You may obtain a copy of the License at
REM
REM http://www.apache.org/licenses/LICENSE-2.0
REM
REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.
REM ------------------------------------------------------------------------

setlocal

rem Java Service Wrapper general NT service install script


if "%OS%"=="Windows_NT" goto nt
echo This script only works with NT-based versions of Windows.
goto :eof

:nt
rem
rem Find the application home.
rem
rem %~dp0 is location of current script under NT
set _REALPATH=%~dp0

set JXCZ_MSGTRANSACTOR_HOME=%~dp0..
set JAVA_HOME=E:/bin/java/jdk1.5.0_12

:conf
set WRAPPER_CONF="transactor_wrapper.conf"
set _JXCZ_MSGTRANSACTOR_HOME="set.JXCZ_MSGTRANSACTOR_HOME=%JXCZ_MSGTRANSACTOR_HOME%"
set _JAVA_HOME="set.JAVA_HOME=%JAVA_HOME%"

rem
rem Install the Wrapper as an NT service.
rem
:startup
"wrapper.exe" -i %WRAPPER_CONF% %_JXCZ_MSGTRANSACTOR_HOME% %_JAVA_HOME%
if not errorlevel 1 goto :eof
pause

