def deploy ( distDir, wasRoot ):
	
	global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
	
	# waiting for other deployments to fnish:
	
	lock_file_path = ScriptLocation + "/.deploy_Lock"
	checkLockFile( lock_file_path )
        
        # create lock_file
        Successed = 0
        
        try:
        	# lock down the protected method
        	lockfile = open( lock_file_path, "w")
		lockfile.write("deploy() locked: " + TimeStamp() )
		lockfile.flush()
		lockfile.close()
        	
        	log(MAJOR_, "ready to enter protected deploy() method...")
        	
        	# call the real function
        	deploy_protected ( distDir, wasRoot )
        	Successed = 1
        	
        finally:
		os.remove(lock_file_path)
        #endTry
        
        # status check: ensure we know if the function is NOT called.
        # this is not supposed to happen.
        if ( Successed == 0 ):
        	fail("Unable to lockdown the proctected method. Please check the lock file " + lock_file_path)
        # end if
        
# End Def	
	

def checkLockFile( lock_file_path ):
	global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        
        log(DEBUG_, "locking deploy() method...")
                
        lockfile_exist = os.path.isfile(lock_file_path)
        
        locked_counter = 0
        
        while ( lockfile_exist == 1 ):
        	
        	f = os.stat(lock_file_path)
        	previous_locktime = f [ stat.ST_MTIME ]
        	current_time = time.time()
        	
        	# allowed lock time is 1200 sec
        	if (current_time - previous_locktime < 1200):
        		
        		log(INFO_, "Another deploy() started at [" + time.ctime(previous_locktime) + "] is still running. Waiting for 15 seconds...")
        		
        		time.sleep(30)
        		lockfile_exist = os.path.isfile(lock_file_path)
        		
        		locked_counter = locked_counter + 1
        		if (locked_counter >100 ):
        			lockfile_exist = 0
        		#end if        		
        	else:
        		lockfile_exist = 0
        	#end if
        #endif
#endDef


def deploy_protected ( distDir, wasRoot ):
        global ScriptLocation
        global ReleaseProperty
        action = ""
        execfile( ScriptLocation+"/Definitions.py" )
        
        if( checkIfAppExists( ReleaseProperty.getProperty("ENTERPRISE_APP_NAME") ) ):
        	action="update"
        else:
        	action="install"
        #endIf
        
        installOrUpdate( action, distDir, wasRoot )
#endDef

#changed by Joshua
def installOrUpdate ( action, distDir, wasRoot ):
	
	depType=""
	
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global clusters
        global testURLs
        global testResponses
        global appsNodesServers
        global hasWebModule
        global DmgrProfileName
        # global wasRoot
        
        if (action == "install"):
                PreValidateApplicationsAbsent(ReleaseProperty.getProperty("ENTERPRISE_APP_NAME") )
        else:
                PreValidateApplicationsExist(ReleaseProperty.getProperty("ENTERPRISE_APP_NAME") )
        #endElse

        ############### CALCULATE AFFECTED NODES ####################
        log(DEBUG_, "Caculate the affected nodes...")
        calculateAffectedNodes(action, distDir, wasRoot, ReleaseProperty.getProperty("ENTERPRISE_APP_NAME") )
        log(DEBUG_, "Caculation finished.")

        ################## PRE-VALIDATE NODES and SERVERS ####################
        log(DEBUG_, "Prevalidate nodes and servers...")
        PreValidateNodesAndServers(uniqueNodesContainedServers )
        log(DEBUG_, "Prevalidation finished.")

        ############### PREPARE AFFECTED NODES ####################
        if (( action == "update") ):
        	log(DEBUG_, "Save and disable autosync...")
                nodesAutosyncs = saveAndDisableAutosync(action, uniqueNodesContainedServers )
                log(DEBUG_, "installOrUpdate: RESULT: nodesAutosyncs="+`nodesAutosyncs` )
        #endIf
        configSave()

        ############### INSTALL APPLICATION AND CONFIGURE ####################
        lowlight(MAJOR_, "Installing the application ...")
        
        installAndConfigureApps(action, distDir, wasRoot )        
        
        configSave()

        ################## SYNC NODES (DISTRIBUTE APPS) ####################
        log(DEBUG_, "synchronize the nodes and distribue apps...")
        
        for nodeContainedServers in uniqueNodesContainedServers:
                nodeName = nodeContainedServers[0]
                servers = nodeContainedServers[1]
                stopSyncStart(action, nodeName, servers )
        #endFor
        
        log(DEBUG_, "nodes synchronization completed.")
        
        ################## START INSTALLED APPLICATIONS ####################
        #if (( action == "install") ):
        #	lowlight(MAJOR_, "starting the new installed application")
        	
        #        length = len(appsNodesServers)
        #        for item in appsNodesServers:
        #                itm = item[0]
        #                length = len(itm)
        #                if (length == 1):
        #                        appName = item
        #                else:
        #                        StartApplicationOnNodesAndServers(ReleaseProperty.getProperty("ENTERPRISE_APP_NAME"), item )
        #                #endElse
        #        #endFor
        #endIf

        ############### RESTORE AFFECTED NODES ####################
        if (( action == "update") ):
        	
                log(DEBUG_, "installOrUpdate: nodesAutosyncs="+`nodesAutosyncs` )
                restoreAutosync(action, nodesAutosyncs )
                
                log(INFO_, "auto-synchronization restored.")
        #endIf
                
        configSave()        
        
        ############### REGENERATE THE PLUGIN XML FILE ####################
                
        log(MAJOR_, "re-generate plugin for webserver ...")
        	
        pluginGen=AdminControl.completeObjectName("type=PluginCfgGenerator,*")        	
        all_args = wasRoot+ " " + wasRoot+"/profiles/"+DmgrProfileName+"/config" + " " + wsadminCell + " null null plugin-cfg.xml"
        AdminControl.invoke(pluginGen, 'generate', all_args)
        configSave()
        
	log(INFO_, "plugin re-generated.")
	
	highlight(MAJOR_, "Application installed. Starting testApplication..." )
	
        ################## TEST ####################
        
	if ( len(testURLs)>0 ):
        	testApplication(testURLs, testResponses )
        else:	
        	log(MAJOR_, "No URL specified for test. testApplication skipped.")
        # end if
