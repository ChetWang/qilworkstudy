
def installAndConfigureApps ( action, distDir, wasRoot ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global testRULs
        global testResponses
        global ReleaseProperty
        appAlias=ReleaseProperty.getProperty("ENTERPRISE_APP_NAME")
        
        #listApplications( )

        appNodeServerPairs = appTargets[0]
        appClusters = appTargets[1]
        appTestURLs = appTargets[2]
        appTestResponses = appTargets[3]
        
        appPath = distDir + "/" + earitem

        ################ VALIDATE EAR ##############
        validateEAR( appPath )

        ################ set targets ##############
        nodeName = []
        serverName = []
        if (len(appNodeServerPairs) > 0):
        	appNodeServerPair = appNodeServerPairs[0]
                nodeName = appNodeServerPair[0]
                serverName = appNodeServerPair[1]
        #endIf
        
        clusterName = []
        if (len(appClusters) > 0):
                clusterName = appClusters[0]
        #endIf
        
        ################ deploy ##############
        log(DEBUG_, "Start " + action + " the application " + appAlias)
        
        installEAR(action, appPath, appAlias, clusterName, nodeName, serverName )
        
        log(MAJOR_, "Application is installed in the workspace.")

        ################ CONFIG TARGETS ##############
        log(DEBUG_, "mapping the modules...")
        
        setTargets(appAlias, appClusters, appNodeServerPairs, appPath )
        log(MAJOR_, "MapModulesToServers finished.")

        ################ VALIDATE INSTALLED APPLICATION ##############
        log(DEBUG_, "Validate the application "+ appAlias)
        
        validateApplication( appAlias )
        log(MAJOR_, "Application [" + appAlias + "] is validated and ready for distribution.")
        
        #listApplications( )
#endDef

