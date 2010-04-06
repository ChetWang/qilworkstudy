package com.nci.svg.server.innerface;

import java.util.HashMap;

import junit.framework.TestCase;

public class InnerFaceTest extends TestCase {

	public InnerFaceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInit() {
		HashMap parameters = new HashMap();
		InnerFace innerFace = new InnerFace(parameters);
		innerFace.init();
	}

}
