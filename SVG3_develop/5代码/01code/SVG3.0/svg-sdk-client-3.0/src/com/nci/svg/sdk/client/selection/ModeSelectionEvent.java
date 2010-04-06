package com.nci.svg.sdk.client.selection;

import java.util.EventObject;

public class ModeSelectionEvent extends EventObject{

	private static final long serialVersionUID = -8558062900424295356L;

	public ModeSelectionEvent(ModeObject source) {
		super(source);
	}
	
	public ModeObject getSource(){
		return (ModeObject)source;
	}

}
