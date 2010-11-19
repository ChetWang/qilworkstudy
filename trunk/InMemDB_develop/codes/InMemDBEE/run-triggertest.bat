SET LIBPATH=./WebContent/WEB-INF/lib
SET CP=./build/classes
setlocal enabledelayedexpansion
for %%j in (%LIBPATH%\*.jar) do (
    SET CP=!CP!;%%j
)
rem SET CP=!CP!;./bin
java -Xms256m -Xmx1024m -cp %CP% com.creaway.inmemdb.trigger.TriggerTest