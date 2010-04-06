package com.nci.svg.server.automapping.framemap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * ���⣺ReadData.java
 * </p>
 * <p>
 * ������ �ṹͼ��ȡ������
 * </p>
 * <p>
 * ������ȡҪ���ɽṹͼ�����ݣ��������ݴ�ŵ�FrameNode������С�
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-11
 * @version 1.0
 */
public class ReadData {
	/**
	 * ���������̲�6��ϵͳ�Ľṹͼ
	 */
	private final String SIX_HAS = "sixhas";

	/**
	 * ������¼���Ǹ�ϵͳ����ṹͼ����
	 */
	private String sysName;
	/**
	 * ���ݿ�����
	 */
	private Connection conn;
	/**
	 * ���ڵ���
	 */
	private String rootId;

	/**
	 * ���캯��
	 * 
	 * @param sysName:String:����ϵͳ��
	 */
	public ReadData(Connection conn, String sysName) {
		this.conn = conn;
		this.sysName = sysName;
		this.rootId = "";
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ��ȡ�ṹͼ����
	 * @param params:Object:����
	 * @return
	 */
	public AutoMapResultBean read(Object params) {
		AutoMapResultBean result = null;
		if (SIX_HAS.equalsIgnoreCase(sysName)) {
			// ��ȡ�̲�6��ϵͳ�ṹͼ����
			result = sixHasRead(params);
		} else {
			result = new AutoMapResultBean();
			result.setFlag(false);
			result.setErrMsg("sysname����δ���壡");
		}

		return result;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� �̲�6��ϵͳ�Ľṹͼ���ݶ�ȡ
	 * @param params
	 * @return
	 */
	private AutoMapResultBean sixHasRead(Object params) {
		AutoMapResultBean result = new AutoMapResultBean();
		FrameNode data = null;

		String[] pa = (String[]) params;
		if ("post".equalsIgnoreCase(pa[0])) {
			// ��ȡ��λ�ṹͼ����
			HashMap hashDatas = getPostDatas(pa[1]);
			data = changeDataStruct(hashDatas);
			// �ڸ�λ�ṹͼ��������������
			data = addOrgToPost(pa[1], data);
		} else if ("org".equalsIgnoreCase(pa[0])) {
			// ��ȡ��֯�ṹͼ����
			this.rootId = pa[1];
			HashMap hashDatas = getOrganiseDatas(pa[1]);
			data = changeDataStruct(hashDatas);
		} else if ("demo".equalsIgnoreCase(pa[0])) {
			// ��ȡģ������
			data = getDemoDJata();
		} else {
			// type��������
			result.setFlag(false);
			result.setErrMsg("type��������");
		}

		if (data == null) {
			result.setFlag(false);
			result.setErrMsg("��ȡ����Ϊ�գ�");
		} else {
			result.setMsg(data);
		}

		return result;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��hash�ṹ����ת�Ƴɽṹͼ����
	 * @param hashDatas
	 * @return
	 */
	private FrameNode changeDataStruct(HashMap hashDatas) {
		if (hashDatas.size() <= 0)
			return null;
		treatHashChildren(hashDatas);
		Object rootObj = hashDatas.get(rootId);
		if (rootObj instanceof SixHasDataUnit) {
			SixHasDataUnit rootUnit = (SixHasDataUnit) rootObj;
			return dataUnitToFrameNode(rootUnit, hashDatas);
		} else {
			return null;
		}
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��SixHasDataUnit�����л�����Ϣת����FrameNode����
	 * @param unit
	 * @return
	 */
	private FrameNode dataUnitToFrameNode(SixHasDataUnit unit, HashMap hashDatas) {
		FrameNode node = new FrameNode();

		// **********
		// ת��������Ϣ
		// **********
		node.setId(unit.getId());
		node.setName(unit.getName());
		node.setMessage(unit.getName());

		// **********
		// ת���ӽڵ㼯
		// **********
		ArrayList children = unit.getChildren();
		for (int i = 0, size = children.size(); i < size; i++) {
			String childId = (String) children.get(i);
			SixHasDataUnit child = (SixHasDataUnit) hashDatas.get(childId);
			node.appendChildNode(dataUnitToFrameNode(child, hashDatas));
		}

		return node;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ����hash�����ݵ��ӽڵ�����
	 * @param hashDatas:HashMap:�����ݿ��ж�ȡ�Ľṹͼ����
	 */
	private void treatHashChildren(HashMap hashDatas) {
		Iterator it = hashDatas.entrySet().iterator();
		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			// String key = (String) e.getKey();
			SixHasDataUnit unit = (SixHasDataUnit) e.getValue();
			// ��ȡ�ڵ���
			String id = unit.getId();
			// ��ȡ���ڵ���
			String parID = unit.getParentId();
			// ��ȡ���ڵ����
			Object parObj = hashDatas.get(parID);
			if (parObj instanceof SixHasDataUnit) {
				SixHasDataUnit parUnit = (SixHasDataUnit) hashDatas.get(parID);
				parUnit.addChildren(id);
			}
		}
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡ�̲�������֯�ṹͼ����
	 * @param id:ָ����֯���
	 * @return
	 */
	private HashMap getOrganiseDatas(String id) {
		HashMap datas = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT org_id, org_name, org_parent_id").append(
				" FROM standard_org START WITH org_id='").append(id).append(
				"' CONNECT BY PRIOR org_id = org_parent_id");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());

			while (rs.next()) {
				String orgID = rs.getString("org_id");
				String orgName = rs.getString("org_name");
				String orgParentID = rs.getString("org_parent_id");
				SixHasDataUnit unit = new SixHasDataUnit(orgID, orgParentID,
						orgName);
				datas.put(orgID, unit);
			}

			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.getStackTrace();
		}
		return datas;
	}

	/**
	 * 2009-6-30 Add by ZHM
	 * 
	 * @���� �ڸ�λ�ṹͼ�����������
	 * @param id:String:���ű��
	 * @param data:FrameNode:ת���õĸ�λ�ṹͼ����
	 */
	private FrameNode addOrgToPost(String id, FrameNode data) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT org_id, org_name, org_parent_id").append(
				" FROM standard_org WHERE org_id='").append(id).append("'");
		FrameNode head = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());

