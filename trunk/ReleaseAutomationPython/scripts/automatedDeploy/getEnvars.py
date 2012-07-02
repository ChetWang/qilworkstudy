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
# 1.3 (22apr2005) initial shipped version
#

def getEnvar ( envar ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        result = java.lang.System.getProperty(envar)
        try:
                _excp_ = 0
                import os
                if(result==None or result==""):
                        result = os.environ[envar]
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        if(result==None):
                result = ""
        #end if
        
        return result
#endDef

def convertToJaclPath ( path ):
        slash = path.find("\\")
        while (slash > 0):
                r1 = path[0:(slash-0)]
                r2 = path[(slash+1):]
                path = r1+"/"+r2
                slash = path.find("\\")
        #endWhile
        return path
#endDef
