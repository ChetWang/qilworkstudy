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
# 2.0 (10feb2006) initial Jython version
# 1.3 (22apr2005) API:setTargets+setModuleMappings: added 'appFile' parameter,
# 1.3 (22apr2005) major restructure, uses WAS-5.0 compatible "AdminApp taskInfo"
# 1.1 (17nov2004) initial shipped version
#
import re


def setTargets ( appName, clusters, appNodeServerPairs, appFile ):
        setModuleMappings(appName, clusters, appNodeServerPairs, appFile )
#endDef

def setModuleMappings ( appName, clusters, appNodeServerPairs, appFile ):
        
        global wsadminCell
        global ScriptLocation
        global hasWebModule
        execfile(ScriptLocation+"/Definitions.py" )
        cellName = wsadminCell
        targets = ""
        
        for cluster in clusters:
                clusterName = cluster
                t = "+WebSphere:cell="+cellName+",cluster="+clusterName
                targets = targets+t
                #log(DEBUG_, "setModuleMappings: cluster targets="+`targets` )
        #endFor
        
        for nodeServerPair in appNodeServerPairs:
                nodeName = nodeServerPair[0]
                serverName = nodeServerPair[1]
                t = "+WebSphere:cell="+cellName+",node="+nodeName+",server="+serverName
                targets = targets+t
                #log(DEBUG_, "setModuleMappings: server targets="+`targets` )
        #endFor
        
        targets = targets[1:]
        #log(INFO_, "setModuleMappings: targets="+`targets` )

        #### v5.0 does not support "AdminApp view", so use "AdminApp taskInfo" instead.
        #log(INFO_, "setModuleMappings: EarFile Mapping query: AdminApp.taskInfo("+appFile+",\"MapModulesToServers\")" )
        
        try:
                _excp_ = 0
                lines = AdminApp.taskInfo(appFile, "MapModulesToServers" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        
        temp = _excp_
        if (temp > 0):
                log(ERROR_, "setModuleMappings: Exception trying to view MapModulesToServers for "+appName )
                return
        #endIf
        
        #log(DEBUG_, "EarFile default mapping="+lines )
        
        lines = wsadminToList(lines)
        mappings = []
        m1 = ""
        MODULE = "MODULE: "
        URI = "URI: "

        for line in lines:
        	        	
                testMOD = line[0:(len(MODULE) -0)].upper()
                testURI = line[0:(len(URI) -0)].upper()
                                
                if (testMOD == MODULE):
                        m1 = line[len(MODULE):]
                        m1 = m1.strip( )
                        #m1 = "\""+m1+"\""
                        
                elif (testURI == URI):
                        m2 = line[len(URI):]
                        m2 = m2.strip( )
                        
                        if (re.match('.*\.war',m2)):
                        	
                        	log(INFO_, "web module detected in the application: " + m2)
                        	
				# targets = targets + "+WebSphere:cell=espear0Cell,node=ihsnode,server=webserver1"
                        	
                        	servers_all=AdminConfig.getid("/Server:/").split(lineSeparator)
                        	
                        	webservers=[]
                        	
                        	for serv in servers_all:
                        		serv_type=AdminConfig.showAttribute(serv, 'serverType')
                        		                        		
                        		if ( serv_type == 'WEB_SERVER'):
                        			webservers.append(serv)
                        			log(DEBUG_, "webserver: "+ serv)
                        			
                        		#end if
                        	#end for
                        	
                        	
                        	for serv in webservers:
                        		splitted = serv.split('/')
                        		cellName = splitted[1]
                        		nodeName = splitted[3]
                        		serverName = splitted[5].split('|')[0]
                        		targets = targets + "+WebSphere:cell="+cellName+",node="+nodeName+",server="+serverName
                        	#end for
                        	
                        	hasWebModule = true
                        #end if
                        	
                        mapping = [m1, m2, targets]
                        #log(DEBUG_, "setModuleMapping: mapping="+`mapping` )
                        
                        mappings.append(mapping)
                        m1 = ""
                #endIf
        #endFor

        options = ["-MapModulesToServers", mappings]
        log(INFO_, "MapModulesToServers: AdminApp edit "+appName+" "+`options` )
        
        try:
                _excp_ = 0
                response = AdminApp.edit(appName, options )
        except java.lang.Exception, e:
                _type_, _value_, _tbck_ = sys.exc_info()
                log(ERROR_, e )
                _excp_ = 1
        #endTry
        
        temp = _excp_
        if (temp > 0):
                log(ERROR_, "setModuleMappings: Exception trying to AdminApp edit "+appName+" "+`options` )
                return
        #endIf
        
        if (len(response) > 0):
                log(INFO_, "setModuleMappings: MapModulesToServers response="+response )
        #endIf
        log(VERBOSE_, "setModuleMappings: DONE." )
        
#endDef

