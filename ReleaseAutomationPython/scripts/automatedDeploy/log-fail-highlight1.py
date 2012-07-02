
import sys
import time
from time import gmtime, strftime
from com.db.automateddeploy.util import LastSuccessRecord
from java.lang import StringBuffer



#-----------------------------------------------------------
# Print with timestamp
#-----------------------------------------------------------
def TimeStamp():
	return '[' + time.strftime('%Y-%m-%d %H:%M:%S') + '] '
	
def TimeStamp2():
	return time.strftime('%Y%m%d_%H%M%S')

def wsadminToList(inStr):
        outList=[]
        if (len(inStr)>0 and inStr[0]=='[' and inStr[-1]==']'):
                tmpList = inStr[1:-1].split(" ")
        else:
                tmpList = inStr.split("\n")  #splits for Windows or Linux
        for item in tmpList:
                item = item.rstrip();        #removes any Windows "\r"
                if (len(item)>0):
                        outList.append(item)
        return outList
#endDef


#level
DEBUG_ = 5
VERBOSE_ = 4
INFO_ = 3
MAJOR_ = 2
WARNING_ = 1
ERROR_ = 0

logConstant = {
	"DEBUG_":DEBUG_,
	"VERBOSE_":VERBOSE_,
	"INFO_":INFO_,
	"MAJOR_":MAJOR_,
	"WARNING_":WARNING_,
	"ERROR_":ERROR_
}

#status
SUCCESS_=0
FAIL_=1

#open log file according to the level of the log infomation
#write DEBUG, VERBOSE, INFO_, MAJOR_ infomation to StdOut.log file
#write WARNING, ERROR_ infomation to StdErr.log file
def openLogFile( level ):
	
	global LogLocation
	logFile = open(LogLocation + "/SystemOut.log", "a")
	return logFile
	
#endDef

#save and close the log file
def saveAndCloseFile( logFile ):
	logFile.flush()
	logFile.close()
#endDef


def checkErrorsWarnings ( level, message ):
	
        global ScriptLocation
        execfile(ScriptLocation + "/Definitions.py" )
        global errors
        global warnings

        if (( level == ERROR_ ) ):
                message = "ERROR: "+ message
                errors.append(message)
        elif (( level == WARNING_ ) ):
                message = "WARNING: "+ message
                warnings.append(message)
        #endIf
        return message
#endDef



def log ( level, message ):
	
        global ScriptLocation 
        execfile(ScriptLocation + "/Definitions.py" )
        global logLevel        
        global screen_output
        
        logFile=openLogFile( level )        
        log_message = []
        
        # pre-processing log message:
        checkErrorsWarnings(level, message )
        log_message = TimeStamp() + message
        
        # write every log message to logfile:
        logFile.write( log_message + "\n" )
        saveAndCloseFile(logFile)
        
        # collect quolified logs for email:
        if (( level <= logLevel ) ):
                screen_output.append(log_message + "\n" )                
        #endIf
        
        # print MAJOR logs
        if (level <= MAJOR_):
        	print log_message
        #endIf
        
#endDef

def fail ( msg ):
	
	global ScriptLocation

	execfile(ScriptLocation + "/Definitions.py" )
	global failOnError 
	global errors 
	global taskName
	
	msg = "FAILURE - " + msg
	
	log( ERROR_, "Deployment failed: "+ msg)
	debugHighlight(ERROR_, "The Web deployment has failed.")
	
	sendMailNotification()
	
	if (failOnError):
		if( taskName == "" ):
			taskName="Undefined Enterprise Application Name"
    	        #endif
    	        loghistory( taskName , FAIL_ )
    	        log( ERROR_, msg )
    	        sys.exit(msg)
        #endIf
        
#endDef

def debugHighlight ( level, message ):
	
	delimitor = "#######################################################################"
	log (3, delimitor)
	log (level, message)
	log (3, delimitor)
#endDef

def highlight ( level, message ):
	
	delimitor = "======================================================================="
	log (3, delimitor)
	log (level, message)
	log (3, delimitor)
	
#endDef

def lowlight ( level, message ):
        delimitor = "------------------------------------------------------------"
	log (3, delimitor)
	log (level, message)
	log (3, delimitor)
        
#endDef

def loghistory( taskname , status ):
	
	global screen_output
	
	logFile=open(LogLocation + "/History.log", "a")
	historyMessage = strftime("%a, %d %b %Y %H:%M:%S", gmtime()) + " | " + taskname + " | "
	if ( status == SUCCESS_ ):
		historyMessage = historyMessage + "Successful"
		
	elif ( status == FAIL_ ):
		historyMessage = historyMessage + "Failed"
	#endIf
	
	screen_output.append(historyMessage + "\n" )
	logFile.write( historyMessage + "\n" )
	saveAndCloseFile(logFile)
	
#endDef


def loglastsuccess( appname , baselocation ):
	LastSuccessRecord.storeLastSuccess( LogLocation + "/Lastsuccess.log", appname, baselocation )
#endDef

