package com.nci.svg.sdk.util;

import java.util.LinkedHashMap;
import java.util.Vector;

public class ActionMap extends LinkedHashMap{	
	
	private static final long serialVersionUID = 5418993573667561857L;
	
	protected Vector objectChangeListeners = new Vector();
	

	public ActionMap(){
		super();
	}
	
	public ActionMap(int size){
		super(size);
	}
	
	public void addActionListener(ObjectChangeListener listener){
		objectChangeListeners.add(listener);
	}
	
	protected void notifyAddActions(){
		for(int i=0;i<objectChangeListeners.size();i++){
			((ObjectChangeListener)objectChangeListeners.get(i)).objectAdded();
		}
	}
	
	protected void notifyRemoveActions(){
		for(int i=0;i<objectChangeListeners.size();i++){
			((ObjectChangeListener)objectChangeListeners.get(i)).objectRemoved();
		}
	}
	
	public Object put(Object key, Object value){
		Object oldValue = super.put(key, value);
		notifyAddActions();
		return oldValue;
	}
	
	public Object remove(Object key){
		Object oldValue = super.remove(key);
		notifyRemoveActions();
		return oldValue;
	}	

}
