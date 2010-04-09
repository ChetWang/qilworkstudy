package com.nci.domino.beans.plugin.note;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoDescBean;
import com.nci.domino.utils.BeanParamUtils;

/**
 * 备注信息对象
 * 
 * @author Qil.Wong
 * 
 */
public class WofoNoteBean extends WofoDescBean {

	/**
	 * 注释bean所在的流程id
	 */
	private String processId;
	/**
	 * 注释的x位置
	 */
	private double posX;
	/**
	 * 注释的y位置
	 */
	private double posY;
	/**
	 * 注释图形的宽度
	 */
	private double width;
	/**
	 * 注释图形的高度
	 */
	private double height;
	/**
	 * 注释内容
	 */
	private String value;
	/**
	 * 被注释的图形id
	 */
	private List notedShapeBeans = new ArrayList();

    protected String creatorUnitId;
    protected String creatorId;
    protected Date createDate;
    protected Date updateDate;
	
	public WofoNoteBean() {

	}

	public WofoNoteBean(String id) {
		this.id = id;
	}

    public WofoNoteBean(Map map) {
        this.id = (String) map.get("note_id");
        this.populate(map);
    }
	
	public String getNoteId() {
		return id;
	}

	public void setNoteId(String noteId) {
		this.id = noteId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public WofoNoteBean cloneNote() {
		WofoNoteBean note = new WofoNoteBean();
		note.height = height;
		note.id = id;
		note.posX = posX;
		note.posY = posY;
		note.processId = processId;
		note.value = value;
		note.width = width;
		return note;
	}
	
    public List getNotedShapeBeans() {
        return notedShapeBeans;
    }

    public void setNotedShapeBeans(List notedShapeBeans) {
        this.notedShapeBeans = notedShapeBeans;
    }

    public void populate(Map map) {
        List list = new ArrayList();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            Entry entry = (Entry) iterator.next();
            list.add(entry.getKey());
        }
        if (list.contains("process_id")) {
            this.processId = (String) map.get("process_id");
        }
        // 关联的对象需要特殊处理
        if (list.contains("pos_x")) {
            this.posX = BeanParamUtils.getDoubleValue((BigDecimal) map.get("pos_x"));
        }
        if (list.contains("pos_y")) {
            this.posY = BeanParamUtils.getDoubleValue((BigDecimal) map.get("pos_y"));
        }
        if (list.contains("area_width")) {
            this.width = BeanParamUtils.getDoubleValue((BigDecimal) map.get("area_width"));
        }
        if (list.contains("area_length")) {
            this.height = BeanParamUtils.getDoubleValue((BigDecimal) map.get("area_length"));
        }
        if (list.contains("text")) {
            this.value = (String) map.get("text");
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
        map.put("note_id", super.getID());
        map.put("process_id", processId);
        // 关联的对象需特殊处理
        map.put("pos_x", BeanParamUtils.parseBigDecimal(posX));
        map.put("pos_y", BeanParamUtils.parseBigDecimal(posY));
        map.put("area_width", BeanParamUtils.parseBigDecimal(width));
        map.put("area_length", BeanParamUtils.parseBigDecimal(height));
        map.put("text", value);
        map.put("creator_unit_id", creatorUnitId);
        map.put("creator_id", creatorId);
        map.put("create_date", createDate);
        map.put("update_date", updateDate);
        return map;
    }
}
