package com.nci.domino.beans.desyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.utils.BeanParamUtils;

public class WofoConditionBean extends WofoBaseBean {

	public static final String TYPE_EXPRESSION = "expression"; // 表达式
	public static final String TYPE_PROCEDURE = "procedure"; // 存储过程
	public static final String TYPE_METHOD = "method"; // Java方法
	public static final String TYPE_CODE = "code"; // Java脚本
	public static final String TYPE_OTHER = "other"; // 其它

	public static final String TYPE_EXPRESSION_TITLE = "表达式";
	public static final String TYPE_PROCEDURE_TITLE = "存储过程";
	public static final String TYPE_METHOD_TITLE = "Java方法";
	public static final String TYPE_CODE_TITLE = "Java脚本";
	public static final String TYPE_OTHER_TITLE = "其它";

	private String conditionId;
	private String conditionCode;
	private String conditionName;
	private String packageId;
	private String processMasterId;
	private String processId;
	private String conditionType;
	private String expression;
	private String description;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	private List members = new ArrayList();

	public WofoConditionBean() {
	}

	public WofoConditionBean(String conditionId) {
		this.conditionId = conditionId;
	}

	public WofoConditionBean(Map map) {
		this.conditionId = (String) map.get("condition_id");
		this.populate(map);
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getProcessMasterId() {
		return processMasterId;
	}

	public void setProcessMasterId(String processMasterId) {
		this.processMasterId = processMasterId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		if (list.contains("condition_code")) {
			this.conditionCode = (String) map.get("condition_code");
		}
		if (list.contains("condition_name")) {
			this.conditionName = (String) map.get("condition_name");
		}
		if (list.contains("package_id")) {
			this.packageId = (String) map.get("package_id");
		}
		if (list.contains("process_master_id")) {
			this.processMasterId = (String) map.get("process_master_id");
		}
		if (list.contains("process_id")) {
			this.processId = (String) map.get("process_id");
		}
		if (list.contains("condition_type")) {
			this.conditionType = (String) map.get("condition_type");
		}
		if (list.contains("expression")) {
			this.expression = (String) map.get("expression");
		}
		if (list.contains("description")) {
			this.description = (String) map.get("description");
		}
		if (list.contains("creator_unit_id")) {
			this.creatorUnitId = (String) map.get("creator_unit_id");
		}
		if (list.contains("creator_id")) {
			this.creatorId = (String) map.get("creator_id");
		}
		if (list.contains("create_date")) {
			this.createDate = BeanParamUtils.getDateValue((Date) map
					.get("create_date"));
		}
		if (list.contains("update_date")) {
			this.updateDate = BeanParamUtils.getDateValue((Date) map
					.get("update_date"));
		}
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("condition_id", conditionId);
		map.put("condition_code", conditionCode);
		map.put("condition_name", conditionName);
		map.put("package_id", packageId);
		map.put("process_master_id", processMasterId);
		map.put("process_id", processId);
		map.put("condition_type", conditionType);
		map.put("expression", expression);
		map.put("description", description);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return conditionName;
	}

	public List getMembers() {
		return members;
	}

	public void setMembers(List members) {
		this.members = members;
	}

	public String getID() {
		return conditionId;
	}

	public void setID(String id) {
		this.conditionId = id;
	}

	public void stateChanged(int preiousState, int newState) {
		if (newState == DELETED || newState == NATURAL) {
			if (members != null) {
				for (int i = 0; i < members.size(); i++) {
					((WofoBaseBean) members.get(i)).setState(newState);
				}
			}
		}
	}
}
