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
# 2.0 (10feb2006) initial Jython version, removed unneeded modules[0] from getModuleID
# 2.0 (10feb2006) removed getModuleID WebModuleDeployment (works for any module)
# 1.1 (17nov2004) initial shipped version
#

def getModuleID ( appName, moduleName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )

        objID = AdminConfig.getid("/Deployment:"+appName+"/")
        objID = AdminConfig.showAttribute(objID, "deployedObject" )
        modules = AdminConfig.showAttribute(objID, "modules" )
        #modules = AdminConfig.showAttribute(AdminConfig.showAttribute(AdminConfig.getid("/Deployment:"+appName+"/" ), "deployedObject" ), "modules" )
        #log(DEBUG_, "getModuleID: modules[]="+`modules`)
        modules = wsadminToList(modules)
        log(DEBUG_, "getModuleID: modules="+`modules`)
        for module in modules:
                id = AdminConfig.showAttribute(module, "uri" )
                if (id == moduleName):
                        moduleID = id
                        log(DEBUG_, "getModuleID: ="+`module`)
                        return module
                #endIf
        #endFor
        return ""
#endDef

def showAttribute ( objName, objType, attName, appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )

        if ("Application"== objType):
                objID = AdminConfig.getid("/Deployment:"+objName+"/")
                objID = AdminConfig.showAttribute(objID,"deployedObject")
        else:
                objID = getModuleID(appName, objName )
        #endElse

        if (attName == "ALL" or attName == "all" or attName == "*" or attName == ""):
                att = AdminConfig.show(objID )
        else:
                att = AdminConfig.showAttribute(objID, attName )
        #endElse
        log(INFO_, "showAtribute: "+objName+" "+attName+"="+att )
#endDef

def setAttribute ( objName, objType, attName, attValue, appName, showSetResult ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "setAttribute: Type="+objType+"  Name="+objName+"  Attribute="+attName+" Value="+attValue )

        if ("Application"== objType):
                objID = AdminConfig.showAttribute(AdminConfig.getid("/Deployment:"+objName+"/" ), "deployedObject" )
        else:
                objID = getModuleID(appName, objName )
        #endElse
        settings = [[attName, attValue]]
        if(len(objID)>0):
                modified = AdminConfig.modify(objID, settings )
        else:
                modified = ""
                log(ERROR_, "could not get objID for Type="+objType+"  Name="+objName+"  Attribute="+attName)
                return
        #endIf
        if (len(modified) > 0):
                log(INFO_, modified )
        #endIf

        if (showSetResult):
                showAttribute(objName, objType, attName, appName )
        #endIf
#endDef
