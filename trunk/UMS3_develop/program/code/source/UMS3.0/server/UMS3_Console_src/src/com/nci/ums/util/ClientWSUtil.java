

package com.nci.ums.util;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

// Referenced classes of package com.nci.ums.util:
//            ServletTemp

public class ClientWSUtil {

	private static Map clientWSArr;

	public ClientWSUtil() {
	}

	private static synchronized void init() {
		if (clientWSArr == null)
			try {
				clientWSArr = new ConcurrentHashMap();
				Properties p = new Properties();
				InputStream is = (new ServletTemp())
						.getInputStreamFromFile("/resources/clientws.props");
				p.load(is);
				is.close();
				String serviceid;
				Object clientWS;
				for (Iterator serviceid_it = p.keySet().iterator(); serviceid_it
						.hasNext(); clientWSArr.put(serviceid, clientWS)) {
					serviceid = (String) serviceid_it.next();
					String className = p.getProperty(serviceid);
					clientWS = Class.forName(className).newInstance();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}

	public static Map getClientWSMap() {
		return clientWSArr;
	}

	static {
		init();
	}
}
