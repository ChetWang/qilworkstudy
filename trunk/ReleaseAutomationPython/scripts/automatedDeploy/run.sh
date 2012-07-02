#---------------------------------------------------
# Release automation auto_scan
#---------------------------------------------------
#echo "Please input the username:"
#read username
#echo "Please input the password:"
#read password 

#---------------------------------------------------

ScanPID=`ps -ef | grep $USER | grep auto_scan.sh | grep -v grep | tr -s " " "*" | cut -f2 -d'*' | head -1`

if [ "$ScanPID" = "" ]; then

       echo "auto_scan is not running."
       echo "Starting Release Automation auto_scan script ..."

       logfile=/apps/WebSphere6/scripts/logs/auto_scan.log

       nohup /apps/WebSphere6/scripts/automatedDeploy/auto_scan.sh > $logfile 2>&1 &

       echo "Please check logfile:" $logfile
       
else
       echo "auto_scan is already running. PID="$ScanPID
       echo "exit"
fi


