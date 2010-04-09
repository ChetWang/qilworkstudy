package com.nci.domino.beans.desyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.utils.BeanParamUtils;

public class WofoMessageBean extends WofoBaseBean {

    private String messageId; // 消息ID
	private String processObjType;
	private String processObjId;

	private String sceneType; // 场景类型
	private String messageTitle; // 消息标题
	private String messageContent; // 消息内容
	private String notifyType; // 通知类型，多个类型用","分隔

	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	private List addressee = new ArrayList(); // 消息接收对象
	
	public WofoMessageBean() {
	}

	public WofoMessageBean(String messageId) {
		this.messageId = messageId;
	}

	public WofoMessageBean(Map map) {
		this.messageId = (String) map.get("message_id");
		this.populate(map);
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	public String getSceneType() {
		return sceneType;
	}

	public void setSceneType(String sceneType) {
		this.sceneType = sceneType;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public List getAddressee() {
        return addressee;
    }

    public void setAddressee(List addressee) {
        this.addressee = addressee;
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
		if (list.contains("scene_type")) {
			this.sceneType = (String) map.get("scene_type");
		}
		if (list.contains("message_title")) {
			this.messageTitle = (String) map.get("message_title");
		}
		if (list.contains("message_content")) {
			this.messageContent = (String) map.get("message_content");
		}
		if (list.contains("notify_type")) {
			this.notifyType = (String) map.get("notify_type");
		}
		// 消息接收对象需要特殊处理
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
		map.put("message_id", messageId);
		map.put("process_obj_type", processObjType);
		map.put("process_obj_id", processObjId);
		map.put("scene_type", sceneType);
		map.put("message_title", messageTitle);
		map.put("message_content", messageContent);
		map.put("notify_type", notifyType);
		// 消息接收对象需要特殊处理
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return super.toString();
	}

    public String getID() {
        return messageId;
    }

    public void setID(String id) {
        this.messageId = id;
    }

}
