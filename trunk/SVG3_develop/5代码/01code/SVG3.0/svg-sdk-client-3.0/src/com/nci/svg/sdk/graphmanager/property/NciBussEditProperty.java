package com.nci.svg.sdk.graphmanager.property;

import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class NciBussEditProperty extends NciBussProperty {

	public NciBussEditProperty(EditorAdapter editor,
			ModelRelaIndunormBean bean, SVGHandle handle) {
		super(editor, bean, handle);
		setType(String.class);
		setEditable(true);
	}

}
