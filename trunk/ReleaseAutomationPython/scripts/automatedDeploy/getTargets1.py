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
# 1.2 (14jan2005) fixed multiple EARs, return testURLs (not list of testURLs)
# 1.1 (17nov2004) initial shipped version
#

def PreValidateTargetsFile ( appName ):
        import os
        if (os.path.isfile( appName ) == 0):
                msg = "PreValidateTargetsFile: ERROR: is not a file, targetFileName="+appName
                fail(msg )
        #endIf
        if (os.path.exists( appName ) == 0):
                msg = "PreValidateTargetsFile: ERROR: does not exist, targetFileName="+appName
                fail(msg )
        #endIf
        if (os.access( appName, os.R_OK ) == 0):
                msg = "PreValidateTargetsFile: ERROR: cannot read targetFileName="+appName
                fail(msg )
        #endIf
#endDef

def readTargetsFile ( appName, depType, distDir ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        PreValidateTargetsFile(appName, depType, distDir )
        ext = ".targets"
        targetFile = distDir+"/"+appName+depType+ext
        log(INFO_, "readTargetsFile: targetFile="+targetFile )

        try:
                _excp_ = 0
                fileId = open( targetFile, "r" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                fileId = `_value_`
                _excp_ = 1
        #endTry
        temp = _excp_
        if (temp == 0):
                more = true
        else:
                more = false
                msg = "ERROR: readTargetsFile: attempting to read targetFile="+targetFile
                fail(msg )
                sys.exit(-1)
        #endElse
        blankLines = 0

        nodeServerPairs = []
        clusters = []
        testURLs = []
        testResponses = []
        while (more):
            try:
                #done_ = eof(fileId)  #?PROBLEM? (jacl 58) CMD_EOF: replace with try:...except: EOFError
                #if (done):
                #        more = false
                #endIf
                line = fileId.readline().strip()
                line = line.strip( )
                log(DEBUG_, "readTargetsFile: line="+line )
                _J2J_bracket_ = line.find("#")
                if (_J2J_bracket_ == 0):
                        log(DEBUG_, "readTargetsFile: COMMENT="+line )
                        line = ""
                #endIf

                equals = line.find("=")
                comma = 0
                if (equals > 0):
                        comma = line.find(",")
                #endIf
                if (equals > 0):
                        key = line[0:equals]
                        key = key.strip( )
                        param1 = ""
                        param2 = ""
                        if (comma > 0):
                                _J2J_bracket_ = (equals+1)
                                param1 = line[_J2J_bracket_:comma]
                                param1 = param1.strip( )
                                param2 = line[(comma+1):]
                                param2 = param2.strip( )
                        else:
                                param1 = line[(equals+1):]
                                param1 = param1.strip( )
                        #endElse

                        if (key == "nodeserver"):
                                if (param1 == ""):
                                        msg = "readTargetsFile: ERROR: (MISSING ','):  "+line
                                        fail( )
                                #endIf
                                log(INFO_, "readTargetsFile: node="+param1+" server="+param2 )
                                nodeServerPair = [param1, param2]
                                nodeServerPairs.append(nodeServerPair)
                        elif (key == "cluster"):
                                log(INFO_, "readTargetsFile: cluster="+param1 )
                                clusters.append(param1)
                        elif (key == "testURL"):
                                log(DEBUG_, "readTargetsFile: testURL="+param1 )
                                testURL = param1
                                testURLs.append(testURL)
                        elif (key == "testResponse"):
                                if (param2 != ""):
                                        cm = ","
                                        param1 = param1+cm+param2
                                #endIf
                                log(DEBUG_, "readTargetsFile: testResponse="+param1 )
                                testResponses.append(param1)
                        else:
                                msg = "readTargetsFile: ERROR: unknown key="+key
                                fail(msg )
                        #endElse

                else:
                        if (line != ""):
                                msg = "ERROR: ReadTargetsFile: (MISSING '='):  "+line
                                fail(msg )
                                sys.exit(-1)
                        #endIf
                #endElse

                if(line==""): blankLines = blankLines +1
                else: blankLines = 0
                if(blankLines>30):  more = false
            except EOFError:
                more = false
            #endTry
        #endWhile
        try:
                _excp_ = 0
                _J2J_brace_ = fileId.close()
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        log(DEBUG_, "readTargetsFile: app nodeServerPairs="+`nodeServerPairs` )
        log(DEBUG_, "readTargetsFile: app clusters="+`clusters` )
        log(DEBUG_, "readTargetsFile: app testURLs="+`testURLs` )
        log(DEBUG_, "readTargetsFile: app testResponses="+`testResponses` )

        targets = [nodeServerPairs, clusters, testURLs, testResponses]
        log(DEBUG_, "readTargetsFile: returning "+`targets` )
        return targets
#endDef

