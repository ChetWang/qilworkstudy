SET INMEMDB_HOME=D:/documents/work/InMemDB_develop/codes/InMemDBEE/
SET LIBPATH=%INMEMDB_HOME%/WebContent/WEB-INF/lib
set CLASSPATH=%CLASSPATH%;%INMEMDB_HOME%/build/classes

setlocal enabledelayedexpansion
for %%j in (%LIBPATH%\*.jar) do (
    SET CLASSPATH=!CLASSPATH!;%%j
)