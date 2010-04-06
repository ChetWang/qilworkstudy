package com.nci.svg.server.innerface.model;

import java.util.HashMap;

import junit.framework.TestCase;

import com.nci.svg.sdk.bean.ResultBean;

public class ModelSearchTest extends TestCase {

	public ModelSearchTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHandleOper() {
		String actionName = "getBusinessModel";
		HashMap requestParams = new HashMap();
		requestParams.put("businessID", "1");
		ModelSearch ms = new ModelSearch(requestParams);
		ResultBean bean = ms.handleOper(actionName, requestParams);
		assertEquals(bean.getReturnFlag(), ResultBean.RETURN_SUCCESS);
	}

}
