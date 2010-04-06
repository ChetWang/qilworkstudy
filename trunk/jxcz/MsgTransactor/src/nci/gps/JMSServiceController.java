package nci.gps;

import nci.gps.util.Utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JMSServiceController {
	
	private String activeMQPath = "";
	
	public JMSServiceController(){
		Document doc =Utilities.getConfigXMLDocument("localJMSInstall.xml");
		Element installer = (Element)doc.getElementsByTagName("installer").item(0);
		activeMQPath = installer.getAttribute("path");
	}
	
	public void startJMSServer(){
		
	}
	
	public void stopJMSServer(){
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JMSServiceController c = new JMSServiceController();
		String arg = null;
		if(args!=null){
			arg = args[0];
		}
		if(arg.equalsIgnoreCase("start")){
			c.startJMSServer();
		}else if(arg.equalsIgnoreCase("stop")){
			c.stopJMSServer();
		}
	}

}
