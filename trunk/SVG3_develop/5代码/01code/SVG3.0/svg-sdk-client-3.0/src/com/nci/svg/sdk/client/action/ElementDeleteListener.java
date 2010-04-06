package com.nci.svg.sdk.client.action;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

public interface ElementDeleteListener {
	
	public void elementDeleted(HashMap<Element, Element> parentNodesMap,List<Element> elementsToDelete);
	
	public void undoDelete(HashMap<Element, Element> parentNodesMap,List<Element> elementsToDelete);

}
