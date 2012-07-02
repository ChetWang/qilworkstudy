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
# 1.1 (17nov2004) initial shipped version
#

def readDistributionDirectory ( distDir ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(DEBUG_, "readDistributionDirectory: "+distDir+" ..." )

        import os
        if (os.path.isdir( distDir ) == 0):
                msg = "readDistributionDirectory: ERROR: is not a directory, distDir="+distDir
                fail(msg )
        #endIf
        if (os.path.exists( distDir ) == 0):
                msg = "readDistributionDirectory: ERROR: does not exist, distDir="+distDir
                fail(msg )
        #endIf
        #if (os.access( distDir, os.R_OK ) == 0):
        #        msg = "readDistributionDirectory: ERROR: cannot read distDir="+distDir
        #        fail(msg )
        #endIf

        import glob
        names = glob.glob( distDir+"/*" )  #?PROBLEM? (jacl 35) COMMAND_UNKNOWN?  glob("-nocomplain", distDir+"/*" )
        if (len(names) == 0):
                msg = "ERROR: readDistributionDirectory: no files found in distDir="+distDir
                fail(msg )
        #endIf

        ears = []
        wars = []
        ejbs = []
        for name in names:
                dot = name.rfind(".")
                if (dot > 1):
                        ext = name[dot:]
                        ext = ext.lower()
                else:
                        ext = ""
                #endElse
                if (ext == ".ear"):
                        log(DEBUG_, "readDistributionDirectory: ear="+name )
                        ears.append(name)
                elif (ext == ".war"):
                        log(DEBUG_, "readDistributionDirectory: war="+name )
                        wars.append(name)
                elif (ext == ".jar"):
                        log(DEBUG_, "readDistributionDirectory: jar="+name )
                        ejbs.append(name)
                #endIf
        #endFor
        return ears
#endDef

def parseApplicationNames ( ears ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        appNames = []
        for ear in ears:
                app = ear
                log(DEBUG_, "parseApplicationNames: earPath="+app )
                dot = app.find(".ear")
                if (dot > 0):
                        app = app[0:dot]
                #endIf
                sl = app.rfind("/")
                bs = app.rfind("\\")
                if (bs > sl):
                        sl = bs
                #endIf
                if (sl > 0):
                        app = app[(sl+1):]
                #endIf
                
                appNames.append(app)
        #endFor
        return appNames
#endDef
