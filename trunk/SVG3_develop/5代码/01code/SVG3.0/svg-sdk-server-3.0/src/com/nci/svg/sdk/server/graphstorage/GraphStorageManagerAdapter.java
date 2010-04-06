
package com.nci.svg.sdk.server.graphstorage;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端图库管理组件抽象类
 *
 */
public abstract class GraphStorageManagerAdapter extends ServerModuleAdapter {
	public static final String graphUnitType = NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT;
	public static final String templateType = NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE;

	public GraphStorageManagerAdapter(HashMap parameters) {
		super(parameters);
	}

	/**
	 * 存图(非图元图形)
	 * 
	 * @param strBussID:业务系统编号
	 * @param logs:图日志数据
	 * @param obj:文件属性
	 * @return:存图结果,成功返回0,失败返回<0
	 */
	public abstract ResultBean saveGraph(String strBussID, String logs,
			GraphFileBean obj);

	/**
	 * 取图(非图元)
	 * 
	 * @param strBussID:业务系统编号
	 * @param strGraphType:图类型
	 * @param strFilter:图文件格式
	 * @param obj:文件属性
	 * @return:图内容
	 */
	public abstract ResultBean loadGraph(String strBussID, GraphFileBean obj);

	/**
	 * 存图元
	 * 
	 * @param businessID:String:业务系统编号
	 * @param strSymbolType:图元类型
	 * @param content:图元内容
	 * @param obj:图元文件属性
	 * @return:保存结果
	 */
	public abstract ResultBean saveSymbol(String businessID,
			String strSymbolType, String content, NCIEquipSymbolBean obj);

	/**
	 * 取图元
	 * 
	 * @param strSymbolType:图元类型
	 * @param obj:图元文件属性
	 * @return:图元内容
	 */
	public abstract ResultBean loadSymbol(String name);

	/**
	 * 根据图元类型获取图元清单
	 * 
	 * @param strSymbolType：图元大类，图元/模板
	 * @param releaseFlag:发布标记
	 * @param person：修改人
	 * @return：图元清单
	 */
	public abstract ArrayList getSymbolList(String strSymbolType,
			String releaseFlag, String person);

	/**
	 * 获取图元表和模板表中操作用户信息
	 * 
	 * @return 操作用户信息列表
	 */
	public abstract ArrayList getSymbolOperators();

	/**
	 * 根据图元名称获取该图元的对象
	 * 
	 * @param symbolName:图元名称
	 * @return:指定名称的图元对象
	 */
	public abstract NCIEquipSymbolBean getSymbolIDFromName(String symbolName);

	/**
	 * 获取指定业务系统支持的图形文件类型
	 * 
	 * @param bussID:String:业务系统编号
	 * @return:指定系统支持的图形文件类型
	 */
	public abstract ResultBean getSupportFileType(String bussID);

	/**
	 * 获取指定业务系统文件列表
	 * 
	 * @param businessID:String:系统编号
	 * @param graphBusinessType:String:业务类型
	 * @return 返回指定系统下指定业务类型的图文件列表，如果业务类型为空则返回该系统下所有文件清单
	 */
	public abstract ResultBean getFilesInformation(String businessID,
			GraphFileBean fileBean);

	/**
	 * 根据传入的业务系统编号和数据库连接获取对应的图库标识
	 * 
	 * @param strBussID：业务系统编号
	 * @return：图库标识，不存在则返回null
	 */
	public abstract String getRelateString(String strBussID);

	/**
	 * 获取数据库中的图元表和模型表中有指定名称的图元或模板
	 * 
	 * @param symbolName
	 * @return
	 */
	public abstract String getSameSymbol(String symbolName);
	
	/**
	 * 重命名symbol，修改缓存、数据库
	 * 
	 * @param oldName:String:所有者
	 * @param newName:String:原名
	 * @param symbolType:String:新名称
	 * @return
	 */
	public abstract String renameSymbol(String oldName, String newName,String symbolType,String operator);

	public String getModuleType() {
		return ModuleDefines.GRAPH_STORAGE_MANAGER;
	}

}
