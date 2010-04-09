package com.nci.domino.components.dialog;

import com.nci.domino.components.WfInputPanel;

/**
 * WfDialog对话框下的插件面板父对象
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
