package com.nci.svg.server.innerface.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.mappingdata.MappingData;
import com.nci.svg.server.mappingdata.imp.PsmsMappingDataImp;
import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * 标题：GetNestDistrictList.java
 * </p>
 * <p>
 * 描述： 台区图获取台区类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHANGSF
 * @时间: 2009-03-25
 * @version 1.0
 */
public class GetNestDistrictList extends OperationServiceModuleAdapter {

	private static final long serialVersionUID = 1L;

	private final String modelID="SVG_DISTRICT";

	private Document doc=null;

	private XmlUtil xu=null;

	public GetNestDistrictList(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("业务模型，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("业务模型，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "业务模型管理类，获取到‘" + actionName
				+ "’请求命令！");
		ResultBean rb = null;

		if (actionName.equalsIgnoreCase(ActionNames.GET_NESTDISTRICTLIST)) {
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getNestDistrictList(businessID);
		}

		return rb;
	}

	/**
	 * 2009-3-23 Add by ZHANGSF
	 * @function 获取台区列表
	 * @param businessID:String:业务系统编号
	 * @return String xml
	 */
	private ResultBean getNestDistrictList(String businessID) {
		ResultBean resultBean = null;
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		
		MappingData md = null;
		String sql = null;
		xu = new XmlUtil();
		if (businessID.equalsIgnoreCase("1")) {
			md = new PsmsMappingDataImp(controller);
		}
		String[] order = { "SD_COMPANY", "SD_YYSBH" };
		//根据模型IDSVG_DISTRICT 、过滤条件null和排序条件"SD_COMPANY", "SD_YYSBH"得到映射数据的SQL
		sql = md.getMappingDataSQL(modelID, null, order);
		if (sql.startsWith("ERROR!")) {
			log.log(this, LoggerAdapter.ERROR, "数据库操作异常，无法获取台区图列表！");
			return returnErrMsg("获取台区图列表，数据库操作异常！" + sql);
		}
		log.log(this, LoggerAdapter.INFO, "获取台区列表信息SQL：" + sql);
		System.out.println("--------------"+sql+"-----------");
		conn = controller.getDBManager().getConnection("psms");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库psms连接,无法获取台区图列表！");
			return returnErrMsg("获取台区图列表，无法获得有效数据库psms连接！");
		}
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			//生成一个document
			doc = xu.createDoc();
			if (doc == null) {
				log.log(this, LoggerAdapter.ERROR, "XML的docment的无法建立！");
				return returnErrMsg("获取台区图列表信息，XML的docment的无法建立！");
			}
			Element root = doc.createElement("Companys");
			String company, departments, areaId, areaName;

			Element area, Areas, Departments, Company;
			while (rs.next()) {
				//台区节点属性
				company = rs.getString("SD_COMPANY");
				departments = rs.getString("SD_YYSBH");
				areaId = rs.getString("SD_OBJID");
				areaName = rs.getString("SD_NAME");

				if (isExist(root, areaId) != null)
					continue;

				area = doc.createElement("area");
				area.setAttribute("name", areaName);
				area.setAttribute("id", areaId);

				//根据isExist方法去判断doc中是否有Departments节点，若没有新建，若有在下面添加子节点
				Departments = isExist(root, departments);
				if (Departments != null) {
					Departments.getElementsByTagName("Areas").item(0)
							.appendChild(area);

					continue;
				} else {
					Departments = doc.createElement("Departments");
					Departments.setAttribute("id", departments);
					Departments.setAttribute("name", md.getValueByCode(modelID,
							"SD_YYSBH", departments));
					Areas = doc.createElement("Areas");
					Areas.appendChild(area);
					Departments.appendChild(Areas);
				}

				Company = isExist(root, company);
				if (Company != null) {
					Company.appendChild(Departments);
					continue;
				} else {
					Company = doc.createElement("Company");
					Company.setAttribute("id", company);
					Company.setAttribute("name", md.getValueByCode(modelID,
							"SD_COMPANY", company));
					Company.appendChild(Departments);
				}

				root.appendChild(Company);
			}
			doc.appendChild(root);
			resultBean = new ResultBean(ResultBean.RETURN_SUCCESS, null,
					"String", xu.nodeToString(doc));
			log.log(this, LoggerAdapter.DEBUG, "获取台区图列表返回xml信息："
					+ xu.nodeToString(doc));
		} catch (SQLException e) {
			e.printStackTrace();
			log.log(this, LoggerAdapter.ERROR, e.getMessage());
			return returnErrMsg(e.getMessage());
		}

		return resultBean;
	}
/*
 * @Author:zhangsf
 * @time:2009-04-27
 * @function:判断node节点是否有id为id的节点
 * @return Element 存在就返回id为id的节点 不然null
 */
	private Element isExist(Element node, String id) {
		NodeList nodeList = node.getChildNodes();
		NamedNodeMap attrList;
		Node temp;
		for (int i = 0, length = nodeList.getLength(); i < length; i++) {
			attrList = nodeList.item(i).getAttributes();
			temp = attrList.getNamedItem("id");
			if (temp == null)
				continue;
			if (temp.getNodeValue().equalsIgnoreCase(id))
				return (Element) nodeList.item(i);
			return isExist((Element) nodeList.item(i), id);
		}
		return null;
	}
}
