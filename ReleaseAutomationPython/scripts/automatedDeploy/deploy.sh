#!/bin/ksh
# set -x
###################################################
# eSpear Release Automation v1.3
###################################################

echo "-------------------------------------"
echo "eSpear Release Automation script v1.3"
echo "-------------------------------------"

ScanPID=`ps -ef | grep $USER | grep auto_scan.sh | grep -v grep | tr -s " " "*" | cut -f2 -d'*' | head -1`

if [ "$ScanPID" -ne "" ]; then

   echo "auto_scan.sh is running. PID=$ScanPID"
   # echo "It's probably that we don't run auto_scan.sh in the future, so please stop it first."
   # exit 4
fi


# commandline argument
if [ $# -ne 1 ]; then 
   echo "Usage: $0 <xml_file> \n"
   exit 1 
fi

# check file access
filename=$1

if [[ ! -e $filename ]]; then
   echo "The xml file does not exist. Please check the filename and path: $filename"
   exit 2 
fi

if [[ ! -r $filename ]]; then
   echo "The xml file is not readable. Please make sure wasadmin has the read permission to this file."
   exit 3
fi

binDir=`dirname "$0"`
. "$binDir/setupCmdLine.sh"

# -------------------------------------------------
# Read backup dir from property file
# -------------------------------------------------
JythonMainFile=`cat $PropertiesDir | grep com.db.scripting.jython.mainpy | cut -f2 -d'='`
echo JythonMainFile: $JythonMainFile

# -------------------------------------------------
# copy applist.dtd to current folder
# -------------------------------------------------
echo "copy applist.dtd to current folder..."
cp /apps/WebSphere6/scripts/automatedDeploy/applist.dtd .

# -------------------------------------------------
# start
# -------------------------------------------------
echo "pwd: `pwd` "
echo "xml file: $filename"

# Validate the xml file, check if it has tag "earfilelocation"
EarTag="earfiles"
NoEarTag=0
TagNum=`cat $filename | grep -iw $EarTag | wc -l`

if [ $TagNum -gt "$NoEarTag" ]; then

   # Invoke wsadmin.sh to execute jython script to do the deploy work
   echo 'start deploy... \n'

   $WsadminDir -p $PropertiesDir -f $JythonMainFile $filename

   # track the final code of the python execution
   finalcode=$?
   echo "exitcode from jython: $finalcode"
   
   exit $finalcode

fi

# end