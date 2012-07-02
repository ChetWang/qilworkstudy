
def stopSyncStart ( action, nodeName, containedServerNames ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        containedAppNames = ""
        
        #if (action == "update"):
        lowlight(MAJOR_, "Stopping all affected AppServers..." )
                
        for serverName in containedServerNames:
               	stopNDServer(nodeName, serverName )
        #endFor
        #endIf
        
        syncNDNode(nodeName )
        
        #if (action == "update"):
        lowlight(MAJOR_, "Restarting all affected AppServers..." )
        	
        for serverName in containedServerNames:
               	startNDServer(nodeName, serverName )
        #endFor
        #endElse
                
#endDef

def syncNDNode ( nodeName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(MAJOR_, "Synchronizing node " + nodeName + " ..." )
        
        EarExpandDelay = 15
        
        ndSync = AdminControl.completeObjectName("type=NodeSync,node="+nodeName+",*" )
        if (ndSync == ""):
                fail("cannot syncNDNode (stopped?) nodeName="+nodeName )
        #endIf
        
        try:
        	sync = AdminControl.invoke(ndSync, "sync" )
        except:
        	log(WARNING_, "Exception - sync nodes " )
        #endTry
        
        # log(DEBUG_, "syncNDNode[]: check for nodeSync EAR expansion complete (for now just delay "+`EarExpandDelay`+" secs)" )
        sleepDelay(EarExpandDelay )
        
        log(INFO_, "Affected node [" + nodeName + "] synchronized.")
#endDef

def startNDServer ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(INFO_, "startNDServer: nodeName="+nodeName+", serverName="+serverName+" ..." )
        
        # replace wsAdmin.startserver with NodeAgent.launchProcess+Wait
        
        started = ""
        try:
                _excp_ = 0
                started = AdminControl.startServer(serverName, nodeName, 300 )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        
        temp = _excp_
        log(INFO_, "startNDServer: return="+`temp`+", started="+started )
        
        retries = 0
        while ((temp > 0) and (retries < 5) ):
                retries = retries+1
                log(WARNING_, "startNDServer: start failed, StatusCode="+`temp` +", retries="+`retries`+" ..." )
                try:
                        _excp_ = 0
                        started = AdminControl.startServer(serverName, nodeName, 300 )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                
                temp = _excp_
                log(INFO_, "startNDServer: return="+`temp`+", started="+started )
                
                sleepDelay(10)
        #endWhile
        
        checkServerStarted(nodeName, serverName )
        log(VERBOSE_, "startNDServer: DONE." )
#endDef

def checkServerStarted ( nodeName, serverName ):
        
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(DEBUG_, "checkServerStarted: nodeName="+nodeName+" serverName="+serverName+" ..." )
        
        serverID = ""
        retries = -1
        while ((len(serverID) == 0) and (retries < 30) ):
                
                sleepDelay(10)
                
                retries = retries+1
                log(DEBUG_, "checkServerStarted: checking status, retries=" +`retries` )
                
                try:
                        _excp_ = 0
                        serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
                        
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                
                temp = _excp_
                if (temp != 0):
                        log(WARNING_, "checkServerStarted AdminControl exception="+`temp`+" serverID="+serverID )
                #endIf
                
                
        #endWhile
                
        if (serverID == ""):
                fail("checkServerStarted: " +serverName +" on " +nodeName + " FAILED TO START." )
                return
        else:
        	if (retries > 0):
                	log(WARNING_, "checkServerStarted: " +serverName +" on " +nodeName + " had slow start." )
        	#endIf
        #endIf
        
        try:
        	state = AdminControl.getAttribute(serverID, "state" )
        except:
        	log(WARNING_, "Exception - checkServerStarted")
        #endTry
        
        state = state.upper()
        if (state == "STARTED"):
                log(MAJOR_, "AppServer " +serverName +" on " +nodeName + " is " + state )
        else:
                fail("AppServer " +serverName +" on " +nodeName + " is " + state )
        #endElse
        
        log(DEBUG_, "checkServerStarted: DONE." )
#endDef

def stopNDServer ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        try:
        	serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
        except:
        	log(WARNING_, "Exception - failed to query server state " + serverName + " " + nodeName)
        #endTry
        
        if (len(serverID) == 0):
                msg = "stopNDServer: cannot access node="+nodeName+" server="+serverName+" state=STOPPED"
                log(WARNING_, msg )
                return
        #endIf
        
        try:
        	state = AdminControl.getAttribute(serverID, "state" )
        except:
        	log(WARNING_, "Exception - failed to query server state " + serverID )
        #endTry
        	
        state = state.upper()
        log(INFO_, "Stopping AppServer: nodeName="+nodeName+" serverName="+serverName+" state="+state )
        
        if (state== "STOPPED"):
                log(INFO_, "AppServer " + nodeName+" "+serverName+" is already stopped." )
        else:
                try:
                	stopped = AdminControl.stopServer(serverName, nodeName, "immediate" )
                except:
                	log(WARNING_, "Exception - failed to stop Server " + nodeName+" "+serverName)
                #EndTry
                
                if (len(stopped) != 0):
                        log(VERBOSE_, "stopNDServer: stopServer response="+stopped )
                #endIf
                
                checkServerStopped(nodeName, serverName )
        #endElse
        
        log(DEBUG_, "stopNDServer: DONE." )
#endDef

def checkServerStopped ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(DEBUG_, "checkServerStopped: nodeName="+nodeName+" serverName="+serverName )
        
        desiredState = "STOPPED"
        serverID = ""
        try:
                _excp_ = 0
                serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        
        temp = _excp_
        if (temp != 0):
                log(WARNING_, "checkServerStopped: exception="+`temp`+" trying to access "+nodeName+" "+serverName )
        #endIf
        if (len(serverID) == 0):
                log(VERBOSE_, "checkServerStopped: cannot access node="+nodeName+" server="+serverName+" (STOPPED)" )
                actualState = desiredState
        else:
                try:
                	actualState = AdminControl.getAttribute(serverID, "state" )
                except:
                	log(WARNING_, "Exception - check Server actualState")
                #endTry
        #endElse
        
        actualState = actualState.upper()
        log(MAJOR_, "AppServer "+serverName+" on "+nodeName+" is "+actualState )
        
        if (actualState != desiredState):
                msg = "ERROR: checkServerStopped: "+nodeName+" "+serverName+" actualState="+actualState+" instead of desiredState="+desiredState
                fail(msg )
        #endIf
#endDef

def sleepDelay ( secs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        import time
        tstart = time.strftime( "%H:%M:%S", time.localtime() )
        #IGNORE: package require java
        try:
                _excp_ = 0
                java.lang.Thread.sleep( (secs * 1000) )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        
        temp = _excp_
        tdone = time.strftime( "%H:%M:%S", time.localtime() )
        log(VERBOSE_, "sleepDelay seconds="+`secs`+" start="+tstart+" done="+tdone )
#endDef

