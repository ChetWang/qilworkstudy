package com.nci.svg.server.operinterface;

import java.util.HashMap;

import com.nci.svg.server.innerface.TransformSVGModule;

import junit.framework.TestCase;

public class TransformSVGModule_Test extends TestCase {

	public void testTransformSymbolToJpg() {
		HashMap parameters =new HashMap();
		TransformSVGModule module = new TransformSVGModule(parameters);
		module.transformSymbolToJpg("TestTransform");
	}

}
