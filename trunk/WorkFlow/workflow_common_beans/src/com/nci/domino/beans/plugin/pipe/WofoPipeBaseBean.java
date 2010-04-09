package com.nci.domino.beans.plugin.pipe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nci.domino.beans.WofoDescBean;

/**
 * 泳道对象基类，所有垂直和水平管道及自定义管道公用属性
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WofoPipeBaseBean extends WofoDescBean {

    protected String processId;
    protected List activitys = new ArrayList(); // 拥有的活动环节
    
	protected String showText;

	protected double width, height, x, y, formerLength;
	protected int index;
	protected boolean vertical;
	
    protected String creatorUnitId;
    protected String creatorId;
    protected Date createDate;
    protected Date updateDate;

	public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public void setShowText(String t) {
		this.showText = t;
	}

	public String getShowText() {
		return showText;
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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	public double getFormerLength() {
		return formerLength;
	}

	public void setFormerLength(double formerLength) {
		this.formerLength = formerLength;
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

    public List getActivitys() {
        return activitys;
    }

    public void setActivitys(List activitys) {
        this.activitys = activitys;
    }
}
