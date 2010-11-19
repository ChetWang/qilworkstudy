SET LIBPATH=./WebContent/WEB-INF/lib
SET CP=./build/classes
setlocal enabledelayedexpansion
for %%j in (%LIBPATH%\*.jar) do (
    SET CP=!CP!;%%j
)

java -cp %CP% com.creaway.inmemdb.core.InMemDBServer