@echo off
rem set JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,address=3999,server=y,suspend=n
java %JAVA_OPTS% -cp mas-api3.1.jar com.jasson.mas.api.demo.APIDemo