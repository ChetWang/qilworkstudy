/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-19
 * @功能：业务规范服务类
 *
 */
package com.nci.svg.server.innerface.indunorm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.IndunormBean;
import com.nci.svg.sdk.bean.IndunormDescBean;
import com.nci.svg.sdk.bean.IndunormFieldBean;
import com.nci.svg.sdk.bean.IndunormMetadataBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * @author yx.nci
 *
 */
public class IndunormModule extends OperationServiceModuleAdapter {

	private static final long serialVersionUID = 1239732580798769765L;

	public IndunormModule(HashMap parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.service.OperationServiceModuleAdapter#handleOper(java.lang.String, java.util.Map)
	 */
	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("行业规范，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("行业规范，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "行业规范管理类，获取到‘" + actionName
				+ "’请求命令！");
		String businessID = (String) getRequestParameter(requestParams,
				ActionParams.BUSINESS_ID);
		ResultBean rb = null;
		if (actionName
				.equalsIgnoreCase(ActionNames.GET_BUSINESS_INDUNORM)) {
			//业务系统业务规范清单
			rb = getIndunormMaps(businessID);
		}
		else if(actionName
				.equalsIgnoreCase(ActionNames.GET_BUSINESS_MOELRELAINDUNORM))
		{
			//业务系统模型与规范关联清单
			rb = getModelRelaIndunorm(businessID);
		}
		return rb;
	}
	
	/**
	 * add by yux,2009-1-19
	 * 根据输入的业务系统编号，获取该业务系统所支持的所有业务规范
	 * @param businessID：业务系统编号
	 * @return：结果
	 */
	private ResultBean getIndunormMaps(String businessID)
	{
		ResultBean resultBean = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT a.* ")
		.append("FROM t_svg_indunorm_type a,t_svg_indunorm_graphtyperela b ")
		.append("WHERE a.it_shortname = b.ig_norm_type AND b.ig_businesssys_id = '")
		.append(businessID).append("'");
		
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取规范信息！");
			return returnErrMsg("获取规范信息，无法获得有效数据库连接！");
		}

