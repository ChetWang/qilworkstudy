package com.nci.svg.server.automapping.framemap;

import junit.framework.TestCase;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

public class CreateFrameMapTest extends TestCase {

	public CreateFrameMapTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateSVG() {
		a();
	}

	private void a() {
		AutoMapResultBean result;
		FrameNode data = getData();

		long begin = System.currentTimeMillis();
//		System.out.println("begin time:" + begin);
		CreateFrameMap createFrameMap = new CreateFrameMap(data, "");

		result = createFrameMap.createSVG();
		long t = System.currentTimeMillis();
//		System.out.println("createSVG:" + (t - begin));
		if (result.isFlag()) {
			// Document doc = (Document) result.getMsg();
			// String filename = "log/frameMap.svg";
			// writeSVGFile(doc, filename);
			String svgContent = (String) result.getMsg();
			System.out.println(svgContent);
		} else {
			return;
		}
		long end = System.currentTimeMillis();
//		System.out.println("use time:" + (end - t));
	}

	public void testSplitString() {
		String str = "���ǲ������ַ���\n���ǵڶ���\nthis is third row";
		String[] sts = str.split("\n");
		for (int i = 0; i < sts.length; i++) {
			System.out.println(sts[i]);
		}
	}

	/**
	 * 2009-6-10 Add by ZHM
	 * 
	 * @���� ������������
	 * @return
	 */
	private FrameNode getData() {
		FrameNode root = new FrameNode("0", "����", "����", "");
		for (int i = 0; i < 2; i++) {
			String name1 = "0_" + i;
			FrameNode sub1 = new FrameNode(name1, "��λ��������" + i, "��λ" + i + "�����Ϣ",
					"");
			for (int j = 0; j < 3; j++) {
				String name2 = name1 + "_" + j;
				FrameNode sub2 = new FrameNode(name2, "����λ" + i + "_" + j,
						"����λ" + i + "_" + j + "�����Ϣ", "");
				// for (int k = 0; k < 3; k++) {
				// String name3 = name2 + "_" + k;
				// FrameNode sub3 = new FrameNode(name3, "�ڵ�" + name3,
				// "������ڵ�" + name3, "");
				// sub2.appendChildNode(sub3);
				// }
				sub1.appendChildNode(sub2);
			}
			root.appendChildNode(sub1);
		}
		return root;
	}
}
