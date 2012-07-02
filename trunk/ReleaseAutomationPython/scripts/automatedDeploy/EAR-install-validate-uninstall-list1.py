#
#
#
import os
import time
import stat


def validateEAR ( appPath ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "validateEAR[]: installed EAR-FILE validation" )
#endDef

def installEAR ( action, appPath, appName, clusterName, nodeName, serverName ):
        
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(DEBUG_, "AdminApp.install serverName=" + `serverName`)
        log(DEBUG_, "AdminApp.install nodeName=" + `nodeName`)
        log(DEBUG_, "AdminApp.install appPath=" + `appPath`)
        log(DEBUG_, "AdminApp.install appName=" + `appName`)
        log(DEBUG_, "AdminApp.install clusterName=" + `clusterName`)
        log(DEBUG_, "AdminApp.install action=" + `action`)
        
        # -----------------------------------------------------
        # application install options
        # -----------------------------------------------------
        update = ""
        if (action == "update"):
                update = "-update "
        #endIf
        update = update + "-appname '" + appName + "'"
        
        common_options = " -verbose -usedefaultbindings -useMetaDataFromBinary -reloadEnabled -reloadInterval 10 -distributeApp "
        	
        if (serverName != [] and nodeName != []):
                options = update + " -node " + nodeName + " -server " + serverName + common_options
        #endIf
        elif (clusterName != []):
		options = update + " -cluster " + clusterName + common_options
        #endIf
        else:
                msg = "ERROR: installEAR: no serverName/nodeName nor clusterName specified."
                fail(msg )
        #endElse
        
        
        # -----------------------------------------------------
        # INSTALL
        # -----------------------------------------------------
        log(INFO_,"installEAR: AdminApp.install " + appPath + " " + options)
        
        try:
        	_excp_ = 0
        	installed = AdminApp.install(appPath, options)
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                log(ERROR_, "AdminApp.install failed. Exception=" + `_value_`)
                _excp_ = 1
        #endTry
                
        temp = _excp_
        if (temp != 0):
                msg = "Exception installing EAR " + `appPath`
                fail(msg )
        #endIf
        
        
        # -----------------------------------------------------
        # CHECK
        # -----------------------------------------------------
        if (len(installed) > 0):
                log(INFO_, installed )
        #endIf
        
        appExists = checkIfAppExists( appName )
        if (appExists):
                pass
        else:
                fail("installEAR failed. application=" + `appName` )
        #endElse
        log(VERBOSE_, "InstallEAR: DONE." )
#endDef



def uninstallEAR ( appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "UninstallEAR: "+appName+"..." )
        uninstalled = AdminApp.uninstall(appName )
        log(INFO_, uninstalled )
        appExists = checkIfAppExists(appName )
        if (appExists):
                fail("failed to uninstallEAR application="+appName )
        #endIf
        log(VERBOSE_, "UninstallEAR: DONE." )
#endDef
