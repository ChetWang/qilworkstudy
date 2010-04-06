package com.nci.svg.sdk.util;

import java.io.Serializable;

public abstract class ObjectChangeListener implements Serializable{

	public abstract void objectAdded();
	
	public abstract void objectRemoved();

}
