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

/**
 * 工作项创建规则
 * 
 * @author denvelope
 * 
 */
public class WofoWorkitemRuleBean extends WofoBaseBean {

	private String itemRuleId;
	private String processObjType; // 流程定义对象类型（流程定义|活动定义）
	private String processObjId; // 流程定义对象ID
	private String bizNumberRule;
	private String bizTitleRule;
	private String bizContentRule;
	private String bizDescriptionRule;
	private String bizStateRule;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	public WofoWorkitemRuleBean() {
	}

	public WofoWorkitemRuleBean(String itemRuleId) {
		this.itemRuleId = itemRuleId;
	}

	public WofoWorkitemRuleBean(Map map) {
		this.itemRuleId = (String) map.get("item_rule_id");
		this.populate(map);
	}

	public String getItemRuleId() {
		return itemRuleId;
	}

	public void setItemRuleId(String itemRuleId) {
		this.itemRuleId = itemRuleId;
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

	public String getBizNumberRule() {
		return bizNumberRule;
	}

	public void setBizNumberRule(String bizNumberRule) {
		this.bizNumberRule = bizNumberRule;
	}

	public String getBizTitleRule() {
		return bizTitleRule;
	}

	public void setBizTitleRule(String bizTitleRule) {
		this.bizTitleRule = bizTitleRule;
	}

	public String getBizContentRule() {
		return bizContentRule;
	}

	public void setBizContentRule(String bizContentRule) {
		this.bizContentRule = bizContentRule;
	}

	public String getBizDescriptionRule() {
		return bizDescriptionRule;
	}

	public void setBizDescriptionRule(String bizDescriptionRule) {
		this.bizDescriptionRule = bizDescriptionRule;
	}

	public String getBizStateRule() {
		return bizStateRule;
	}

	public void setBizStateRule(String bizStateRule) {
		this.bizStateRule = bizStateRule;
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
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
		if (list.contains("biz_number_rule")) {
			this.bizNumberRule = (String) map.get("biz_number_rule");
		}
		if (list.contains("biz_title_rule")) {
			this.bizTitleRule = (String) map.get("biz_title_rule");
		}
		if (list.contains("biz_content_rule")) {
			this.bizContentRule = (String) map.get("biz_content_rule");
		}
		if (list.contains("biz_description_rule")) {
			this.bizDescriptionRule = (String) map.get("biz_description_rule");
		}
		if (list.contains("biz_state_rule")) {
			this.bizStateRule = (String) map.get("biz_state_rule");
		}
		if (list.contains("creator_unit_id")) {
			this.creatorUnitId = (String) map.get("creator_unit_id");
		}
		if (list.contains("creator_id")) {
			this.creatorId = (String) map.get("creator_id");
		}
		if (list.contains("create_date")) {
			this.createDate = (Date) map.get("create_date");
		}
		if (list.contains("update_date")) {
			this.updateDate = (Date) map.get("update_date");
		}
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("item_rule_id", itemRuleId);
		map.put("process_obj_type", processObjType);
		map.put("process_obj_id", processObjId);
		map.put("biz_number_rule", bizNumberRule);
		map.put("biz_title_rule", bizTitleRule);
		map.put("biz_content_rule", bizContentRule);
		map.put("biz_description_rule", bizDescriptionRule);
		map.put("biz_state_rule", bizStateRule);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String getID() {
		return this.getItemRuleId();
	}

	public void setID(String id) {
		this.setItemRuleId(id);
	}

}