			while (rs.next()) {
				String orgID = rs.getString("org_id");
				String orgName = rs.getString("org_name");
				// String orgParentID = rs.getString("org_parent_id");
				head = new FrameNode();
				head.setId(orgID);
				head.setName(orgName);
				head.setMessage(orgName);
			}

			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.getStackTrace();
		}

		if (head != null) {
			head.appendChildNode(data);
			data.setParentNode(head);
		}
		return head;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡ�̲����и�λ�ṹͼ����
	 * @param id:ָ�����ű��
	 * @return
	 */
	private HashMap getPostDatas(String id) {
		HashMap datas = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT post_id, post_name, parent_post_id").append(
				" FROM standard_post WHERE org_id='").append(id).append("'");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());

			while (rs.next()) {
				String postID = rs.getString("post_id");
				String postName = rs.getString("post_name");
				String postParentID = rs.getString("parent_post_id");
				SixHasDataUnit unit = new SixHasDataUnit(postID, postParentID,
						postName);
				datas.put(postID, unit);
				if (postParentID.equals("-1")) {
					rootId = postID;
				}
			}

			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.getStackTrace();
		}
		return datas;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡģ������
	 * @return
	 */
	private FrameNode getDemoDJata() {
		FrameNode root = new FrameNode("0", "����", "����", "");
		for (int i = 0; i < 2; i++) {
			String name1 = "0_" + i;
			FrameNode sub1 = new FrameNode(name1, "��λ" + i, "��λ" + i + "�����Ϣ",
					"");
			for (int j = 0; j < 3; j++) {
				// if (i > 0)
				// continue;
				String name2 = name1 + "_" + j;
				FrameNode sub2 = new FrameNode(name2, "����λ" + i + "_" + j,
						"����λ" + i + "_" + j + "�����Ϣ", "");
//				for (int k = 0; k < 1; k++) {
//					String name3 = name2 + "_" + k;
//					FrameNode sub3 = new FrameNode(name3, "�ڵ�" + name3, "������ڵ�"
//							+ name3, "");
//					for (int ii = 0; ii < 1; ii++) {
//						String name4 = name3 + "_" + ii;
//						FrameNode sub4 = new FrameNode(name4, "�ڵ�" + name4,
//								"���Ĳ�ڵ�" + name4, "");
//						for (int ij = 0; ij < 1; ij++) {
//							String name5 = name4 + "_" + ij;
//							FrameNode sub5 = new FrameNode(name5, "�ڵ�" + name5,
//									"�����ڵ�" + name5, "");
//							sub4.appendChildNode(sub5);
//						}
//						sub3.appendChildNode(sub4);
//					}
//					sub2.appendChildNode(sub3);
//				}
				sub1.appendChildNode(sub2);
			}
			root.appendChildNode(sub1);
		}
		return root;
	}
}
