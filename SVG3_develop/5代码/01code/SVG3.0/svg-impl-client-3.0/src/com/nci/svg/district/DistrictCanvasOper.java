package com.nci.svg.district;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.display.canvas.CanvasOperAdapter;
import com.nci.svg.sdk.shape.GroupBreakerIF;
import com.nci.svg.sdk.topology.TopologyManagerAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class DistrictCanvasOper extends CanvasOperAdapter {

	public DistrictCanvasOper(EditorAdapter editor, SVGHandle handle) {
		super(editor, handle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GroupBreakerIF createGroupBreaker() {
		DistrictGroupBreak districtGroupBreak = new DistrictGroupBreak();
		return districtGroupBreak;
	}

	@Override
	protected TopologyManagerAdapter createTopologyManagerAdapter() {
		DistrictTopologyManager districtTopologyManager = new DistrictTopologyManager(
				editor, handle);
		return districtTopologyManager;
	}

}
