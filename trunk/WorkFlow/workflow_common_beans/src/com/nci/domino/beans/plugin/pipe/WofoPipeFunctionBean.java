package com.nci.domino.beans.plugin.pipe;

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

/**
 * 泳道职能对象
 * 
 * @author Qil.Wong
 * 
 */
public class WofoPipeFunctionBean extends WofoPipeBaseBean {

	private String name;
	
	private List roles = new ArrayList();

	public WofoPipeFunctionBean() {
	}

	public WofoPipeFunctionBean(String id) {
		this.id = id;
	}
	
	public WofoPipeFunctionBean(Map map) {
	    this.id = (String) map.get("track_id");
	    this.populate(map);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		showText = name;
	}

	public List getRoles() {
		return roles;
	}

	public void setRoles(List roles) {
		this.roles = roles;
	}

	public void setShowText(String t) {
		super.setShowText(t);
		name = t;
	}

	public void stateChanged(int oldState, int newState) {
		if (newState == DELETED || newState == NATURAL) {
			if (roles != null) {
				for (int i = 0; i < roles.size(); i++) {
					((WofoBaseBean) roles.get(i)).setState(newState);
				}
			}
		}
	}

    public void populate(Map map) {
        List list = new ArrayList();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            Entry entry = (Entry) iterator.next();
            list.add(entry.getKey());
        }
        if (list.contains("track_code")) {
        }
        if (list.contains("track_name")) {
            this.showText = (String) map.get("track_name");
        }
        if (list.contains("process_id")) {
            this.processId = (String) map.get("process_id");
        }
        // 活动和角色需要特殊处理
        if (list.contains("pipe_direction")) {
            this.vertical = Boolean.valueOf((String) map.get("pipe_direction")).booleanValue();
        }
        if (list.contains("pipe_width")) {
            this.width = BeanParamUtils.getDoubleValue((BigDecimal) map.get("pipe_width"));
        }
        if (list.contains("pipe_length")) {
            this.height = BeanParamUtils.getDoubleValue((BigDecimal) map.get("pipe_length"));
        }
        if (list.contains("pipe_index")) {
            this.index = BeanParamUtils.getIntValue((BigDecimal) map.get("pipe_index"));
        }
        if (list.contains("pipe_x")) {
            this.x = BeanParamUtils.getDoubleValue((BigDecimal) map.get("pipe_x"));
        }
        if (list.contains("pipe_y")) {
            this.y = BeanParamUtils.getDoubleValue((BigDecimal) map.get("pipe_y"));
        }
        if (list.contains("former_length")) {
            this.formerLength = BeanParamUtils.getDoubleValue((BigDecimal) map.get("former_length"));
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
        map.put("track_id", super.getID());
        map.put("track_code", null);
        map.put("track_name", super.getShowText());
        map.put("process_id", super.getProcessId());
        // 活动和角色需要另外作特殊处理
        map.put("pipe_direction", String.valueOf(vertical));
        map.put("pipe_width", BeanParamUtils.parseBigDecimal(width));
        map.put("pipe_length", BeanParamUtils.parseBigDecimal(height));
        map.put("pipe_index", BeanParamUtils.parseBigDecimal(index));
        map.put("pipe_x", BeanParamUtils.parseBigDecimal(x));
        map.put("pipe_y", BeanParamUtils.parseBigDecimal(y));
        map.put("former_length", BeanParamUtils.parseBigDecimal(formerLength));
        map.put("creator_unit_id", creatorUnitId);
        map.put("creator_id", creatorId);
        map.put("create_date", createDate);
        map.put("update_date", updateDate);
        return map;
    }
}
