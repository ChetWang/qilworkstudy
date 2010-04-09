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

public class WofoConditionMemberBean extends WofoBaseBean {

	private String memberId;
	private String memberName;
	private String conditionId;
	private String memberValue;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	public WofoConditionMemberBean() {
	}

	public WofoConditionMemberBean(String memberId) {
		this.memberId = memberId;
	}

	public WofoConditionMemberBean(Map map) {
		this.memberId = (String) map.get("member_id");
		this.populate(map);
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public String getMemberValue() {
		return memberValue;
	}

	public void setMemberValue(String memberValue) {
		this.memberValue = memberValue;
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
		if (list.contains("member_name")) {
			this.memberName = (String) map.get("member_name");
		}
		if (list.contains("condition_id")) {
			this.conditionId = (String) map.get("condition_id");
		}
		if (list.contains("member_value")) {
			this.memberValue = (String) map.get("member_value");
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
		map.put("member_id", memberId);
		map.put("member_name", memberName);
		map.put("condition_id", conditionId);
		map.put("member_value", memberValue);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return memberName;
	}

	public String getID() {
		return memberId;
	}

	public void setID(String id) {
		this.memberId = id;
	}
}
