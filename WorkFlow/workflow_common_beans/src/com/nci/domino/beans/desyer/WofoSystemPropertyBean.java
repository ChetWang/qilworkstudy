package com.nci.domino.beans.desyer;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 系统参数
 * 
 * @author denvelope 2010-3-26
 *
 */
public class WofoSystemPropertyBean implements Serializable {

    protected String propertyId;
	protected String propertyCode;
	protected String propertyName;
	protected BigDecimal propertyOrder;

	public WofoSystemPropertyBean() {
	}

	public String getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyCode() {
		return this.propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public BigDecimal getPropertyOrder() {
		return this.propertyOrder;
	}

	public void setPropertyOrder(BigDecimal propertyOrder) {
		this.propertyOrder = propertyOrder;
	}
}
