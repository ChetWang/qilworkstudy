package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.server.automapping.comm.AutoMapComm;
import com.nci.svg.server.automapping.comm.AutoMapResultBean;

import junit.framework.TestCase;

public class SystemReadDataTest extends TestCase {

	public SystemReadDataTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSystemReadData() {
		String dataString = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\xtt.xml");
		SystemReadData rd = new SystemReadData(dataString);
		ArrayList stationIdList = rd.getStationIdList();
		HashMap stationMap = rd.getStationMap();
		ArrayList lineList = rd.getLineList();

		SystemCheckData cd = new SystemCheckData(stationIdList, stationMap,
				lineList);
		AutoMapResultBean result = cd.checkData();
		if(!result.isFlag()){
			System.out.println(result.getErrMsg());
			return;
		}
		
		SystemCoordinateCalculate scc = new SystemCoordinateCalculate(stationIdList, stationMap);
		result = scc.calculate("default");
		if(!result.isFlag()){
			System.out.println(result.getErrMsg());
			return;
		}
		ArrayList matrix = (ArrayList) result.getMsg();
		
		SystemCreateMap scm = new SystemCreateMap(stationIdList, stationMap,
				lineList, matrix);
		result = scm.createSVG();
		if(!result.isFlag()){
			System.out.println(result.getErrMsg());
			return;
		}else{
			System.out.println(result.getMsg());
		}
	}

}
