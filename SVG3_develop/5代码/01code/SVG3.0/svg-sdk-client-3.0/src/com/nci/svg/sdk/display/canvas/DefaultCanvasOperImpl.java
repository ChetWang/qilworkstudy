package com.nci.svg.sdk.display.canvas;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.shape.GroupBreakerIF;
import com.nci.svg.sdk.topology.TopologyManagerAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class DefaultCanvasOperImpl extends CanvasOperAdapter {


	public DefaultCanvasOperImpl(EditorAdapter editor, SVGHandle handle) {
		super(editor, handle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GroupBreakerIF createGroupBreaker() {
		GroupBreakerIF beaker = new GroupBreakerIF(){

			@Override
			public boolean breakGroup(Element groupElement) {
				if(groupElement.getAttribute("id").indexOf("layer")>=0){
					return true;
				}
				return false;
			}
			
		};
		return beaker;
	}

	@Override
	protected TopologyManagerAdapter createTopologyManagerAdapter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
