package com.nci.svg.server.automapping.area;

import junit.framework.TestCase;

public class AreaPoleTest extends TestCase {

	public AreaPoleTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetCoordinate() {
		AreaPole pole = new AreaPole();
		pole.setCoordinate(0.1, 0.2);
		assertTrue(Math.abs(pole.getCoordinateX() - 0.1) < 0.00001);
		assertTrue(Math.abs(pole.getCoordinateY() - 0.2) < 0.00001);
		
		double[] point = { 0.0, 0.0 };
		pole.setCoordinate(point);
		assertTrue(Math.abs(pole.getCoordinate()[0] - 0.0) < 0.00001);
		assertTrue(Math.abs(pole.getCoordinateX() - 0.0) < 0.00001);
		assertTrue(Math.abs(pole.getCoordinate()[1] - 0.0) < 0.00001);
		assertTrue(Math.abs(pole.getCoordinateY() - 0.0) < 0.00001);
		
		pole.setCoordinate(0.1, 0.2);
		assertTrue(Math.abs(pole.getCoordinateX() - 0.1) < 0.00001);
		assertTrue(Math.abs(pole.getCoordinateY() - 0.2) < 0.00001);
	}
	
	/**
	 * ²âÊÔ¹¹Ôìº¯Êý
	 */
	public void testElectricPoleStruct(){
		AreaPole pole = new AreaPole();
		pole.setCoordinate(0.1, 0.2);
		pole.setProperty("code", "0001");
		pole.setProperty("distance", new Double(30.0));
		
		AreaPole p = new AreaPole(pole);
		assertEquals("0001", (String)p.getProperty("code"));
		assertEquals(30.0, ((Double)p.getProperty("distance")).doubleValue(), 0.0001);
		
		pole.setProperty("distance", new Double(40.0));
		assertEquals(30.0, ((Double)p.getProperty("distance")).doubleValue(), 0.0001);
		assertEquals(40.0, ((Double)pole.getProperty("distance")).doubleValue(), 0.0001);
	}


}
