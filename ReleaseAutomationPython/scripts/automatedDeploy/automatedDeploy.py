#
# Author: Barry Searle
#
# (C) Copyright IBM Corp. 2004,2006 - All Rights Reserved.
# DISCLAIMER:
# The following source code is sample code created by IBM Corporation.
# This sample code is not part of any standard IBM product and is provided
# to you solely for the purpose of assisting you in the development of your
# applications. The code is provided 'AS IS', without warranty or condition
# of any kind. IBM shall not be liable for any damages arising out of your
# use of the sample code, even if IBM has been advised of the possibility of
# such damages.
#
# Change History:
# 2.0 (10feb2006) initial Jython version, several code changes (envar handling)
# 1.3 (22apr2005) removed DEPLOY_WAS, optional DistDir, uses getEnvars
# 1.2 (14jan2005) restructured (future: will read DEPLOY_XXXX envars or invocation parameters)
# 1.1 (17nov2004) initial shipped version
#

version = "2.0"

import sys
print "@@ sys.path="+`sys.path`
print "@@ sys.prefix="+`sys.prefix`

print ">>> automatedDeploy.py <<<"

#print "registry_all="+`sys.registry`
ScriptLocation = java.lang.System.getProperty('com.db.scripting.jython.location')
LogLocation = java.lang.System.getProperty('com.db.scripting.jython.logDir')
if (ScriptLocation==None or ScriptLocation==""):
        ScriptLocation = java.lang.System.getProperty('user.dir')
#endIf
if(LogLocation==None or LogLocation==""):
	LogLocation = java.lang.System.getProperty('user.dir')
#endIf

ScriptLocation += "/"
LogLocation += "/"

execfile(ScriptLocation+"/log-fail-highlight.py" )
failOnError = "true"
logLevel = INFO_

debugHighlight(INFO_, "sys.argv[0]="+`sys.argv[0]`)

true = 1
false = 0
nodeServerPairs = []
uniqueNodesContainedServers = []
nodesAutosyncs = []
clusters = []
testURLs = []
testResponses = []
appsNodesServers = []
errors = []
warnings = []

execfile(ScriptLocation+"/deploy.py" )
execfile(ScriptLocation+"/deploy-install-configure.py" )
execfile(ScriptLocation+"/deploy-prepareNodes.py" )
execfile(ScriptLocation+"/getApplications.py" )
execfile(ScriptLocation+"/getTargets.py" )
execfile(ScriptLocation+"/EAR-install-validate-uninstall-list.py" )
execfile(ScriptLocation+"/APP-list-start-stop-test-exists.py" )
execfile(ScriptLocation+"/ATTRIB-setTargets.py" )
execfile(ScriptLocation+"/ATTRIB-show-set.py" )
execfile(ScriptLocation+"/ATTRIB-file.py" )
execfile(ScriptLocation+"/NODES-pairs-unique.py" )
execfile(ScriptLocation+"/NODE-save-restore-sync.py" )
execfile(ScriptLocation+"/SERVER-stop-sync-start.py" )
execfile(ScriptLocation+"/getEnvars.py" )

print ""
print ""
SCRIPTNAME = "automatedDeploy.py - Version "+version
highlight(MAJOR_, "running "+SCRIPTNAME )
print ""

wasRoot = getEnvar("was.install.root" )
wasRoot = convertToJaclPath(wasRoot )
log(INFO_, "envar script.dir="+ScriptLocation )
log(INFO_, "envar was.install.root="+wasRoot )

action = ""
depType = ""
failOnError = ""
distDir = ""

base = 0
if(len(sys.argv)>0):
        param1 = sys.argv[0]
        param1 = param1[len(param1)-3:].lower()
        if(param1==".py" or param1==".jy"):
                base = 1
        #endIf
#endIf
if ( len(sys.argv) > (base+0) ):
        action = sys.argv[base+0]
        if (len(sys.argv) > (base+1) ):
                depType = sys.argv[(base+1)]
                if (len(sys.argv) > (base+2) ):
                        failOnError = sys.argv[(base+2)]
                        if (len(sys.argv) > (base+3) ):
                                distDir = sys.argv[(base+3)]
                                if (len(sys.argv) > (base+4) ):
                                        extra = sys.argv[(base+4)]
                                        log(ERROR_, SCRIPTNAME+": only accepts 4 parameters, ignoring "+extra )
                                #endIf
                        #endIf
                #endIf
        #endIf
elif (len(action) == 0):
        proc_result = SCRIPTNAME+" requires 1-4 params (action depType failonerror distDir)"
        log(ERROR_, proc_result )
        sys.exit( proc_result)
#endIf
if (len(action) <= 0):
        action = "confirm"
#endIf
if (len(depType) <= 0):
        depType = "-pilot"
#endIf
if (len(failOnError) <= 0):
        failOnError = "true"
#endIf
if (len(distDir) <= 0):
        distDir = ScriptLocation+"dist"
#endIf
distDir = convertToJaclPath(distDir )

log(MAJOR_, SCRIPTNAME+": INPUTS:(default=confirm)action=      "+action )
log(MAJOR_, SCRIPTNAME+": INPUTS:(default=-pilot) depType=     "+depType )
log(MAJOR_, SCRIPTNAME+": INPUTS:(default=true)   failOnError= "+failOnError )
log(MAJOR_, SCRIPTNAME+": INPUTS:(default=./dist) distDir=     "+distDir )
log(MAJOR_, SCRIPTNAME+": INPUTS:(unused)         wasRoot=     "+wasRoot )

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
if (wsadminType != "DEPLOYMENT_MANAGER"):
        fail(SCRIPTNAME+" currently only tested for AppServers connected to NetworkDeployment DeploymentManager" )
#endIf
if (wsadminConn != "SOAP"):
        fail(SCRIPTNAME+" currently only tested for AppServers connected using type=SOAP" )
#endIf
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminType="+wsadminType )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminVers="+wsadminVers )
log(MAJOR_, SCRIPTNAME+": WSADMIN: ServrVers="+serverVers )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminCell="+wsadminCell )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminNode="+wsadminNode )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminConn="+wsadminConn )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminHost="+wsadminHost )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminSevr="+wsadminServer )
log(MAJOR_, SCRIPTNAME+": WSADMIN: AdminPort="+wsadminPort )

JaclVers = sys.version[0:3]   #?PROBLEM? (jacl 116) INFO_NUMARGS!=2?  "tclversion"
log(VERBOSE_, "JYTHON vers="+JaclVers )
WasTclVers = 0

log(MAJOR_, SCRIPTNAME+": invoking deploy "+action+" "+depType+" "+failOnError+" "+distDir+" "+`wasRoot` )
log(INFO_, "" )
deploy(action, depType, failOnError, distDir, wasRoot )

print ""
print ""
print ""
if (len(errors) > 0):
        log(DEBUG_, "errors.length="+`len(errors)` )
        msgs = ""
        for msg in errors:
                msgs = msgs+"\n"+msg
        #endFor
        debugHighlight(ERROR_, "ERRORS during DEPLOYMENT:"+msgs )
#endIf
if (len(warnings) > 0):
        log(DEBUG_, "warnings.length="+`len(warnings)` )
        msgs = ""
        for msg in warnings:
                msgs = msgs+"\n"+msg
        #endFor
        highlight(WARNING_, "WARNINGS during DEPLOYMENT:"+msgs )
#endIf
if ( len(warnings) == 0 ):
        highlight(MAJOR_, "No errors, no warnings during automatedDeployment (action="+action+")" )
#endIf

print ""
highlight(MAJOR_, SCRIPTNAME+" DONE."  )
print ""
print ""
