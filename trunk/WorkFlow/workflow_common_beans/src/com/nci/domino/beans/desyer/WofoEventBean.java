package com.nci.domino.beans.desyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoBaseBean;

/**
 * 事件
 * 
 * @author denvelope 2010-3-29
 *
 */
public class WofoEventBean extends WofoBaseBean {

	protected String eventId;
	protected String eventName; // 事件名称
	protected String listenerType; // 监听类型
	protected String performMode; // 执行方式
	protected String description; // 备注
	protected String creatorUnitId;
	protected String creatorId;
	protected Date createDate;
	protected Date updateDate;
	
	public static final String PERFORM_MODE_SYN = "SYN"; // 同步
	public static final String PERFORM_MODE_ASYN = "ASYN"; // 异步

	public WofoEventBean() {
	}

    public WofoEventBean(Map map) {
        this.eventId = (String) map.get("event_id");
        this.populate(map);
    }
	
	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getListenerType() {
        return listenerType;
    }

    public void setListenerType(String listenerType) {
        this.listenerType = listenerType;
    }

    public String getPerformMode() {
        return performMode;
    }

    public void setPerformMode(String performMode) {
        this.performMode = performMode;
    }

    public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatorUnitId() {
		return this.creatorUnitId;
	}

	public void setCreatorUnitId(String creatorUnitId) {
		this.creatorUnitId = creatorUnitId;
	}

	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
    public Map toMap() {
        Map map = new HashMap();
        map.put("event_id", eventId);
        map.put("event_name", eventName);
        map.put("listener_type", listenerType);
        map.put("perform_mode", performMode);
        map.put("description", description);
        map.put("creator_unit_id", creatorUnitId);
        map.put("creator_id", creatorId);
        map.put("create_date", createDate);
        map.put("update_date", updateDate);
        return map;
    }
    
    public void populate(Map map) {
        List list = new ArrayList();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            Entry entry = (Entry) iterator.next();
            list.add(entry.getKey());
        }
        if (list.contains("event_name")) {
            this.eventName = (String) map.get("event_name");
        }
        if (list.contains("listener_type")) {
            this.listenerType = (String) map.get("listener_type");
        }
        if (list.contains("perform_mode")) {
            this.performMode = (String) map.get("perform_mode");
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
            this.createDate = (Date) map.get("create_date");
        }
        if (list.contains("update_date")) {
            this.updateDate = (Date) map.get("update_date");
        }
    }

    public String getID() {
        return this.eventId;
    }

    public void setID(String id) {
        this.eventId = id;
    }
    
    public String toString(){
    	return eventName;
    }
}
