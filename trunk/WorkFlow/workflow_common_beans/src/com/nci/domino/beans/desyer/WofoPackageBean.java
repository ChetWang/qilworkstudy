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
 * 流程包
 * 
 * @author Qil.Wong
 * 
 */
public class WofoPackageBean extends WofoBaseBean {

	private static final long serialVersionUID = 9121112831150050066L;

	public static final String PACKAGE_STATUS_ON = "ON"; // 启用
	public static final String PACKAGE_STATUS_OFF = "OFF"; // 停用
	public static final String PACKAGE_STATUS_DELETE = "DEL"; // 删除

	private String packageId;
	private String packageName;
	private String parentPackageId;
	private String packageXpath;
	private int displayOrder;
	private String description;
	private String effectUnitId;
	private String effectDepartmentId;
	private String packageStatus;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;

	private List conditions = new ArrayList(); // 拥有的条件（现已移至流程定义下）
	private List activities = new ArrayList(); // 拥有的活动（现已转变设计，暂时无用）

	public WofoPackageBean() {
	}

	/**
	 * 创建根节点 如果用该方法创建子节点，那么在子节点对象创建后，需要setPackageXpath()
	 * 
	 * @param packageId
	 */
	public WofoPackageBean(String packageId) {
		this.packageId = packageId;
		this.packageXpath = packageId.toString();
	}

	/**
	 * 创建子节点
	 * 
	 * @param packageId
	 * @param parent
	 */
	public WofoPackageBean(String packageId, WofoPackageBean parent) {
		this(packageId);
		this.parentPackageId = parent.getPackageId();
		this.packageXpath = parent.getPackageXpath() + "/"
				+ packageId.toString();
	}

	public WofoPackageBean(Map map) {
		this.packageId = (String) map.get("package_id");
		this.populate(map);
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(String parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public String getPackageXpath() {
		return packageXpath;
	}

	public void setPackageXpath(String packageXpath) {
		this.packageXpath = packageXpath;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPackageStatus() {
		return packageStatus;
	}

	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
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

	public void populate(Map map) {
		List list = new ArrayList();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			list.add(entry.getKey());
		}
		if (list.contains("package_name")) {
			this.packageName = (String) map.get("package_name");
		}
		if (list.contains("parent_package_id")) {
			this.parentPackageId = (String) map.get("parent_package_id");
		}
		if (list.contains("package_xpath")) {
			this.packageXpath = (String) map.get("package_xpath");
		}
		if (list.contains("display_order")) {
			this.displayOrder = BeanParamUtils.getIntValue((BigDecimal) map.get("display_order"));
		}
		if (list.contains("description")) {
			this.description = (String) map.get("description");
		}
		if (list.contains("effect_unit_id")) {
			this.effectUnitId = (String) map.get("effect_unit_id");
		}
		if (list.contains("effect_department_id")) {
			this.effectDepartmentId = (String) map.get("effect_department_id");
		}
		if (list.contains("package_status")) {
			this.packageStatus = (String) map.get("package_status");
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
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("package_id", packageId);
		map.put("package_name", packageName);
		map.put("parent_package_id", parentPackageId);
		map.put("package_xpath", packageXpath);
		map.put("display_order", BeanParamUtils.parseBigDecimal(displayOrder));
		map.put("description", description);
		map.put("effect_unit_id", effectUnitId);
		map.put("effect_department_id", effectDepartmentId);
		map.put("package_status", packageStatus);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		return map;
	}

	public String toString() {
		return packageName;
	}

	public boolean isRootNode() {
		if (this.packageXpath == null) {
			return false;
		} else {
			if (this.packageXpath.indexOf("/") == -1) {
				return true;
			}
		}
		return false;
	}

	public List getConditions() {
		return conditions;
	}

	public void setConditions(List conditions) {
		this.conditions = conditions;
	}

	public List getActivities() {
		return activities;
	}

	public void setActivities(List activities) {
		this.activities = activities;
	}

	public String getID() {
		return packageId;
	}

	public void setID(String id) {
		this.packageId = id;
	}
}
