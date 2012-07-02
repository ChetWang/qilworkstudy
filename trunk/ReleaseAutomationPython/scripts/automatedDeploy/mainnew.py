###############################################################################
#          import            						      #
###############################################################################
import os
import sys
import socket
import shutil
import java
from com.db.automateddeploy.util import FileUtil
from java.lang import System
from java.util import Properties
from java.io import FileInputStream
from java.io import File

###############################################################################
#          initializing variables					      #
###############################################################################
version="1.3"
TarFileName=""
argNum=1
script_result=""

SCRIPTNAME = "Release Automation script version " + version
ScriptLocation = System.getProperty('com.db.scripting.jython.location')
LogLocation = System.getProperty('com.db.scripting.jython.logDir')
DistLocation = System.getProperty('com.db.scripting.jython.distDir')
BuildLocation = System.getProperty('com.db.scripting.jython.buildDir')
BackupLocation = System.getProperty('com.db.scripting.jython.backupDir')
DmgrProfileName = System.getProperty('com.db.scripting.jython.dmgrProfileName')

#-------------------------------------------------------------------------
# mail config
mail_disabled = System.getProperty('com.db.scripting.jython.mail.disabled')
mail_redirect = System.getProperty('com.db.scripting.jython.mail.redirect')
mail_smtphost = System.getProperty('com.db.scripting.jython.mail.smtphost')
mail_from = System.getProperty('com.db.scripting.jython.mail.from')
mail_cc = System.getProperty('com.db.scripting.jython.mail.cc')

# mail output
screen_output = []
lineSeparator = java.lang.System.getProperty('line.separator')
msg_hostname,aliaslist,ipaddrlist = socket.gethostbyaddr(socket.gethostname())

execfile(ScriptLocation+"/mail2.py" )

#-------------------------------------------------------------------------
PWD = System.getProperty('user.dir')
xml_filename = sys.argv[0]
xml_filepath = PWD + "/" + xml_filename

if (ScriptLocation==None or ScriptLocation==""):
        ScriptLocation = PWD
#endIf
if(LogLocation==None or LogLocation==""):
	LogLocation = PWD
#endIf
if(DistLocation==None or DistLocation==""):
	DistLocation = PWD
#endIf

BuildLocation = PWD

if ( not os.path.isdir(LogLocation)):
	os.makedirs( LogLocation )
# end if

if ( not os.path.isdir(BackupLocation)):
	os.makedirs( BackupLocation )
# end if


true = 1
false = 0
allapps=[]
nodeServerPairs = []
uniqueNodesContainedServers = []
nodesAutosyncs = []
clusters = []
testURLs = []
testResponses = []
appsNodesServers = []
errors = []
warnings = []
hasWebModule = false

ReleaseProperty=Properties()

wsadminSvr = AdminControl.queryNames("node="+AdminControl.getNode( )+",type=Server,*" )
v = wsadminSvr.find(",version=")
serverVers = wsadminSvr[v+9:]
v = serverVers.find(",")
serverVers = serverVers[0:v]
wsadminSvrId = AdminControl.getConfigId(wsadminSvr )
wsadminType = AdminConfig.showAttribute(wsadminSvrId, "serverType" )
wsadminVers = AdminControl.getAttribute(wsadminSvr, "platformVersion" )
wsadminConn = AdminControl.getType( )
wsadminServer = AdminControl.getAttribute(wsadminSvr, "name" )
wsadminNode = AdminControl.getNode( )
wsadminCell = AdminControl.getCell( )
wsadminHost = AdminControl.getHost( )
wsadminPort = AdminControl.getPort( )
wasRoot = ""
taskName = "Undefined Enterprise App Name"

###############################################################################
#          initializing logging system            			      #
###############################################################################

execfile(ScriptLocation+"/log-fail-highlight1.py" )
failOnError = "true"
logLevel = logConstant.get(System.getProperty('com.db.scripting.jython.logLevel'))

###############################################################################
#         loading external functions		     			      #
###############################################################################
execfile(ScriptLocation+"/deploy1.py" )
execfile(ScriptLocation+"/deploy-install-configure2.py" )
execfile(ScriptLocation+"/deploy-prepareNode2.py" )
execfile(ScriptLocation+"/getApplications.py" )
execfile(ScriptLocation+"/EAR-install-validate-uninstall-list1.py" )
execfile(ScriptLocation+"/APP-list-start-stop-test-exists1.py" )
execfile(ScriptLocation+"/ATTRIB-setTargets1.py" )
execfile(ScriptLocation+"/ATTRIB-show-set.py" )
execfile(ScriptLocation+"/ATTRIB-file.py" )
execfile(ScriptLocation+"/NODES-pairs-unique.py" )
execfile(ScriptLocation+"/NODE-save-restore-sync.py" )
execfile(ScriptLocation+"/SERVER-stop-sync-start.py" )
execfile(ScriptLocation+"/getEnvars.py" )

