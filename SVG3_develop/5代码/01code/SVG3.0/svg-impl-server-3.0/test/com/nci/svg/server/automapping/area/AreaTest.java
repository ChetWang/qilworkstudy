package com.nci.svg.server.automapping.area;

import junit.framework.TestCase;

public class AreaTest extends TestCase {

	public AreaTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testArea() {
		Area area = new Area();
		area.setProperty("code", "0001");
		area.setProperty("name", "≤‚ ‘001");

		assertEquals("0001", area.getProperty("code"));
		assertEquals("≤‚ ‘001", area.getProperty("name"));

		String[] basicPropertyName = { "code", "name", "basevol", "company",
				"serviceStation", "village" };
		for (int i = 0, size = basicPropertyName.length; i < size; i++)
			assertEquals(basicPropertyName[i], area.getPropertNames()[i]);
	}

}
