@echo off

set baseDir=C:/IBM/WebSphere/scripts/automatedDeploy

copy c:\dev\ReleaseAutomation.ear C:\IBM\WebSphere\scripts\eardist
copy C:\IBM\WebSphere\scripts\eardist\win.xml C:\IBM\WebSphere\scripts\dist\

rem C:\IBM\WebSphere\AppServer\bin\setupcmdline.bat
C:/IBM/WebSphere/AppServer/bin/wsadmin -f %baseDir%/mainnew.py win.xml test -p %baseDir%/wsadmin_win.properties -lang jython -username zhufeng -password cyan87 

echo on
