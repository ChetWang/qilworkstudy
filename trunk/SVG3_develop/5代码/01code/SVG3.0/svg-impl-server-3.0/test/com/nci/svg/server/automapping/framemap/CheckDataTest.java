package com.nci.svg.server.automapping.framemap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

import junit.framework.TestCase;

public class CheckDataTest extends TestCase {

	public CheckDataTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCheck() {
		AutoMapResultBean result;
		FrameNode data = getData();
		// 检查数据是否合法
		CheckData checkData = new CheckData(data);
		result = checkData.check();
		if (!result.isFlag()) {
			System.out.println(result.getMsg());
			return;
		}
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
				FrameNode sub2 = new FrameNode(name2, "Node" + name2, "第二层节点",
						"");
				for (int k = 0; k < 3; k++) {
					String name3 = name2 + "_" + k;
					FrameNode sub3 = new FrameNode(name3, "Node" + name3,
							"第三层节点", "");
					sub2.appendChildNode(sub3);
				}
				sub1.appendChildNode(sub2);
			}
			root.appendChildNode(sub1);
		}
		return root;
	}

}
