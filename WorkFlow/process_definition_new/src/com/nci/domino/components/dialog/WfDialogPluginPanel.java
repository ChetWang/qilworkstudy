package com.nci.domino.components.dialog;

import com.nci.domino.components.WfInputPanel;

/**
 * WfDialog�Ի����µĲ����常����
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfDialogPluginPanel extends WfInputPanel {

	protected WfDialog dialog;

	public WfDialogPluginPanel(WfDialog dialog) {
		this.dialog = dialog;
	}

}
