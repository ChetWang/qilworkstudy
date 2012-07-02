stty erase ^H
binDir=`dirname "$0"`
. "$binDir/setupCmdLine.sh"

"$JAVA_HOME/bin/java" -classpath "$AutodeployJar" com.db.automateddeploy.util.CmdClient
