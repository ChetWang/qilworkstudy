package com.nci.svg.server.mappingdata;

import java.util.List;
import java.util.Map;

public interface MappingData {
	/*
	 * @function :获取业务模型定义信息查询
	 * @parm filter:过滤条件 键为本系统的字段 如SD_OBJID
	 * @parm filter:过滤条件 键为本系统的字段 如SD_OBJID
	 * @retrun 在业务系统的sql
	 */
	public List getMappingData(String modelId,Map filter,List relation, String[] order);
	
	public String getMappingDataSQL(String modelId,Map filter,String [] order);
	/*
	 * @function :获取代码值通过编码
	 * @parm modelName:本系统的数据模型
	 * @parm field:过滤条件 键为本系统的字段
	 * @param code 编码
	 * @param String 代码值
	 */
	public String getValueByCode(String modelId,String field,String code);
}
