package com.nci.svg.sdk.ui.equip;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;


public abstract class MetaDataManager {
	protected EditorAdapter editor;
	
	public MetaDataManager(EditorAdapter editor){
		this.editor = editor;
	}
	
	public EditorAdapter getEditor(){
		return editor;
	}
	
	public abstract String getEquipType(Element ele);
	
	public abstract String getVoltageLevel(Element ele);
	
	public abstract String getPSMSID(Element ele);//getPSMSID
	
	public abstract String getScadaID(Element ele);//scada object id
	
	public abstract String getCIMType(Element ele);
	
	public abstract String getName(Element ele);
	
	public abstract String getParentID(Element ele);
	/**
	 * ªÒ»°»›¡ø
	 * @param ele
	 * @return
	 */
	public abstract String getCapability(Element ele);
	
	
//	public abstract String getOthers(Element ele);
	
	public abstract String getTooltipString(Element selectedShapeEle);
}
