#
#
#


def calculateAffectedNodes ( action, distDir, wasRoot, appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global clusters
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global testURLs
        global testResponses
        global appsNodesServers
        appsNodesServers = []
        nodeServerPairs = []
        uniqueNodesContainedServers = []
        nodesAutosyncs = []
        testURLs = []
        testResponses = []
        
        log(DEBUG_, "calculateAffectedNodes appName="+`appName`+" ..." )

        ############### FIND NODES/SERVERS ####################
        if (appName == ""):
                fail("calculateAffectedNodes: No appName in property files!" )
        #endIf

        ################ READ TARGETS ##############
        
        appNodeServerPairs = appTargets[0]
        log(DEBUG_, "appNodeServerPairs="+`appNodeServerPairs`)
        tmpNodeServerPairs = []
        appClusters = appTargets[1]
        appTestURLs = appTargets[2]
        appTestResponses = appTargets[3]
        
        ################## PRE-VALIDATE CLUSTERS ####################
        for cluster in appClusters:
        	PreValidateCluster(cluster )
        #endFor

        ################## APPEND TOTAL NODES/SERVERS/TESTS ############
        for appNodeServerPair in appNodeServerPairs:
                nodeServerPairs.append(appNodeServerPair)
                tmpNodeServerPairs.append(appNodeServerPair)
      	#endFor
      	
        clusterNodeServerPairs = getNodeServerPairs(appClusters )
        
        for clusterNodeServerPair in clusterNodeServerPairs:
                nodeServerPairs.append(clusterNodeServerPair)
                tmpNodeServerPairs.append(clusterNodeServerPair)
        #endFor

        clusters.append(appClusters)

        for url in appTestURLs:
                testURLs.append(url)
        #endFor
        
        for response in appTestResponses:
                testResponses.append(response)
        #endFor

        appsNodesServers.append(appName)
        appsNodesServers.append(tmpNodeServerPairs)
        
        ################## UNIQUE NODES (AND THEIR UNIQUE SERVERS) ############
        if (len(nodeServerPairs) == 0):
                fail("calculateAffectedNodes: No node/server/cluster (Targets) specified" )
        #endIf
        
        uniqueNodesContainedServers = determineUniqueNodesContainedServers(nodeServerPairs )
        
        log(MAJOR_, "calculateAffectedNodes: uniqueNodesContainedServers="+`uniqueNodesContainedServers` )

#endDef
