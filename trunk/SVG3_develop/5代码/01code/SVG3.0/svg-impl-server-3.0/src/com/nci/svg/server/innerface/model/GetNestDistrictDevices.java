package com.nci.svg.server.innerface.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
 * 标题：GetNestDistrictDevices.java
 * </p>
 * <p>
 * 描述： 获取指定台区下所有台区图所需的设备信息
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHANGSF
 * @时间: 2009-03-27
 * @version 1.0
 */
public class GetNestDistrictDevices extends OperationServiceModuleAdapter {

	private static final long serialVersionUID = -5453136844395783478L;
	
	private final String ModelId_Areas="SVG_DISTRICT"; //台区ModelId
	
	private final String ModelId_DsTransformers="SVG_DSTRANSFORMER";//配电ModelId
	
	private final String ModelId_LowEquiBoxs="SVG_LOWEQUIBOX";//低压柜体ModelId
	
	private final String ModelId_Lines="SVG_BRANCH_LINE";//线路ModelId
	
	private final String ModelId_PoleLoop="SVG_POLELOOP";//回路ModelId
	
	private final String ModelId_Pole="SVG_POLE";//杆塔ModelId
	
	private final String ModelId_SERVICE_LINE="SVG_SERVICE_LINE";//接户线ModelId
	
	private final String ModelId_BOX="SVG_BOX";//表箱ModelId
	
	private Document doc=null;
	
	private Element polesE=null,LinesE=null,root,DsTransformersE,LowEquiBoxsE;
	
	private MappingData md=null;
	
	private Map filter=null;
	
	private List p_ObjId=null;
	
	public GetNestDistrictDevices(HashMap parameters) {
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

		if (actionName.equalsIgnoreCase(ActionNames.GETNESTDISTRICTDEVICES)) {
			String districtID = (String) getRequestParameter(requestParams,
					ActionParams.DISTRICTID);
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getNestDistrictDevices(districtID,businessID);
		}
		return rb;
	}

	private ResultBean getNestDistrictDevices(String districtID,String businessID) {
		ResultBean resultBean=null;
		Connection con = null;
		Statement st;
		String sql;
		if(businessID.equalsIgnoreCase("1")){
			con=controller.getDBManager().getConnection("psms");
		}
		doc=XmlUtil.createDoc();
		md=new PsmsMappingDataImp(controller);
		try {
				st=con.createStatement();
				//台区信息
				filter=new HashMap();
				filter.put("SD_OBJID", districtID);
				sql=md.getMappingDataSQL(ModelId_Areas,filter, null);
				root=addDeviceInfo(st,new String[]{sql,"Area"},null);
		
				//配变信息
				
				sql="SELECT hh FROM v$_epms_district WHERE obj_id='"+districtID+"'";
				DsTransformersE=getBaseDevicesFromDistrict(st,new String[]{sql,"SD_YHH",ModelId_DsTransformers,"DsTransformer"});
				root.appendChild(DsTransformersE);
				
				//低压柜体信息
				sql="SELECT obj_a_id FROM jfw_oway_atdata WHERE obj_b_id='"+districtID+"'";
				LowEquiBoxsE=getBaseDevicesFromDistrict(st,new String[]{sql,"SL_OBJID",ModelId_LowEquiBoxs,"EquiBox"});
				root.appendChild(LowEquiBoxsE);	
					
				//线路信息
				polesE=doc.createElement("Poles");
				LinesE=doc.createElement("Lines");
				sql="SELECT obj_id FROM v$_epms_branch_line WHERE p_objid='";
				String loopSQL="SELECT obj_id FROM v$_epms_loop WHERE p_objid = '";
				String poleSQL="SELECT obj_id FROM v$_epms_service_line WHERE p_objid = '";
				p_ObjId=new ArrayList();
				p_ObjId.add(districtID);

				String deviParams[]={sql,"BL_OBJID",ModelId_Lines,"Line",
						loopSQL,"SP_ID",ModelId_PoleLoop,"Loop","P_OBJID",ModelId_Pole,"Pole",
						poleSQL,"SL_OBJID",
						ModelId_SERVICE_LINE,"ServiceLine","SB_OBJID",ModelId_BOX,"Box"};
				
				LinesE=getLineDevicesFromDistrict(st,deviParams,p_ObjId);
				root.appendChild(LinesE);	
				root.appendChild(polesE);
				
				
				doc.appendChild(root);
				resultBean = new ResultBean(ResultBean.RETURN_SUCCESS, null,
						"String", XmlUtil.nodeToString(doc));
			} catch (SQLException e) {
				e.printStackTrace();
				log.log(this, LoggerAdapter.ERROR, e.getMessage());
				return returnErrMsg(e.getMessage());
			}
		
		return resultBean;
	}
	