		HashMap map = new HashMap();
		try {
			Statement st = conn.createStatement();
			log
					.log(this, LoggerAdapter.DEBUG, "获取规范种类信息："
							+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			IndunormBean bean = null;
			String shortName = null;
			while(rs.next())
			{
				bean = new IndunormBean();
				shortName = rs.getString("IT_SHORTNAME");
				bean.getTypeBean().setShortName(shortName);
				bean.getTypeBean().setName(rs.getString("IT_NAME"));
				bean.getTypeBean().setQuote(rs.getString("IT_QUOTE"));
				bean.getTypeBean().setDesc(rs.getString("IT_DESC"));
				map.put(shortName, bean);
			}
			if(rs != null)
				rs.close();
			
			loadIndunormDescs(conn,map,businessID);
			loadIndunormMetadatas(conn,map,businessID);
			loadIndunormFields(conn,map,businessID);
			resultBean = new ResultBean(ResultBean.RETURN_SUCCESS,null,"HashMap",map);
			
		}
		catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范信息，数据库操作失败！");
		}finally {
			controller.getDBManager().close(conn);
		}
		return resultBean;
	}
	
	/**
	 * add by yux,2009-1-19
	 * 加载业务规范的描述信息
	 * @param conn：数据库连接
	 * @param map:数据集
	 * @param businessID：业务系统编号
	 */
	private void loadIndunormDescs(Connection conn,HashMap map,String businessID)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.* FROM t_svg_indunorm_desc c ")
		.append("WHERE c.id_variety IN ")
		.append("( ").append("SELECT DISTINCT a.it_shortname FROM t_svg_indunorm_type a,t_svg_indunorm_graphtyperela b ")
		.append("WHERE a.it_shortname = b.ig_norm_type AND b.ig_businesssys_id = '")
		.append(businessID).append("') ORDER BY c.id_variety");
		try {
			Statement st = conn.createStatement();
			log
					.log(this, LoggerAdapter.DEBUG, "获取规范描述信息："
							+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			IndunormBean bean = null;
			String shortName = null;
			while(rs.next())
			{
				
				shortName = rs.getString("ID_VARIETY");
				bean = (IndunormBean)map.get(shortName);
				if(bean != null)
				{
					IndunormDescBean descBean = new IndunormDescBean();
					descBean.setId(rs.getString("ID_ID"));
					descBean.setShortName(rs.getString("ID_SHORT_NAME"));
					descBean.setName(rs.getString("ID_NAME"));
					descBean.setVariety(shortName);
					descBean.setDesc(rs.getString("ID_DESC"));
					bean.getDescMap().put(descBean.getId(), descBean);
				}
			}
		}
		catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return;
	}
	
	/**
	 * add by yux,2009-1-19
	 * 加载业务规范的数据域信息
	 * @param conn：数据库连接
	 * @param map：数据集
	 * @param businessID：业务系统编号
	 */
	private void loadIndunormMetadatas(Connection conn,HashMap map,String businessID)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.* FROM t_svg_indunorm_metadatas c ")
		.append("WHERE c.im_variety IN ")
		.append("( ").append("SELECT DISTINCT a.it_shortname FROM t_svg_indunorm_type a,t_svg_indunorm_graphtyperela b ")
		.append("WHERE a.it_shortname = b.ig_norm_type AND b.ig_businesssys_id = '")
		.append(businessID).append("') ORDER BY c.im_variety");
		try {
			Statement st = conn.createStatement();
			log
					.log(this, LoggerAdapter.DEBUG, "获取规范数据域信息："
							+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			IndunormBean bean = null;
			String shortName = null;
			while(rs.next())
			{
				
				shortName = rs.getString("IM_VARIETY");
				bean = (IndunormBean)map.get(shortName);
				if(bean != null)
				{
					IndunormMetadataBean metadataBean = new IndunormMetadataBean();
					metadataBean.setId(rs.getString("IM_ID"));
					metadataBean.setShortName(rs.getString("IM_SHORT_NAME"));
					metadataBean.setName(rs.getString("IM_NAME"));
					metadataBean.setVariety(shortName);
					metadataBean.setDesc(rs.getString("IM_DESC"));
					bean.getMetadataMap().put(metadataBean.getId(), metadataBean);
				}
			}
		}
		catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return;
	}
	
	/**
	 * add by yux,2009-1-19
	 * 加载业务规范的数据字段信息
	 * @param conn：数据库连接
	 * @param map：数据集
	 * @param businessID：业务系统编号
	 */
	private void loadIndunormFields(Connection conn,HashMap map,String businessID)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.*,d.im_variety,d.im_short_name FROM t_svg_indunorm_fields c,t_svg_indunorm_metadatas d  ")
		.append("WHERE c.if_variety = d.im_id and d.im_variety IN ")
		.append("( ").append("SELECT DISTINCT a.it_shortname FROM t_svg_indunorm_type a,t_svg_indunorm_graphtyperela b ")
		.append("WHERE a.it_shortname = b.ig_norm_type AND b.ig_businesssys_id = '")
		.append(businessID).append("') ORDER BY d.im_variety");
		try {
			Statement st = conn.createStatement();
			log
					.log(this, LoggerAdapter.DEBUG, "获取规范数据域信息："
							+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			IndunormBean bean = null;
			String shortName = null;
			while(rs.next())
			{
				
				shortName = rs.getString("IM_VARIETY");
				bean = (IndunormBean)map.get(shortName);
//				String key = null;
				if(bean != null)
				{
					IndunormFieldBean FieldBean = new IndunormFieldBean();
					FieldBean.setId(rs.getString("IF_ID"));
					FieldBean.setShortName(rs.getString("IF_SHORT_NAME"));
					FieldBean.setName(rs.getString("IF_NAME"));
					FieldBean.setVariety(rs.getString("IF_VARIETY"));
					FieldBean.setDesc(rs.getString("IF_DESC"));
//					key = FieldBean.getVariety() + ":" + FieldBean.getShortName();
					bean.getFieldMap().put(FieldBean.getId(), FieldBean);
				}
			}
		}
		catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return;
	}
	
	/**
	 * add by yux,2009-1-19
	 * 根据指定的业务系统编号，获取模型与规范关联关系
	 * @param businessID:业务系统编号
	 * @return：
	 */
	private ResultBean getModelRelaIndunorm(String businessID)
	{
		ResultBean resultBean = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.mi_modelid,a.mi_indunormid,a.mi_propertyid,")
		.append("(SELECT c.mp_name FROM t_svg_model_propertys c WHERE a.mi_propertyid = c.mp_id) mp_name,")
		.append("(SELECT c.mp_type FROM t_svg_model_propertys c WHERE a.mi_propertyid = c.mp_id) mp_type,")
		.append("(SELECT c.mp_code FROM t_svg_model_propertys c WHERE a.mi_propertyid = c.mp_id) mp_code,")
		.append("(SELECT h.cc_name FROM t_svg_model_propertys c,t_svg_code_commsets h WHERE a.mi_propertyid = c.mp_id AND h.cc_code = c.mp_type AND h.cc_shortname = 'SVG_MODEL_PROPERTYTYPE') mp_typename,")
        .append("(SELECT e.id_short_name FROM t_svg_indunorm_desc e WHERE a.mi_descid = e.id_id) id_short_name,")
        .append("(SELECT f.im_short_name FROM t_svg_indunorm_metadatas f where a.mi_metadataid = f.im_id) im_short_name,")
        .append("(SELECT g.if_short_name FROM t_svg_indunorm_fields g WHERE a.mi_fieldid = g.if_id) if_short_name FROM t_svg_model_indunormrela a ")
        .append("WHERE mi_bussid = '")
		.append(businessID).append("'");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取规范信息！");
			return returnErrMsg("获取规范信息，无法获得有效数据库连接！");
		}

		HashMap map = new HashMap();
		
		try {
			Statement st = conn.createStatement();
			log
					.log(this, LoggerAdapter.DEBUG, "获取规范种类信息："
							+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelRelaIndunormBean bean = null;
			String modelID = null;
			while(rs.next())
			{
				bean = new ModelRelaIndunormBean();
				modelID = rs.getString("MI_MODELID");
                bean.setModelID(modelID);
                bean.setModelPropertyID(rs.getString("MI_PROPERTYID"));
                bean.setModelPropertyName(rs.getString("MP_NAME"));
                bean.setModelPropertyType(rs.getString("MP_TYPE"));
                bean.setModelPropertyCode(rs.getString("MP_CODE"));
                bean.setModelPropertyTypeName(rs.getString("MP_TYPENAME"));
                bean.setIndunormShortName(rs.getString("MI_INDUNORMID"));
                bean.setDescShortName(rs.getString("ID_SHORT_NAME"));
                bean.setMetadataShortName(rs.getString("IM_SHORT_NAME"));
                bean.setFieldShortName(rs.getString("IF_SHORT_NAME"));
                ArrayList list = (ArrayList)map.get(modelID);
                if(list == null)
                {
                	list = new ArrayList();
                	map.put(modelID, list);
                }
				list.add(bean);
			}
			if(rs != null)
				rs.close();

			resultBean = new ResultBean(ResultBean.RETURN_SUCCESS,null,"HashMap",map);
			
		}
		catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范信息，数据库操作失败！");
		}finally {
			controller.getDBManager().close(conn);
		}
		return resultBean;
	}

}
