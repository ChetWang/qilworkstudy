package com.nci.domino.beans.desyer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.utils.BeanParamUtils;

public class WofoParticipantScopeBean extends WofoBaseBean {

	public static final String PARTICIPANT_TYPE_UNIT = "UNIT"; // 机构
	public static final String PARTICIPANT_TYPE_USER = "USER"; // 人员
	public static final String PARTICIPANT_TYPE_ROLE = "ROLE"; // 角色
	public static final String PARTICIPANT_TYPE_VIRTUAL = "VIRTUAL"; // 虚拟角色

	public static final String ROLE_EFFECT_SCOPE_UNIT = "SAME_UNIT"; // 本单位
	public static final String ROLE_EFFECT_SCOPE_DEPT = "SAME_DEPT"; // 本部门

	public static final String PARTICIPANT_TYPE_UNIT_TITLE = "机构"; // 机构
	public static final String PARTICIPANT_TYPE_USER_TITLE = "人员"; // 人员
	public static final String PARTICIPANT_TYPE_ROLE_TITLE = "角色"; // 角色
	public static final String PARTICIPANT_TYPE_VIRTUAL_TITLE = "虚拟角色"; // 虚拟角色

	public static final String ROLE_EFFECT_SCOPE_UNIT_TITLE = "本单位"; // 本单位
	public static final String ROLE_EFFECT_SCOPE_DEPT_TITLE = "本部门"; // 本部门

	private String participantScopeId;
	private String activityId;
	private String participantType;
	private String participantId;
	private String participantCode;
	private String participantName;
	private int participateOrder;
	private String roleEffectScope;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	public WofoParticipantScopeBean() {
	}

	public WofoParticipantScopeBean(String participantScopeId) {
		this.participantScopeId = participantScopeId;
	}

	public WofoParticipantScopeBean(Map map) {
		this.participantScopeId = (String) map.get("participant_scope_id");
		this.populate(map);
	}

	public String getParticipantScopeId() {
		return participantScopeId;
	}

	public void setParticipantScopeId(String participantScopeId) {
		this.participantScopeId = participantScopeId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getParticipantCode() {
		return participantCode;
	}

	public void setParticipantCode(String participantCode) {
		this.participantCode = participantCode;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public int getParticipateOrder() {
		return participateOrder;
	}

	public void setParticipateOrder(int participateOrder) {
		this.participateOrder = participateOrder;
	}

	public String getRoleEffectScope() {
		return roleEffectScope;
	}

	public void setRoleEffectScope(String roleEffectScope) {
		this.roleEffectScope = roleEffectScope;
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
		if (list.contains("activity_id")) {
			this.activityId = (String) map.get("activity_id");
		}
		if (list.contains("participant_type")) {
			this.participantType = (String) map.get("participant_type");
		}
		if (list.contains("participant_id")) {
			this.participantId = (String) map.get("participant_id");
		}
		if (list.contains("participant_code")) {
			this.participantCode = (String) map.get("participant_code");
		}
		if (list.contains("participant_name")) {
			this.participantName = (String) map.get("participant_name");
		}
		if (list.contains("participate_order")) {
			this.participateOrder = BeanParamUtils.getIntValue((BigDecimal) map
					.get("participate_order"));
		}
		if (list.contains("role_effect_scope")) {
			this.roleEffectScope = (String) map.get("role_effect_scope");
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
		map.put("participant_scope_id", participantScopeId);
		map.put("activity_id", activityId);
		map.put("participant_type", participantType);
		map.put("participant_id", participantId);
		map.put("participant_code", participantCode);
		map.put("participant_name", participantName);
		map.put("participate_order", BeanParamUtils.parseBigDecimal(participateOrder));
		map.put("role_effect_scope", roleEffectScope);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public WofoParticipantScopeBean cloneScopeBean() {
		WofoParticipantScopeBean newBean = new WofoParticipantScopeBean(this.toMap());
		return newBean;
	}

	public String toString() {
		return participantName;
	}

	public String getID() {
		return participantScopeId;
	}

	public void setID(String id) {
		this.participantScopeId = id;
	}
}
