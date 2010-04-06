package com.nci.svg.server.mappingdata.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.server.mappingdata.MappingData;
/**
 * <p>
 * ���⣺PsmsMappingDataImp.java
 * </p>
 * <p>
 * ������ ����ģ�ͱ�ŵ�PSMS�л�ȡӳ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHANGSF
 * @ʱ��: 2009-03-27
 * @version 1.0
 */
public class PsmsMappingDataImp implements MappingData {
	
	private final String map_propname = "SELECT map_propname,fieldname FROM t_jfw_fieldinfo WHERE modelname=? ";//��ȡӳ���ֶ�

	private final String getTable = "SELECT tablename FROM t_jfw_datamodel WHERE modelname=?";//��ȡʵ��psms�����ݴ��ڵı�

	private final String getTableNameByField = "SELECT a.fieldname,b.tablename,b.namefield,b.valuefield FROM T_JFW_FIELDINFO a,t_jfw_codemodel b "
			+ "WHERE a.modelname=? AND a.fieldname=? AND a.codetype=b.modelname";//�����ֶ�ȥ���Ҵ���ģ�͵ı�

	private final String getValueFromCodeData = "SELECT a.codename FROM t_jfw_code_data a ,t_jfw_datamodel b "
			+ "WHERE b.modelname=? AND b.namefield=? AND a.codevalue=? AND a.modelname=b.codetype "; //���ݴ���ӹ���������л�ȡ����ֵ
	
	private final String mappingModelAndField="SELECT map_propname FROM t_jfw_fieldinfo WHERE modelname=?  AND fieldname=?";

	private String modelID;

	private Map filter;

	private String[] order;

	private ServerModuleControllerAdapter controller;

	private Connection svgCon, busCon;

	private PreparedStatement ps;

	private Statement st;

	private ResultSet rs;

	/*
	 * @function ���캯��
	 * @param controller�����������
	 * @param filter ��������
	 * @param order ��������
	 */
	public PsmsMappingDataImp(ServerModuleControllerAdapter controller) {
		this.controller = controller;
	}

	//����ģ�ͱ�Ż�ȡPSMS��ӳ�����ݵ�SQL
	public String getMappingDataSQL(String modelID,Map filter, String[] order) {
		this.modelID = modelID;
		this.filter = filter;
		this.order = order;
		return getSQL();
	}

	//���ݱ���ȥ������л�ȡ����ֵ
	public String getValueByCode(String modelName, String field, String code) {
		String value = null, tableName = null, nameField = null, valueField = null;
		StringBuffer getValueByCode = new StringBuffer();
		busCon = controller.getDBManager().getConnection("psms");
		svgCon=controller.getDBManager().getConnection("svg");
		
		try {
			ps=svgCon.prepareStatement(mappingModelAndField);
			ps.setString(1, modelName);
			ps.setString(2, field);
			
			rs=ps.executeQuery();
			String temp="";
			while(rs.next()){
				temp=rs.getString("map_propname");
			}
			modelName=temp.substring(0, temp.indexOf("."));
			field=temp.substring(temp.indexOf(".")+1);
			
			st = busCon.createStatement();
			ps = busCon.prepareStatement(getTableNameByField);
			ps.setString(1, modelName);
			ps.setString(2, field);

			rs = ps.executeQuery();

			while (rs.next()) {
				tableName = rs.getString("tablename");
				nameField = rs.getString("namefield");
				valueField = rs.getString("valuefield");
			}
			if(nameField==null)return null;
			if (tableName != null || tableName.trim().length() != 0) {
				getValueByCode.append("SELECT ").append(nameField).append(
						" FROM ").append(tableName).append(" WHERE ").append(
								valueField).append(" ='").append(code).append("'");
				System.out.println(getValueByCode.toString());
				rs = st.executeQuery(getValueByCode.toString());
				while (rs.next()) {
					value = rs.getString(nameField);
				}
			} else {
				ps = busCon.prepareStatement(getValueFromCodeData);
				ps.setString(1, modelName);
				ps.setString(2, field);
				ps.setString(3, code);
				rs = ps.executeQuery();
				while (rs.next()) {
					value = rs.getString("codename");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (ps != null)
					ps.close();
				if (!busCon.isClosed())
					busCon.close();
				if (!svgCon.isClosed())
					svgCon.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return value;
	}


	private String getSQL() {
		// �ж��Ƿ�ɹ���ȡ�����������
		StringBuffer sb = new StringBuffer();
		Map fieldMap=new HashMap();
		String modelname = null, tableName = null;
		if (controller == null) {
			return "ERROR!ҵ��ģ�ͣ�δ�ܻ�ȡ�����������!";
		}
		svgCon = controller.getDBManager().getConnection("svg");
		busCon = controller.getDBManager().getConnection("psms");
		try {
			ps = svgCon.prepareStatement(map_propname);
			ps.setString(1, modelID);
			rs = ps.executeQuery();
			sb.append("SELECT ");
			while (rs.next()) {
				if (sb.length() != 7)
					sb.append(",");
				sb.append(rs.getString("map_propname")).append(" ").append(rs.getString("fieldname"));
				fieldMap.put(rs.getString("fieldname"), rs.getString("map_propname"));
				if (modelname == null)
					modelname = rs.getString("map_propname").substring(0,
							rs.getString("map_propname").lastIndexOf("."));
			}
			tableName = getTable(modelname, busCon);
			if (tableName == null)
				return "ERROR!δ����ҵ��ģ�����ҵ�ӳ��ģ��";
			sb.append(" FROM ").append(tableName).append(" " + modelname)
					.append(getFilterSQL(fieldMap)).append(getOrderSQL());

			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR!" + e.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (!busCon.isClosed())
					busCon.close();
				if (!svgCon.isClosed())
					svgCon.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return "ERROR!" + e.getMessage();
			}
		}
	}

	//����������ת��SQL
	private String getOrderSQL() throws SQLException {
		if (order == null || order.length == 0)
			return "";
		StringBuffer sb = new StringBuffer(" ORDER BY ");
		
		for (int i = 0, length = order.length; i < length; i++) {
			if (sb.length() != 10)
				sb.append(" , ");
			sb.append(order[i]);
		}
		return sb.toString();
	}

	//����������ת��SQL
	private String getFilterSQL(Map fieldMap) throws SQLException {
		if (filter == null)
			return "";
		StringBuffer sb = new StringBuffer();

		Iterator it = filter.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key=(String)it.next();
			sb = new StringBuffer(" WHERE ");
			if (sb.length() != 7)
				sb.append(" AND ");
				sb.append(fieldMap.get(key)).append("='").append(filter.get(key)).append("' ");
		}
		return sb.toString();
	}

	//����psms�еĴ������ģ�ͻ�ȡ�����
	private String getTable(String modelname, Connection busCon)
			throws SQLException {
		String tableName = null;
		ps = busCon.prepareStatement(getTable);
		ps.setString(1, modelname);
		rs = ps.executeQuery();

		while (rs.next()) {
			tableName = rs.getString("tablename");
		}
		rs.close();
		ps.close();
		return tableName;
	}

	public String getModelID() {
		return modelID;
	}

	public void setModelName(String modelID) {
		this.modelID = modelID;
	}

	public List getMappingData(String modelId, Map filter, List relation,
			String[] order) {
		return null;
	}

	

}
