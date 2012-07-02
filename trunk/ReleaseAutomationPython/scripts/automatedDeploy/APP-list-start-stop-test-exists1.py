#
#
#
def PreValidateApplicationsExist ( appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        appExists = checkIfAppExists(appName )
        if (appExists):
        	log(INFO_, "PreValidate Applications OK, Application [" +appName + "] exists.")
        else:
        	fail("PreValidateApplicationsPresent: MISSING application="+appName )
        #endElse
#endDef


def PreValidateApplicationsAbsent ( appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        appExists = checkIfAppExists(appName )
        if (appExists):
        	fail("PreValidateApplicationsAbsent: failed. Application [" +appName + "] exists.")
        else:
        	log(INFO_, "PreValidateApplicationsAbsent: OK")
        #endElse
        
#endDef


def validateApplication ( appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        AdminConfig.validate(AdminConfig.getid("/Deployment:"+appName+"/" ) )
#endDef

def listApplications ():
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(DEBUG_, "ListApplications:" )
        apps = AdminApp.list( )
        apps = wsadminToList(apps) # running on windows, target is linux (different SEPARATOR)
        for app in apps:
                log(DEBUG_, " > "+app )
        #endFor
        log(DEBUG_, "ListApplications: DONE." )
#endDef

def checkIfAppExists ( appName ):
	
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        appExists = true
        application = AdminConfig.getid("/Deployment:"+appName+"/" )
        
        if (len(application) == 0):
                appExists = false
        #endElse
        
        return appExists
#endDef

def StartApplication ( appName, nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(INFO_, "StartApplication: appName='"+appName+"' nodeName="+nodeName+" serverName="+serverName+" ..." )
        
        appMgrID = AdminControl.queryNames("type=ApplicationManager,node="+nodeName+",process="+serverName+",*" )
        length = len(appMgrID)
        
        log(DEBUG_, "StartApplication: appMgrID="+appMgrID )
        
        if (length >= 1):
                
                try:
                        _excp_ = 0
                        started = AdminControl.invoke(appMgrID, "startApplication", `appName` )
                except java.lang.Exception, e:
			log( ERROR_,e.getMessage )
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp > 0):
                        log(WARNING_, "StartApplication: Exception trying to start '"+appName+"' "+nodeName+" "+serverName )
                        return
                else:
                        if (len(started) > 0):
                                log(INFO_, started )
                        #endIf
                #endElse
        else:
                log(ERROR_, "StartApplication: appMgr ERROR, NOT ACCESSABLE, cannot start '"+appName + "'")
        #endElse
        checkApplicationRunning(nodeName, serverName, appName )
        
#endDef

def StopApplication ( appName, nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "StopApplication: appName="+appName+" nodeName="+nodeName+" serverName="+serverName+" ..." )
        appMgrID = AdminControl.queryNames("type=ApplicationManager,node="+nodeName+",process="+serverName+",*" )
        length = len(appMgrID)
        log(VERBOSE_, "StopApplication: appMgrID.length="+`length`+" appMgrID="+appMgrID )
        if (length >= 1):
                log(DEBUG_, "StopApplication: stopping "+appName+"  ..." )
                stopped = ""
                try:
                        _excp_ = 0
                        stopped = AdminControl.invoke(appMgrID, "stopApplication", appName )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp > 0):
                        log(WARNING_, "StopApplication: Exception trying to stop "+appName+" "+nodeName+" "+serverName )
                else:
                        if (len(stopped) > 0):
                                log(DEBUG_, stopped )
                        #endIf
                #endElse
        else:
                log(ERROR_, "StopApplication: appMgr ERROR, NOT ACCESSABLE, cannot stop "+appName )
        #endElse
        log(VERBOSE_, "StopApplication: DONE." )
#endDef

def testApplication ( testURLs, testResponses ):
        
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        execfile(ScriptLocation+"/SERVER-stop-sync-start.py" )
        global WasTclVers
        
        sleepDelay(30 )
        
        indx = 0
        length = len(testURLs)
        
        while (indx < length):
                url = testURLs[indx]
                response = testResponses[indx]
                
                log(INFO_, "testApplication: testURL="+url+" expectedResponse="+response )
                
                indx = indx+1
                lines = readWebPage(url )
                
                if (lines==None or len(lines) == 0):
                        if (WasTclVers > 5):
                                lowlight(ERROR_, "testApplication FAILED. No response from url="+url)
                        else:
                                log(WARNING_, "testApplication: (v51 Tcl/Jacl) FAILED CONNECT: url="+url+"  expectedResponse="+response )
                        #endElse
                        continue
                #endIf
                
                response_text = ""
                found = false
                for line in lines:
                	response_text = response_text + "\n" + line
                        if (line.find(response) >= 0):
                                found = true
                        #endIf
                #endFor
                
                if (found):
                        log(INFO_, "testApplication: SUCCESS. The expected response string is received from test URL." )
                else:
                        lowlight(ERROR_, "testApplication: FAILED. The response from server does not contain the expected response string." )
                        log(INFO_, "Server response: \n" + response_text)
                #endFor
                #endElse
        #endWhile
#endDef

def readWebPage ( webpageURL ):
	
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global WasTclVers
        
        webpageURL = webpageURL.strip()
        
        slash = webpageURL.find("http://")
        if (slash < 0):
                log(ERROR_, "missing 'http://' from webpageURL="+webpageURL )
                return
        #endIf
        webpageURL = webpageURL[7:]

        slash = webpageURL.find("/")
        if (slash < 0):
                log(ERROR_, "missing server'/'path' from webpageURL="+webpageURL )
                return
        #endIf
        port = ""
        server = ""
        path = ""
        #if ( not x = regexp("^(http://)?([^:/]+)(:([0-9]+))?(/.*)", webpageURL, sre.IGNORECASE) #?PROBLEM? (jacl 194) SUBS REGXXX_NUMBER_ARGS "protocol" "server" "y" "port" "path"  ):
        #        log(ERROR_, "readWebPage: invalid webpageURL="+webpageURL )
        #        return
        #endIf
        server = server.strip( )
        if (port == ""):
                port = 80
        #endIf
        path = webpageURL[(slash):]
        server = webpageURL[0:(slash-0)]
        colon = server.find(":")
        if (colon > 0):
                port = server[(colon+1):]
                server = server[0:(colon-0)]
        #endIf

        log(INFO_, "readWebPage: server="+`server`+" port="+`port`+" contextPath="+`path` )
        try:
                _excp_ = 0
                import httplib
                conn = httplib.HTTPConnection(server,int(port))  
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                log(DEBUG_, "readWebPage Connection Exception="+`_value_`+`_value_` )
                _excp_ = 1
        #endTry
        e = _excp_
        if (e != 0):
                log(ERROR_, "readWebPage: set socket error="+`e`+" for server="+`server`+" port="+`port` )
                if (WasTclVers <= 0):
                        log(WARNING_, "testApplication: TclJava.jar and/or Jacl.jar too old for web/socket reads ?" )
                        log(WARNING_, "testApplication: #### (copy TCLJAVA.jar plus JACL.jar from WAS60-lib into WAS51-lib) ####" )
                        WasTclVers = 5
                #endIf
                return
        else:
                WasTclVers = 6
        #endElse

        try:
                _excp_ = 0
                conn.request("GET",path)
                #print >>sock, "GET "+path+" HTTP/1.1"
                #print >>sock, "Host: "+server
                #print >>sock, "Accept: */*"
                #print >>sock, "User-Agent: JACL readWebPage"
                #print >>sock, ""
                #log(DEBUG_, "readWebPage: completed sending request." )
                #sock.flush()
                #log(DEBUG_, "readWebPage: completed flush socket " )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                log(WARNING_, "readWebPage Connection Exception="+`_value_`+`_value_` )
                _excp_ = 1
        #endTry
        e = _excp_
        if (e != 0):
                log(ERROR_, "readWebPage: puts socket write error="+`e`+"  (for server="+`server`+" port="+`port`+")" )
                return
        #endIf
        
        log(INFO_, "readWebPage: request sent. waiting for response ..." )

        lines = []
        lineCount = 0
        more = true
        #done = eof(sock)  #?PROBLEM? (jacl 247) CMD_EOF: replace with try:...except: EOFError
        #if (done):
        #        more = false
        #endIf
        blankLines = 0
        response = conn.getresponse()
        # log(INFO_,"WebPageResponse status="+`response.status`+" reason="+`response.reason` )
        
        resp = response.read(4096)
        #log(DEBUG_,"WebPageResponse resp="+`resp` )
        
        respLines = wsadminToList(resp)
        indx = 0
	line = ""
        while (more):
            try:
                lineCount = lineCount+1
                if (lineCount > 100):
                        more = false
                #endIf
                #done = eof(sock)  #?PROBLEM? (jacl 255) CMD_EOF: replace with try:...except: EOFError
                #if (done):
                #        more = false
                #else:
                if(indx>=len(respLines)):
                        more = false
                else:
                        #line = sock.readline().strip()
                        line = respLines[indx]
                        indx += 1
                        line = line.strip( )
                        log(VERBOSE_, "  "+line )
                        if (( line == "Connection: Close")  or
                          ( line == "HTTP/1.1 505 HTTP Version Not Supported") ):
                                log(ERROR_, "readWebPage: Connection Closed, error="+line+"  (for server="+`server`+" port="+`port`+")" )
                                EOF = true
                                more = false
                        #endIf
                        lines.append(line)
                        line = line.upper()
                        if (line == "</BODY>"):
                                more = false
                        #endIf
                #endElse

                if(line==""): blankLines = blankLines +1
                else: blankLines = 0
                if(blankLines>20):  more = false
            except EOFError:
                more = false
            #endTry
        #endWhile
        try:
                _excp_ = 0
                x = conn.close()
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        return lines
#endDef

def StopApplicationOnNodesAndServers ( appName, uniqueNodeServerPairs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(INFO_, "StopApplicationOnNodesAndServers: appName="+appName+" NodeServerPairs="+`uniqueNodeServerPairs`+"..." )
        if (len(uniqueNodeServerPairs) == 0):
                fail("StopApplicationOnNodesAndServers : No nodes/servers/clusters specified" )
        #endIf
        for nodeServer in uniqueNodeServerPairs:
                nodeName = nodeServer[0]
                serverName = nodeServer[1]
                StopApplication(appName, nodeName, serverName )
        #endFor
        log(INFO_, "StopApplicationOnNodesAndServers DONE." )
#endDef

def StartApplicationOnNodesAndServers ( appName, uniqueNodeServerPairs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(INFO_, "StartApplicationOnNodesAndServers: appName='"+appName+"' NodeServerPairs="+`uniqueNodeServerPairs`+"..." )
        if (len(uniqueNodeServerPairs) == 0):
                fail("StartApplicationOnNodesAndServers : No nodes/servers/clusters specified" )
        #endIf
        for nodeServer in uniqueNodeServerPairs:
                nodeName = nodeServer[0]
                serverName = nodeServer[1]
                StartApplication(appName, nodeName, serverName )
        #endFor
        log(INFO_, "StartApplicationOnNodesAndServers DONE." )
#endDef

def checkApplicationRunning ( nodeName, serverName, appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(VERBOSE_, "checkApplicationRunning: "+nodeName+" "+serverName+" "+appName )
        appID = ""
        try:
                _excp_ = 0
                appID = AdminControl.completeObjectName("type=Application,node="+nodeName+",Server="+serverName+",name="+appName+",*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        if (temp > 0):
                log(WARNING_, "checkApplicationRunning: Exception trying to getID for "+appName+" "+nodeName+" "+serverName )
        #endIf
        length = len(appID)

        retries = 0
        while (( retries < 20 )  and ( length == 0 ) ):
                retries = retries+1
                log(INFO_, "checkApplicationRunning: not yet started, "+appName+" "+nodeName+" "+serverName+", retries="+`retries`+" ..." )
                try:
                        _excp_ = 0
                        sleepDelay(10 )
                        appID = AdminControl.completeObjectName("type=Application,node="+nodeName+",Server="+serverName+",name="+appName+",*" )
                        if (len(appID) == 0):
                                appExists = checkIfAppExists(appName )
                                if (appExists):
                                        pass
                                else:
                                        log(ERROR_, "checkApplicationRunning: "+appName+" is NOT INSTALLED." )
                                        return
                                #endElse
                        #endIf
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp > 0):
                        log(WARNING_, "checkApplicationRunning: Exception trying to getID for "+appName+" "+nodeName+" "+serverName )
                #endIf
                length = len(appID)
                log(DEBUG_, "checkApplicationRunning: temp="+`temp`+" appID="+appID+" ..." )
        #endWhile
        if (retries > 0):
                log(INFO_, "checkApplicationRunning: "+nodeName+" "+serverName+" "+appName+" had slow start, status retries="+`retries` )
        #endIf

        if (length > 0):
                log(INFO_, "checkApplicationRunning: "+appName+" is STARTED." )
        else:
                log(ERROR_, "checkApplicationRunning: "+appName+" "+nodeName+" "+serverName+" DID NOT START." )
        #endElse
#endDef

