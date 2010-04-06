package com.nci.svg.server.automapping;

import com.nci.svg.server.automapping.comm.BasicProperty;

import junit.framework.TestCase;

public class BasicPorpertyTest extends TestCase {

	public BasicPorpertyTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetProperty() {
		String[] propertNames = { "code", "name", "basevol", "company" };
		BasicProperty bp = new BasicProperty(propertNames);
		bp.setProperty("code", "0001");
		bp.setProperty("name", "≤‚ ‘0001");
		bp.setProperty("basevol", new Double(1000.0));
		bp.setProperty("company", new Integer(1));

		assertEquals("0001", (String) bp.getProperty("code"));
		assertEquals("≤‚ ‘0001", (String) bp.getProperty("name"));
		assertEquals(1, ((Integer) bp.getProperty("company")).intValue());
		assertEquals(1000.0,
				((Double) bp.getProperty("basevol")).doubleValue(), 0.0001);
		assertEquals(bp.getProperty("test"), null);
	}

	public void testGetPropertNames() {
		String[] propertNames = { "code", "name", "basevol", "company" };
		BasicProperty bp = new BasicProperty(propertNames);
		bp.setProperty("test", "test");
		assertEquals(bp.getProperty("test"), null);
	}

}
