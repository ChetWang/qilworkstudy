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
 * ���⣺GetNestDistrictList.java
 * </p>
 * <p>
 * ������ ̨��ͼ��ȡ̨����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHANGSF
 * @ʱ��: 2009-03-25
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
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ҵ��ģ�ͣ�δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ҵ��ģ�ͣ�δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "ҵ��ģ�͹����࣬��ȡ����" + actionName
				+ "���������");
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
	 * @function ��ȡ̨���б�
	 * @param businessID:String:ҵ��ϵͳ���
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
		//����ģ��IDSVG_DISTRICT ����������null����������"SD_COMPANY", "SD_YYSBH"�õ�ӳ�����ݵ�SQL
		sql = md.getMappingDataSQL(modelID, null, order);
		if (sql.startsWith("ERROR!")) {
			log.log(this, LoggerAdapter.ERROR, "���ݿ�����쳣���޷���ȡ̨��ͼ�б�");
			return returnErrMsg("��ȡ̨��ͼ�б����ݿ�����쳣��" + sql);
		}
		log.log(this, LoggerAdapter.INFO, "��ȡ̨���б���ϢSQL��" + sql);
		System.out.println("--------------"+sql+"-----------");
		conn = controller.getDBManager().getConnection("psms");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�psms����,�޷���ȡ̨��ͼ�б�");
			return returnErrMsg("��ȡ̨��ͼ�б��޷������Ч���ݿ�psms���ӣ�");
		}
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			//����һ��document
			doc = xu.createDoc();
			if (doc == null) {
				log.log(this, LoggerAdapter.ERROR, "XML��docment���޷�������");
				return returnErrMsg("��ȡ̨��ͼ�б���Ϣ��XML��docment���޷�������");
			}
			Element root = doc.createElement("Companys");
			String company, departments, areaId, areaName;

			Element area, Areas, Departments, Company;
			while (rs.next()) {
				//̨���ڵ�����
				company = rs.getString("SD_COMPANY");
				departments = rs.getString("SD_YYSBH");
				areaId = rs.getString("SD_OBJID");
				areaName = rs.getString("SD_NAME");

				if (isExist(root, areaId) != null)
					continue;

				area = doc.createElement("area");
				area.setAttribute("name", areaName);
				area.setAttribute("id", areaId);

				//����isExist����ȥ�ж�doc���Ƿ���Departments�ڵ㣬��û���½�����������������ӽڵ�
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
			log.log(this, LoggerAdapter.DEBUG, "��ȡ̨��ͼ�б���xml��Ϣ��"
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
 * @function:�ж�node�ڵ��Ƿ���idΪid�Ľڵ�
 * @return Element ���ھͷ���idΪid�Ľڵ� ��Ȼnull
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
