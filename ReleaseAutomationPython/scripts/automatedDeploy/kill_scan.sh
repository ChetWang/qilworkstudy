#!/bin/ksh

ScanPID=`ps -ef | grep $USER | grep auto_scan.sh | grep -v grep | tr -s " " "*" | cut -f2 -d'*' | head -1`

if [ "$ScanPID" = "" ]; then
       echo "auto_scan.sh is not running."
       
else
       echo "auto_scan.sh is running. PID="$ScanPID
       echo "killing..."
       kill -9 $ScanPID
fi

echo "killed.\n"