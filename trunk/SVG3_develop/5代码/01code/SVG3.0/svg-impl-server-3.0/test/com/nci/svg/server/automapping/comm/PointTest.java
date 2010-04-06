package com.nci.svg.server.automapping.comm;

import junit.framework.TestCase;

public class PointTest extends TestCase {

	public PointTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPoint() {
		Point point = new Point();
		assertEquals(0.0, point.getX(), 0.0001);
		assertEquals(0.0, point.getY(), 0.0001);
	}

	public void testPointDoubleDouble() {
		Point point= new Point(-1.0, 10000);
		assertEquals(-1, point.getX(), 0.00001);
		assertEquals(10000, point.getY(), 0.00001);
	}

	public void testPointPoint() {
		Point point1 = new Point();
		assertEquals(0.0, point1.getX(), 0.0001);
		assertEquals(0.0, point1.getY(), 0.0001);
		
		point1.setX(1345.678);
		point1.setY(987.654);
		assertEquals(1345.678, point1.getX(), 0.0001);
		assertEquals(987.654, point1.getY(), 0.0001);
		
		Point point2 = new Point(point1);
		assertEquals(1345.678, point2.getX(), 0.0001);
		assertEquals(987.654, point2.getY(), 0.0001);
		
		point1.setX(0.0);
		assertEquals(0.0, point1.getX(), 0.0001);
		assertEquals(987.654, point1.getY(), 0.0001);
		
		point2.setY(0.0);
		assertEquals(1345.678, point2.getX(), 0.0001);
		assertEquals(0.0, point2.getY(), 0.0001);
	}
}
