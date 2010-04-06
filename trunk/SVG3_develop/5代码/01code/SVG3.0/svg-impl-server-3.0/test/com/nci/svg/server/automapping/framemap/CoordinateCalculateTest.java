package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;

import junit.framework.TestCase;

public class CoordinateCalculateTest extends TestCase {
	private final double DELTA = 0.000001;
	
	public CoordinateCalculateTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCalculate() {
		AutoMapResultBean result;
		FrameNode data = getData();
		// 检查数据是否合法
		CheckData checkData = new CheckData(data);
		result = checkData.check();
		if(!result.isFlag()){
			System.out.println(result.getMsg());
			return;
		}
		
		CoordinateCalculate calculate = new CoordinateCalculate(data);
		result = calculate.calculate();
		if(!result.isFlag()){
			return ;
		}
		Point coord = data.getCoordinate();
		assertEquals(1300, coord.getX(), DELTA);
		assertEquals(0, coord.getY(), DELTA);
		assertEquals(2600, data.getRight(), DELTA);
		
		FrameNode node = (FrameNode) data.getChildNodes().get(0);
		assertEquals(81*2, node.getCoordinate().getY(), DELTA);
		assertEquals(1300-9*100, node.getCoordinate().getX(), DELTA);
		
		FrameNode node2 = (FrameNode) node.getChildNodes().get(0);
		assertEquals(81*4, node2.getCoordinate().getY(), DELTA);
		assertEquals(1300-12*100, node2.getCoordinate().getX(), DELTA);
		
		FrameNode node3 = (FrameNode) node2.getChildNodes().get(0);
		assertEquals(81*6, node3.getCoordinate().getY(), DELTA);
		assertEquals(0, node3.getCoordinate().getX(), DELTA);
		
		ArrayList line1 = node.getPoints();
		ArrayList line2 = node2.getPoints();
		
		Point points = (Point) line1.get(0);
		assertEquals(1325, points.getX(), DELTA);
		assertEquals(81, points.getY(), DELTA);
		points = (Point) line1.get(1);
		assertEquals(1325, points.getX(), DELTA);
		assertEquals(1.5*81, points.getY(), DELTA);
		points = (Point) line1.get(2);
		assertEquals(1300-9*100+25, points.getX(), DELTA);
		assertEquals(1.5*81, points.getY(), DELTA);
		points = (Point) line1.get(3);
		assertEquals(1300-9*100+25, points.getX(), DELTA);
		assertEquals(2*81, points.getY(), DELTA);
		
		points = (Point) line2.get(0);
		assertEquals(425, points.getX(), DELTA);
		assertEquals(81*3, points.getY(), DELTA);
		points = (Point) line2.get(1);
		assertEquals(425, points.getX(), DELTA);
		assertEquals(81*3.5, points.getY(), DELTA);
		points = (Point) line2.get(2);
		assertEquals(125, points.getX(), DELTA);
		assertEquals(81*3.5, points.getY(), DELTA);
		points = (Point) line2.get(3);
		assertEquals(125, points.getX(), DELTA);
		assertEquals(81*4, points.getY(), DELTA);
	}

	/**
	 * 2009-6-10 Add by ZHM
	 * 
	 * @功能 产生测试数据
	 * @return
	 */
	private FrameNode getData() {
		FrameNode root = new FrameNode("0", "root", "根节点", "");
		for (int i = 0; i < 3; i++) {
			String name1 = "0_" + i;
			FrameNode sub1 = new FrameNode(name1, "Node" + name1, "第一层节点", "");
			for (int j = 0; j < 3; j++) {
				String name2 = name1 + "_" + j;
				FrameNode sub2 = new FrameNode(name2, "Node" + name2, "第二层节点", "");
				for (int k = 0; k < 3; k++) {
					String name3 = name2 + "_" + k;
					FrameNode sub3 = new FrameNode(name3, "Node" + name3, "第三层节点",
							"");
					sub2.appendChildNode(sub3);
				}
				sub1.appendChildNode(sub2);
			}
			root.appendChildNode(sub1);
		}
		return root;
	}
}