	/*
	 * @function 根据获取线路设备信息或者需递归往下挖取的设备信息
	 * @param st 
	 * @param params参数列表长度19 参数顺序为 1、主线获取设备IDSQL，2、对应模型字段，3、模型ID，4，输出文件描述
	 * 5、回路sql，6、对应模型字段，7、模型ID，8，输出文件描述，9、杆塔对应模型字段，10、杆塔对应模型字段，11、模型ID，12、输出文件描述
	 * 13、接户线sql，14、接户线对应模型字段，15、接户线模型ID，16，输出文件描述，17、表箱对应模型字段，18、表箱对应模型字段，19、输出文件描述
	 * @retrun Elemet
	 */
	private Element getLineDevicesFromDistrict(Statement st, String[] params,List lineList) throws SQLException {
		String districtInfoSql=null;
		ResultSet rs=null;
		String yhh=null,temp=null;
		Element parent=null;
		String sql=null;
		ResultSetMetaData rsm = null;
		Element children = null,loops;
		if(params[3].equalsIgnoreCase("Line")){
			parent=LinesE;
		}else{
			parent=doc.createElement(params[3]+"s");
		}
		while(lineList.size()!=0){
			temp=(String) lineList.remove(0);
			sql=params[0]+temp+"'";
			log.log(this, LoggerAdapter.INFO, "获取台区图的设备编号sql：" + sql);
			rs=st.executeQuery(sql);
			while(rs.next()){
				yhh=rs.getString(1);
				lineList.add(yhh);
			}
			filter=new HashMap();
			filter.put(params[1], temp);
			districtInfoSql=md.getMappingDataSQL(params[2],filter, null);
			log.log(this, LoggerAdapter.INFO, "获取台区图配变信息**sql：" + districtInfoSql);
			
			rs=st.executeQuery(districtInfoSql);
			rsm = rs.getMetaData();
			while(rs.next()){
				children=doc.createElement(params[3]);
				for(int i=1,length=rsm.getColumnCount()+1;i<length;i++){
					children.setAttribute(rsm.getColumnName(i).toLowerCase(), rs.getString(rsm.getColumnName(i)));
				}
			}
			if(children!=null&&params.length==18){
				sql=params[4]+temp+"'";
				loops=getBaseDevicesFromDistrict(st,new String[]{sql,params[5],params[6],params[7],params[8],params[9],params[10]});
				sql=params[11]+temp+"'";
				parent=getBaseDevicesFromDistrict(st,new String[]{sql,params[12],params[13],params[14],params[15],params[16],params[17]});
				children.appendChild(loops);
			}
			if(children!=null)
				parent.appendChild(children);
		}
		return parent ;
	}
	/*
	 * @Author:zhangsf
	 * @datetime:2009-04-27
	 * @function:给设备添加设备属性
	 * @return:添加完属性的设备
	 */
      private Element addDeviceInfo(Statement st,String params[],Element tem) throws SQLException{
    	  Element temp=null;
    	  
    	  ResultSet rs = st.executeQuery(params[0]);
    	  ResultSetMetaData rsm=rs.getMetaData();
    	  while(rs.next()){
    		  	temp=doc.createElement(params[1]);
    		  	for(int i=1,length=rsm.getColumnCount()+1;i<length;i++){
					temp.setAttribute(rsm.getColumnName(i).toLowerCase(), rs.getString(rsm.getColumnName(i)));
				}
    		  	if(tem==null)return temp;
    		  	tem.appendChild(temp);
    	  }
    	  return tem;
      }
	
	/*
	 * @function 根据获取单层设备信息和下级设备即
	 * 首先获取objid然后获取设备信息
	 * @param st 
	 * @param params参数列表单层参数长度4，并取下级设备信息的则7；
	 *  参数顺序为 1.获取设备编号sql， 2.设备ID对应模型字段名 key，3模型ID,4,在输出文件描述，5下级设备模型主键，6下级设备模型ID,7,下级设备输出文件描述
	 * @retrun Elemet单层设备Element
	 */
	private Element getBaseDevicesFromDistrict(Statement st,String params[]) throws SQLException{
		String districtInfoSql=null;
		MappingData md=null;
		ResultSet rs=null;
		Element parent=null,service_line=null;
		List info=new ArrayList();
		if(params[3].equalsIgnoreCase("Pole")){
			parent=polesE;
		}else if(params[3].equalsIgnoreCase("ServiceLine")){
			parent=LinesE;
		}else{
			parent=doc.createElement(params[3]+"s");
		}
		log.log(this, LoggerAdapter.INFO, "获取台区图的设备信息的获取单层设备的设备IDsql：" + params[0]);
		rs=st.executeQuery(params[0]);
		while(rs.next()){
			info.add(rs.getString(1)); 
		}
			md=new PsmsMappingDataImp(controller);
		for(int j=0,size=info.size();j<size;j++){
			filter=new HashMap();
			filter.put(params[1], info.get(j));
			districtInfoSql=md.getMappingDataSQL(params[2],filter, null);
			log.log(this, LoggerAdapter.INFO, "获取台区图的设备信息的获取单层设备的设备sql：" + districtInfoSql);
			service_line=addDeviceInfo(st,new String[]{districtInfoSql,params[3]},null);
			if(params.length==7){
				if(params[6].equalsIgnoreCase("Pole")){
					String sql="SELECT obj_a_id FROM jfw_oway_atdata WHERE obj_b_id='"+info.get(j)+"'";
					polesE=getBaseDevicesFromDistrict(st,new String[]{sql,params[4],params[5],params[6]});
				}else if(params[6].equalsIgnoreCase("Box")){
					String sql="SELECT obj_a_id FROM jfw_oway_atdata WHERE obj_b_id='"+info.get(j)+"'";
					service_line.appendChild(getBaseDevicesFromDistrict(st,new String[]{sql,params[4],params[5],params[6]}));
				}
			}
			if(params.length==7&&params[6].equalsIgnoreCase("Box")){
				parent.appendChild(service_line);
			}else{
				parent=addDeviceInfo(st,new String[]{districtInfoSql,params[3]},parent);
			}
		}
		return parent;
	}
}
