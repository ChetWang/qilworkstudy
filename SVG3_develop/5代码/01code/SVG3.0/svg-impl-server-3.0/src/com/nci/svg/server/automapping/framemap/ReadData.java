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
 * 标题：ReadData.java
 * </p>
 * <p>
 * 描述： 结构图读取数据类
 * </p>
 * <p>
 * 用来获取要生成结构图的数据，并将数据存放到FrameNode类对象中。
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-11
 * @version 1.0
 */
public class ReadData {
	/**
	 * 用来生成烟草6有系统的结构图
	 */
	private final String SIX_HAS = "sixhas";

	/**
	 * 用来记录是那个系统请求结构图生成
	 */
	private String sysName;
	/**
	 * 数据库连接
	 */
	private Connection conn;
	/**
	 * 根节点编号
	 */
	private String rootId;

	/**
	 * 构造函数
	 * 
	 * @param sysName:String:请求系统名
	 */
	public ReadData(Connection conn, String sysName) {
		this.conn = conn;
		this.sysName = sysName;
		this.rootId = "";
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 获取结构图数据
	 * @param params:Object:参数
	 * @return
	 */
	public AutoMapResultBean read(Object params) {
		AutoMapResultBean result = null;
		if (SIX_HAS.equalsIgnoreCase(sysName)) {
			// 读取烟草6有系统结构图数据
			result = sixHasRead(params);
		} else {
			result = new AutoMapResultBean();
			result.setFlag(false);
			result.setErrMsg("sysname参数未定义！");
		}

		return result;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 烟草6有系统的结构图数据读取
	 * @param params
	 * @return
	 */
	private AutoMapResultBean sixHasRead(Object params) {
		AutoMapResultBean result = new AutoMapResultBean();
		FrameNode data = null;

		String[] pa = (String[]) params;
		if ("post".equalsIgnoreCase(pa[0])) {
			// 获取岗位结构图数据
			HashMap hashDatas = getPostDatas(pa[1]);
			data = changeDataStruct(hashDatas);
			// 在岗位结构图上增加所属部门
			data = addOrgToPost(pa[1], data);
		} else if ("org".equalsIgnoreCase(pa[0])) {
			// 获取组织结构图数据
			this.rootId = pa[1];
			HashMap hashDatas = getOrganiseDatas(pa[1]);
			data = changeDataStruct(hashDatas);
		} else if ("demo".equalsIgnoreCase(pa[0])) {
			// 获取模拟数据
			data = getDemoDJata();
		} else {
			// type参数有误
			result.setFlag(false);
			result.setErrMsg("type参数有误");
		}

		if (data == null) {
			result.setFlag(false);
			result.setErrMsg("获取数据为空！");
		} else {
			result.setMsg(data);
		}

		return result;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 将hash结构数据转移成结构图数据
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
	 * @功能 将SixHasDataUnit对象中基本信息转换成FrameNode对象
	 * @param unit
	 * @return
	 */
	private FrameNode dataUnitToFrameNode(SixHasDataUnit unit, HashMap hashDatas) {
		FrameNode node = new FrameNode();

		// **********
		// 转换基本信息
		// **********
		node.setId(unit.getId());
		node.setName(unit.getName());
		node.setMessage(unit.getName());

		// **********
		// 转换子节点集
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
	 * @功能 处理hash表数据的子节点数据
	 * @param hashDatas:HashMap:从数据库中读取的结构图数据
	 */
	private void treatHashChildren(HashMap hashDatas) {
		Iterator it = hashDatas.entrySet().iterator();
		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			// String key = (String) e.getKey();
			SixHasDataUnit unit = (SixHasDataUnit) e.getValue();
			// 获取节点编号
			String id = unit.getId();
			// 获取父节点编号
			String parID = unit.getParentId();
			// 获取父节点对象
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
	 * @功能 获取烟草六有组织结构图数据
	 * @param id:指定组织编号
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
	 * @功能 在岗位结构图添加所属部门
	 * @param id:String:部门编号
	 * @param data:FrameNode:转换好的岗位结构图数据
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
	 * @功能 获取烟草六有岗位结构图数据
	 * @param id:指定部门编号
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
	 * @功能 获取模拟数据
	 * @return
	 */
	private FrameNode getDemoDJata() {
		FrameNode root = new FrameNode("0", "部门", "部门", "");
		for (int i = 0; i < 2; i++) {
			String name1 = "0_" + i;
			FrameNode sub1 = new FrameNode(name1, "岗位" + i, "岗位" + i + "简介信息",
					"");
			for (int j = 0; j < 3; j++) {
				// if (i > 0)
				// continue;
				String name2 = name1 + "_" + j;
				FrameNode sub2 = new FrameNode(name2, "副岗位" + i + "_" + j,
						"副岗位" + i + "_" + j + "简介信息", "");
//				for (int k = 0; k < 1; k++) {
//					String name3 = name2 + "_" + k;
//					FrameNode sub3 = new FrameNode(name3, "节点" + name3, "第三层节点"
//							+ name3, "");
//					for (int ii = 0; ii < 1; ii++) {
//						String name4 = name3 + "_" + ii;
//						FrameNode sub4 = new FrameNode(name4, "节点" + name4,
//								"第四层节点" + name4, "");
//						for (int ij = 0; ij < 1; ij++) {
//							String name5 = name4 + "_" + ij;
//							FrameNode sub5 = new FrameNode(name5, "节点" + name5,
//									"第五层节点" + name5, "");
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
