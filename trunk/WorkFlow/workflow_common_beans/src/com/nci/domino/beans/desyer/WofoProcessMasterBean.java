package com.nci.domino.beans.desyer;

import java.math.BigDecimal;
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

/**
 * 流程资源集合
 * 
 * @author Qil.Wong
 * 
 */
public class WofoProcessMasterBean extends WofoBaseBean {

	private String processMasterId;
	private String processMasterName;
	private String packageId;
	private String effectUnitId;
	private String effectDepartmentId;
	private String description;
	private int displayOrder;
	private String processMasterStatus;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	public WofoProcessMasterBean() {
	}

	public WofoProcessMasterBean(String processMasterId) {
		this.processMasterId = processMasterId;
	}

	public WofoProcessMasterBean(Map map) {
		this.processMasterId = (String) map.get("process_master_id");
		this.populate(map);
	}

	public String getProcessMasterId() {
		return processMasterId;
	}

	public void setProcessMasterId(String processMasterId) {
		this.processMasterId = processMasterId;
	}

	public String getProcessMasterName() {
		return processMasterName;
	}

	public void setProcessMasterName(String processMasterName) {
		this.processMasterName = processMasterName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getEffectUnitId() {
		return effectUnitId;
	}

	public void setEffectUnitId(String effectUnitId) {
		this.effectUnitId = effectUnitId;
	}

	public String getEffectDepartmentId() {
		return effectDepartmentId;
	}

	public void setEffectDepartmentId(String effectDepartmentId) {
		this.effectDepartmentId = effectDepartmentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getProcessMasterStatus() {
		return processMasterStatus;
	}

	public void setProcessMasterStatus(String processMasterStatus) {
		this.processMasterStatus = processMasterStatus;
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
		if (list.contains("process_master_name")) {
			this.processMasterName = (String) map.get("process_master_name");
		}
		if (list.contains("package_id")) {
			this.packageId = (String) map.get("package_id");
		}
		if (list.contains("effect_unit_id")) {
			this.effectUnitId = (String) map.get("effect_unit_id");
		}
		if (list.contains("effect_department_id")) {
			this.effectDepartmentId = (String) map.get("effect_department_id");
		}
		if (list.contains("description")) {
			this.description = (String) map.get("description");
		}
		if (list.contains("display_order")) {
			this.displayOrder = BeanParamUtils.getIntValue((BigDecimal) map.get("display_order"));
		}
		if (list.contains("process_master_status")) {
			this.processMasterStatus = (String) map
					.get("process_master_status");
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
		map.put("process_master_id", processMasterId);
		map.put("process_master_name", processMasterName);
		map.put("package_id", packageId);
		map.put("effect_unit_id", effectUnitId);
		map.put("effect_department_id", effectDepartmentId);
		map.put("description", description);
		map.put("display_order", BeanParamUtils.parseBigDecimal(displayOrder));
		map.put("process_master_status", processMasterStatus);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return processMasterName;
	}

	public String getID() {
		return processMasterId;
	}

	public void setID(String id) {
		this.processMasterId = id;
	}
}