#endDef


#-------------------------------------------
# this methon is never used
#-------------------------------------------
def confirm ( action, distDir, wasRoot ):
	
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global clusters
        global testURLs
        global testResponses
        log(DEBUG_, "confirm: "+action+" "+depType+" "+distDir )
        log(DEBUG_, "confirm: "+wasRoot+" ..." )

        ############### FIND APPLICATIONS ####################
        ears = readDistributionDirectory(distDir )
        log(INFO_, "confirm: Deployment ears="+`ears` )
        appNames = parseApplicationNames(ears )
        log(INFO_, "confirm: Deployment appNames="+`appNames` )


        ################## PRE-VALIDATE APPLICATIONS (exists) ####################
        PreValidateApplicationsExist(appNames )

        ############### CALCULATE AFFECTED NODES ####################
        calculateAffectedNodes(action, depType, distDir, wasRoot, appNames )

        ################## PRE-VALIDATE NODES and SERVERS ####################
        PreValidateNodesAndServers(uniqueNodesContainedServers )

        ################## TEST ####################
        testApplication(testURLs, testResponses )

        log(MAJOR_, "confirm: DONE." )
#endDef

def uninstall ( action, depType, distDir, wasRoot ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global clusters
        global testURLs
        global testResponses
        global appsNodesServers
        log(DEBUG_, "uninstall: "+action+" "+depType+" "+distDir )
        log(DEBUG_, "uninstall: "+wasRoot+" ..." )

        ############### FIND APPLICATIONS ####################
        ears = readDistributionDirectory(distDir )
        log(INFO_, "uninstall: Deployment ears="+`ears` )
        appNames = parseApplicationNames(ears )
        log(INFO_, "uninstall: Deployment appNames="+`appNames` )

        ################## PRE-VALIDATE Application TARGETS+SETTINGS files ####################
        PreValidateApplicationTargetsAndSettings(appNames, depType, distDir )

        ################## PRE-VALIDATE APPLICATIONS (exists) ####################
        PreValidateApplicationsExist(appNames )

        ############### CALCULATE AFFECTED NODES ####################
        calculateAffectedNodes(action, depType, distDir, wasRoot, appNames )

        ################## PRE-VALIDATE NODES and SERVERS ####################
        PreValidateNodesAndServers(uniqueNodesContainedServers )

        log(INFO_, "uninstall: Deployment appNames="+`appNames` )
        #listApplications( )

        ################## STOP AND UNINSTALL APPLICATIONS ####################
        for item in appsNodesServers:
                itm = item[0]
                length = len(itm)
                if (length == 1):
                        appName = item
                else:
                        appExists = checkIfAppExists(appName )
                        if (appExists):
                                StopApplicationOnNodesAndServers(appName, item )
                                uninstallEAR(appName )
                        #endIf
                #endElse
        #endFor

        listApplications( )
        configSave( )
        log(MAJOR_, "uninstall: DONE. (appNames="+`appNames`+") " )
#endDef


