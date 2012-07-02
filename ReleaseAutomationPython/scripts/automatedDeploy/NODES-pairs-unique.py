#
#
#

def PreValidateNodesAndServers ( uniqueNodesContainedServers ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        if (len(uniqueNodesContainedServers) == 0):
                fail("PreValidateNodesAndServers: No nodes/servers/clusters specified" )
        #endIf
        
        for nodeContainedServers in uniqueNodesContainedServers:
                nodeName = nodeContainedServers[0]
                PreValidateNode(nodeName )
                containedServers = nodeContainedServers[1]
                for serverName in containedServers:
                        PreValidateServer(nodeName, serverName )
                #endFor
        #endFor
#endDef

def PreValidateNode ( nodeName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        nodeID = AdminConfig.getid("/Node:"+nodeName+"/" )
        if (len(nodeID) == 0):
                msg = "PreValidateNode: failed - invalid nodeName ["+nodeName+"]"
                fail(msg )
        #endIf
        
        ndSync = AdminControl.completeObjectName("type=NodeSync,node="+nodeName+",*" )
        if (( ndSync == "") ):
                fail("PreValidateNode: failed - could not access node ["+nodeName+"]. nodeagent STOPPED" )
        #endIf
        
        nodeAgent = AdminConfig.getid("/Node:"+nodeName+"/Server:nodeagent/" )
        if (( nodeAgent == "") ):
                fail("PreValidateNode: failed for nodeName="+nodeName+" (nodeAgent STOPPED)" )
        else:
                log(VERBOSE_, "PreValidateNode OK for nodeName=["+nodeName+"]")
        #endElse
#endDef

def PreValidateServer ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        serverID = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        if (len(serverID) == 0):
                msg = "PreValidateServer: failed for server="+serverName+" node="+nodeName+" (invalid serverName)"
                fail(msg )
        #endIf
        serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
        if (len(serverID) == 0):
                msg = "PreValidateServer: cannot access node="+nodeName+" server="+serverName+" (server STOPPED)"
                log(WARNING_, msg )
        else:
                log(VERBOSE_, "PreValidateSrvr OK serverName="+serverName+" nodeName="+nodeName )
        #endElse
#endDef

def PreValidateCluster ( clusterName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        clusterID = AdminConfig.getid("/ServerCluster:"+clusterName+"/" )
        if (len(clusterID) == 0):
                msg = "PreValidateServer: failed for clusterName="+clusterName+" (invalid clusterName)"
                fail(msg )
        #endIf
        clusterID = AdminControl.completeObjectName("name="+clusterName+",*" )
        if (len(clusterID) == 0):
                msg = "PreValidateServer: cannot access clusterName="+clusterName+" (cluster STOPPED)"
                log(WARNING_, msg )
        else:
                state = AdminControl.getAttribute(clusterID, "state" )
                log(VERBOSE_, "PreValidateCluster OK clusterName="+clusterName+" state="+state )
        #endElse
#endDef

def getNodeServerPairs ( clusters ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        nodeServerPairs = []
        for cluster in clusters:
                cluster_id = AdminConfig.getid("/ServerCluster:"+cluster+"/" )
                members = AdminConfig.list("ClusterMember", cluster_id )
                members = wsadminToList(members)
                for member in members:
                        node = AdminConfig.showAttribute(member, "nodeName" )
                        server = AdminConfig.showAttribute(member, "memberName" )
                        log(DEBUG_, "getNodeServerPairs: cluster="+cluster+" contains node="+node+" server="+server )
                        nodeServerPair = [node, server]
                        nodeServerPairs.append(nodeServerPair)
                #endFor
        #endFor
        log(DEBUG_, "getNodeServerPairs: returning nodeServerPairs="+`nodeServerPairs` )
        return nodeServerPairs
#endDef

def determineUniqueNodesContainedServers ( nodeServerPairs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        nodesContainedServers = []
        for nodeServer in nodeServerPairs:
                nodeName = nodeServer[0]
                serverName = nodeServer[1]
                nodeIndex = 0
                for uniquenodeContainedServers in nodesContainedServers:
                        uniquenode = uniquenodeContainedServers[0]
                        if (uniquenode == nodeName):
                                containedServers = uniquenodeContainedServers[1]
                                for server in containedServers:
                                        if (server == serverName):
                                                serverName = ""
                                                break
                                        #endIf
                                #endFor
                                if (serverName != ""):
                                        containedServers.append(serverName)
                                        nodeContainedServers = [nodeName, containedServers]
                                        
                                        del nodesContainedServers[nodeIndex]
                                        nodesContainedServers.insert( nodeIndex, nodeContainedServers )
                                        nodesContainedServers = nodesContainedServers
                                        
                                #endIf
                                nodeName = ""
                                break
                        #endIf
                        nodeIndex = nodeIndex+1
                #endFor
                if (nodeName != "" and serverName != ""):
                        nodeContainedServers = [nodeName, [serverName]]
                        nodesContainedServers.append(nodeContainedServers)
                #endIf
                
        #endFor
        
        return nodesContainedServers
#endDef
