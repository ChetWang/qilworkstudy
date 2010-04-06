package nci.gps.util;

import junit.framework.TestCase;

public class ReadConfigTest extends TestCase {

	public final void testReadConfig() {
		ReadConfig rc = ReadConfig.getInstance();
		System.out.println(rc.toString());
	}

}
