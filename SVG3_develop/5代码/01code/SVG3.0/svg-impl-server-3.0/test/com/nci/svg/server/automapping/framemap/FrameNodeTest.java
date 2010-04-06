package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.Point;

import junit.framework.TestCase;

public class FrameNodeTest extends TestCase {
	private final double DELTA = 0.000001;

	public FrameNodeTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFrameNode() {
		FrameNode node = new FrameNode();
		assertEquals("", node.getId());
		assertEquals("", node.getName());
		assertEquals("", node.getMessage());
		assertEquals(0, node.getCoordinate().getX(), DELTA);
	}

	public void testFrameNodeStringStringStringString() {
		FrameNode node = new FrameNode("id","name","message","url");
		assertEquals("id", node.getId());
		assertEquals("name", node.getName());
		assertEquals("message", node.getMessage());
		assertEquals("url", node.getUrl());
		
		node.setId("id2");
		assertEquals("id2", node.getId());
		node.setId(null);
		assertEquals(null, node.getId());
	}

	public void testAppendChildNode() {
		FrameNode node = new FrameNode("id","name","message","url");
		FrameNode child1 = new FrameNode("child1", "child1_Name", "child1_message","url");
		FrameNode child2 = new FrameNode("child2", "child2_name", "child2_message","url");
		node.appendChildNode(child1);
		node.appendChildNode(child2);
		// 验证子节点数
		ArrayList childNodes =node.getChildNodes();
		assertEquals(2, childNodes.size());
		// 验证获取子节点是否正确
		FrameNode testChild = node.getChildNode("child1");
		assertEquals("child1_Name", testChild.getName());
		testChild = node.getChildNode("child");
		assertEquals(null, testChild);
		
		// 验证获取父节点功能
		testChild = node.getChildNode("child1");
		FrameNode parenNode = testChild.getParentNode();
		assertEquals("id", parenNode.getId());
		
		// 验证删除子节点
		testChild = node.getChildNode("child2");
		assertEquals("child2_name", testChild.getName());
		// 验证删除返回值
		assertEquals(true, node.removeChildNode("child1"));
		testChild = node.getChildNode("child1");
		assertEquals(null, testChild);
		// 验证删除返回值
		assertEquals(false, node.removeChildNode("child1"));
		
		child1 = new FrameNode("child1", "child1_Name", "child1_message","url");
		child1.setCoordinate(100, 100);
		node.appendChildNode(child1);
		childNodes =node.getChildNodes();
		assertEquals(2, childNodes.size());
		
		// **************
		// 验证获取节点坐标
		// **************
		child2.setCoordinate(200, 100);
		testChild = node.getChildNode("child2");
		assertEquals(200, testChild.getCoordinate().getX(), DELTA);
		assertEquals(100, testChild.getCoordinate().getY(), DELTA);
		
		node.setCoordinate(0, 0);
		assertEquals(0, node.getCoordinate().getX(), DELTA);
		assertEquals(0, node.getCoordinate().getY(), DELTA);
		
		Point point = new Point(0,0);
		child1.appendPoints(point);
		child2.appendPoints(point);
		
		point.setX(0);
		point.setY(50);
		child1.appendPoints(point);
		child2.appendPoints(point);
		
		point.setX(100);
		point.setY(100);
		child1.appendPoints(point);
		
		point.setX(150);
		point.setY(100);
		child2.appendPoints(point);
		
		testChild = node.getChildNode("child1");
		ArrayList points = testChild.getPoints();
		point = (Point) points.get(0);
		assertEquals(0, point.getX(),DELTA);
		assertEquals(0, point.getY(), DELTA);
		point = (Point) points.get(2);
		assertEquals(100, point.getX(), DELTA);
		
		testChild = node.getChildNode("child2");
		points = testChild.getPoints();
		point = (Point) points.get(0);
		assertEquals(0, point.getX(),DELTA);
		assertEquals(0, point.getY(), DELTA);
		point = (Point) points.get(2);
		assertEquals(150, point.getX(), DELTA);
		
		// ***********
		// 验证坐标移动
		// ***********
		node.move(-50, 23);
		assertEquals(-50, node.getCoordinate().getX(), DELTA);
		assertEquals(23, node.getCoordinate().getY(), DELTA);
		testChild = node.getChildNode("child1");
		assertEquals(50, testChild.getCoordinate().getX(), DELTA);
		assertEquals(123, testChild.getCoordinate().getY(), DELTA);
		points = testChild.getPoints();
		point = (Point) points.get(0);
		assertEquals(-50, point.getX(), DELTA);
		assertEquals(23, point.getY(), DELTA);
		point = (Point) points.get(2);
		assertEquals(50, point.getX(), DELTA);
		assertEquals(123, point.getY(), DELTA);
		
		testChild = node.getChildNode("child2");
		assertEquals(150, testChild.getCoordinate().getX(), DELTA);
		assertEquals(123, testChild.getCoordinate().getY(), DELTA);
		points = testChild.getPoints();
		point = (Point) points.get(0);
		assertEquals(-50, point.getX(), DELTA);
		assertEquals(23, point.getY(), DELTA);
		point = (Point) points.get(2);
		assertEquals(100, point.getX(), DELTA);
		assertEquals(123, point.getY(), DELTA);
	}
	
	public void testSetRight(){
		FrameNode node = new FrameNode("id","name","message","url");
		double d = 0.0;
		node.setRight(d);
		assertEquals(0.0, node.getRight(), DELTA);
		d = 100.0;
		assertEquals(0.0, node.getRight(), DELTA);
	}
}
