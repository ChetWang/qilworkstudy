#!/bin/ksh
set -x
#####################################################################
# Release Automation roll-back script
#   usage: rollback.sh <ApplicationName>
# 
#####################################################################

binDir=`dirname "$0"`
. "$binDir/setupCmdLine.sh"


#validate the parameter
if [ $# -ne 1 ]
then 
	echo "You must provide the Application name"
	echo 'usage:  rollback.sh <ApplicationName>' 
	echo ''
	exit 1
fi


logloc=` cat $PropertiesDir | grep logDir | cut -f2 -d"=" `
distloc=` cat $PropertiesDir | grep distDir | cut -f2 -d"=" `
logloc="$logloc/Lastsuccess.log"


#load the successful history from logloc
echo "Read the successful history from $logloc"
sucloc=`cat $logloc | grep $1 | cut -f2 -d"=" | cut -f1 -d","`
earloc=`cat $logloc | grep $1 | cut -f2 -d"=" | cut -f2-100 -d","`

#validate the successful history
if [ "$sucloc" = "" ]
then
	echo "There is no successful installation for application $1"
	exit 2
fi


#recovery the ear file from the backup directory
IFS=','
for earitem in $earloc
do
	earname=`echo $earitem | awk -F/ '{print $NF}'`
	src="$sucloc/$earname"
	cp $src $earitem
done


#recover the xml file from the backup directory
xmls=`find $sucloc -type f -name "*.xml"`
for xml in $xmls
do
	xmlname=`echo $xml | awk -F/ '{print $NF}'`
	echo $xml
	echo "$distloc/$xmlname"
	cp $xml "$distloc/$xmlname"
done 
