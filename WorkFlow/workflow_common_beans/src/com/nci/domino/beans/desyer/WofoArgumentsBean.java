package com.nci.domino.beans.desyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.utils.BeanParamUtils;

public class WofoArgumentsBean extends WofoBaseBean{
    
    public static final String ARG_TYPE_STRING = "String";
    public static final String ARG_TYPE_NUMBER = "Number";
    public static final String ARG_TYPE_DATE = "Date";
    public static final String ARG_TYPE_BOOLEAN = "Boolean";
    
	private String argId;
	private String processObjType;
	private String processObjId;
	private String processMasterId;
	private String packageId;
	private String argName;
	private String argType;
	private String defaultValue;
	private String followField;
	private String sqlRead;
	private String sqlWrite;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;
	
	private boolean editable = true;
	
	private boolean duplicate = false;

	public WofoArgumentsBean() {
	}

	public WofoArgumentsBean(String argId) {
		this.argId = argId;
	}


	public WofoArgumentsBean(Map map) {
		this.argId = (String) map.get("arg_id");
		this.populate(map);
	}

	public String getArgId() {
		return argId;
	}

	public void setArgId(String argId) {
		this.argId = argId;
	}

	public String getProcessObjType() {
		return processObjType;
	}

	public void setProcessObjType(String processObjType) {
		this.processObjType = processObjType;
	}

	public String getProcessObjId() {
		return processObjId;
	}

	public void setProcessObjId(String processObjId) {
		this.processObjId = processObjId;
	}

	public String getProcessMasterId() {
		return processMasterId;
	}

	public void setProcessMasterId(String processMasterId) {
		this.processMasterId = processMasterId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getArgName() {
		return argName;
	}

	public void setArgName(String argName) {
		this.argName = argName;
	}

	public String getArgType() {
		return argType;
	}

	public void setArgType(String argType) {
		this.argType = argType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getFollowField() {
		return followField;
	}

	public void setFollowField(String followField) {
		this.followField = followField;
	}

	public String getSqlRead() {
		return sqlRead;
	}

	public void setSqlRead(String sqlRead) {
		this.sqlRead = sqlRead;
	}

	public String getSqlWrite() {
		return sqlWrite;
	}

	public void setSqlWrite(String sqlWrite) {
		this.sqlWrite = sqlWrite;
	}

	public String getCreatorUnitId() {
		return creatorUnitId;
	}

	public void setCreatorUnitId(String creatorUnitId) {
		this.creatorUnitId = creatorUnitId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = BeanParamUtils.getDateValue(createDate);
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = BeanParamUtils.getDateValue(updateDate);
	}

	public void populate(Map map) {
		List list = new ArrayList();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			list.add(entry.getKey());
		}
		if (list.contains("process_obj_type")) {
			this.processObjType = (String) map.get("process_obj_type");
		}
		if (list.contains("process_obj_id")) {
			this.processObjId = (String) map.get("process_obj_id");
		}
		if (list.contains("process_master_id")) {
			this.processMasterId = (String) map.get("process_master_id");
		}
		if (list.contains("package_id")) {
			this.packageId = (String) map.get("package_id");
		}
		if (list.contains("arg_name")) {
			this.argName = (String) map.get("arg_name");
		}
		if (list.contains("arg_type")) {
			this.argType = (String) map.get("arg_type");
		}
		if (list.contains("default_value")) {
			this.defaultValue = (String) map.get("default_value");
		}
		if (list.contains("follow_field")) {
			this.followField = (String) map.get("follow_field");
		}
		if (list.contains("sql_read")) {
			this.sqlRead = (String) map.get("sql_read");
		}
		if (list.contains("sql_write")) {
			this.sqlWrite = (String) map.get("sql_write");
		}
		if (list.contains("creator_unit_id")) {
			this.creatorUnitId = (String) map.get("creator_unit_id");
		}
		if (list.contains("creator_id")) {
			this.creatorId = (String) map.get("creator_id");
		}
		if (list.contains("create_date")) {
			this.createDate = BeanParamUtils.getDateValue((Date) map.get("create_date"));
		}
		if (list.contains("update_date")) {
			this.updateDate = BeanParamUtils.getDateValue((Date) map.get("update_date"));
		}
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("arg_id", argId);
		map.put("process_obj_type", processObjType);
		map.put("process_obj_id", processObjId);
		map.put("process_master_id", processMasterId);
		map.put("package_id", packageId);
		map.put("arg_name", argName);
		map.put("arg_type", argType);
		map.put("default_value", defaultValue);
		map.put("follow_field", followField);
		map.put("sql_read", sqlRead);
		map.put("sql_write", sqlWrite);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}
	
	/**
	 * 克隆参数对象
	 * @return
	 */
	public WofoArgumentsBean cloneArguments() {
		WofoArgumentsBean newAg = new WofoArgumentsBean(this.toMap());
		return newAg;
	}

	public String toString() {
		return argName;
	}
	
	public String getID() {
		return argId;
	}

	public void setID(String id) {
		this.argId = id;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
}