wasRoot = getEnvar( "was.install.root" )
wasRoot = convertToJaclPath(wasRoot )

highlight(MAJOR_, "Running " + SCRIPTNAME )

log(INFO_, "Preparing the deployment ... " )
log(INFO_, "wsadmin.sh: AdminType="+wsadminType )
log(INFO_, "wsadmin.sh: AdminVers="+wsadminVers )
log(INFO_, "wsadmin.sh: ServrVers="+serverVers )
log(INFO_, "wsadmin.sh: AdminCell="+wsadminCell )
log(INFO_, "wsadmin.sh: AdminNode="+wsadminNode )
log(INFO_, "wsadmin.sh: AdminConn="+wsadminConn )
log(INFO_, "wsadmin.sh: AdminHost="+wsadminHost )
log(INFO_, "wsadmin.sh: AdminSevr="+wsadminServer )
log(INFO_, "wsadmin.sh: AdminPort="+wsadminPort )

log(INFO_, "script.dir=" + ScriptLocation )
log(INFO_, "was.install.root=" + wasRoot )


###############################################################################
#          check the argvs          		  			      #
###############################################################################
log(DEBUG_, "checking the commandline parameters...")

if( len(sys.argv) < argNum ):
	script_result = ( SCRIPTNAME + " requires at lease %i params (xmlfile )") % argNum
	log(ERROR_, script_result)
	
	fail( "Insufficient commandline parameters!" )	
#endIf

log( MAJOR_, "commandline parameters check finished.")


###############################################################################
#          read xml file		     			              #
###############################################################################
try:
	log(INFO_, "deployment xml file: " + xml_filepath )
	allapps = FileUtil.readAppConfig( xml_filename )
		
except java.lang.Exception, e:
	log( ERROR_, "The xml definition file is invalid!\nMessage: " + e.getMessage() )
	fail( e.getMessage() )

#end of try


###############################################################################
#          start the deployment		     			              #
###############################################################################
highlight( MAJOR_, "Starting deployment... " )

# LOG HISTORY
logvalue = []
for appitem in allapps:
	
	ReleaseProperty.put( "ENTERPRISE_APP_NAME", appitem[0] )
	taskName=appitem[0]
	appTargets = [appitem[2],appitem[3],appitem[4],appitem[5]]
	
	BackupLocation = BackupLocation + "/" + taskName + "_" + TimeStamp2()
	logvalue = BackupLocation
	
	if ( not os.path.isdir(BackupLocation)):
		os.makedirs( BackupLocation )
	#endif
	
	#---------------------------------------------
	# backup xml file
	#---------------------------------------------
	shutil.copyfile( xml_filename , BackupLocation + "/" + os.path.basename(xml_filename))
	
	for earitem in appitem[1]:
		
		log(INFO_, "ear file: " + earitem)
		
		if ( not os.path.isfile(earitem) ):
			fail("The path of ear file is invalid: " + `earitem`)
		#endif
			
		#---------------------------------------------
		# backup ear file
		#---------------------------------------------
		earitembasename = os.path.basename(earitem)
		shutil.copyfile( earitem , BackupLocation + "/" + earitembasename )
		
		log(INFO_, "Backup location: " + BackupLocation)
		
		#---------------------------------------------
		# deploy
		#---------------------------------------------
		deploy( BuildLocation , wasRoot )
	#endfor
	
	
	###############################################################################
	#          finish the deployment		     			      #
	###############################################################################
	log(MAJOR_, "log deployment history...")
	if( len(errors) == 0 ):
		
		loghistory(ReleaseProperty.getProperty("ENTERPRISE_APP_NAME"), SUCCESS_)
		
		for earitem in appitem[1]:
			logvalue=logvalue + "," + earitem
		#end for
			
		loglastsuccess(ReleaseProperty.getProperty("ENTERPRISE_APP_NAME"), logvalue)
		
	else:
		loghistory(ReleaseProperty.getProperty("ENTERPRISE_APP_NAME"), FAIL_)
		
	#endif
	
	highlight(MAJOR_, "Send email notifications...")
	sendMailNotification()

#endFor

log( MAJOR_, "Script finished." )
log( DEBUG_, "\n\n\n" )

if( len(errors) != 0 ):
	sys.exit("Error happend during this deployment.")
# endif

###############################################################################
#          END				     			              #
###############################################################################