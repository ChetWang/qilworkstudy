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

/**
 * Á÷³ÌÇ¨ÒÆ
 * 
 * @author Qil.Wong
 * 
 */
public class WofoTransitionBean extends WofoBaseBean {

	private String transitionId;
	private String transitionName;
	private String processId;
	private String processCode;
	private String previousActivityId;
	private String nextActivityId;
	private String conditionMemberId;
	private String anchors;
	private String wordsPos;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	public WofoTransitionBean() {
	}

	public WofoTransitionBean(String transitionId) {
		this.transitionId = transitionId;
	}

	public WofoTransitionBean(Map map) {
		this.transitionId = (String) map.get("transition_id");
		this.populate(map);
	}

	public WofoTransitionBean cloenTransition() {
		WofoTransitionBean newTran = new WofoTransitionBean(this.toMap());
		return newTran;
	}

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getPreviousActivityId() {
		return previousActivityId;
	}

	public void setPreviousActivityId(String previousActivityId) {
		this.previousActivityId = previousActivityId;
	}

	public String getNextActivityId() {
		return nextActivityId;
	}

	public void setNextActivityId(String nextActivityId) {
		this.nextActivityId = nextActivityId;
	}

	public String getConditionMemberId() {
		return conditionMemberId;
	}

	public void setConditionMemberId(String conditionMemberId) {
		this.conditionMemberId = conditionMemberId;
	}

	public String getAnchors() {
		return anchors;
	}

	public void setAnchors(String anchors) {
		this.anchors = anchors;
	}

	public String getWordsPos() {
		return wordsPos;
	}

	public void setWordsPos(String wordsPos) {
		this.wordsPos = wordsPos;
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
		if (list.contains("transition_name")) {
			this.transitionName = (String) map.get("transition_name");
		}
		if (list.contains("process_id")) {
			this.processId = (String) map.get("process_id");
		}
		if (list.contains("process_code")) {
			this.processCode = (String) map.get("process_code");
		}
		if (list.contains("previous_activity_id")) {
			this.previousActivityId = (String) map.get("previous_activity_id");
		}
		if (list.contains("next_activity_id")) {
			this.nextActivityId = (String) map.get("next_activity_id");
		}
		if (list.contains("condition_member_id")) {
			this.conditionMemberId = (String) map.get("condition_member_id");
		}
		if (list.contains("anchors")) {
			this.anchors = (String) map.get("anchors");
		}
		if (list.contains("words_pos")) {
			this.wordsPos = (String) map.get("words_pos");
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
		map.put("transition_id", transitionId);
		map.put("transition_name", transitionName);
		map.put("process_id", processId);
		map.put("process_code", processCode);
		map.put("previous_activity_id", previousActivityId);
		map.put("next_activity_id", nextActivityId);
		map.put("condition_member_id", conditionMemberId);
		map.put("anchors", anchors);
		map.put("words_pos", wordsPos);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return transitionName;
	}

	public String getID() {
		return transitionId;
	}

	public void setID(String id) {
		this.transitionId = id;
	}
}
