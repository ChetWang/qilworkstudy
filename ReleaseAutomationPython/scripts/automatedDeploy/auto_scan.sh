#!/bin/ksh

echo "Auto_scan started..."

binDir=`dirname "$0"`
. "$binDir/setupCmdLine.sh"


ProcessCount=`ps -ef | grep auto_scan.sh | wc -l`

Const=2
# Start auto scan if there is no script auto_scan.sh running
if [ $ProcessCount -le $Const ]
then
  # Read scan dir from property file
  JythonDistDir="com.db.scripting.jython.distDir"
  ScanDir=`cat $PropertiesDir | grep $JythonDistDir | cut -f2 -d'='`
  echo $ScanDir
  
  cd $ScanDir
  
  # Read the main py file
  JythonMainFile=`cat $PropertiesDir | grep com.db.scripting.jython.mainpy | cut -f2 -d'='`
  echo $JythonMainFile
  
  # Read the interval value
  JythonInterval=`cat $PropertiesDir | grep com.db.scripting.jython.interval | cut -f2 -d'='`
  echo $JythonInterval
  
  while true
  do
          
     # Get xml file name list in the scan dir
     xmlFiles=`find $ScanDir -type f -name "*.xml" | sort`
     for file in $xmlFiles
     do
     	
        echo "start the scheduled job"
      
        count=`echo $file | tr "/" " " | wc -w`
        field=`expr $count + 1`
        filename=`echo $file | cut -f$field -d'/'`
        appname=`cat $file | grep EnterpriseApp | grep name= | cut -f2 -d'"'`
        echo $filename
        echo $appname
                  
        # Validate the xml file, check if it has tag "earfilelocation" 
        EarTag="earfiles"
        NoEarTag=0
        TagNum=`cat $file | grep -iw $EarTag | wc -l`
                  
        if [ $TagNum -gt "$NoEarTag" ]; then
                    
           # Invoke wsadmin.sh to execute python script to do auto deploy work
           $WsadminDir -p $PropertiesDir -f $JythonMainFile $filename
        
        fi
                        
        # remove xml file
        rm $file
        
        echo "finish the scheduled job \n\n"
     done
          
     sleep $JythonInterval
     
  done
  
fi


