package com.nci.svg.district;

import java.util.Set;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.module.BusinessRootModuleAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-6
 * @功能：
 *
 */
public class DistrictRootModule extends BusinessRootModuleAdapter {

	public DistrictRootModule(EditorAdapter editor) {
		super(editor);
		
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.BusinessRootModuleAdapter#setMenuInfo()
	 */
	@Override
	public void setMenuInfo() {
		this.id = "TQT";
		this.label = "台区图";

	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.BusinessRootModuleAdapter#verifyValidity(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set)
	 */
	@Override
	protected boolean verifyValidity(SVGHandle handle,
			Set<Element> selectedElements) {

		if(handle == null)
			return false;
		
		if(handle.getCanvas().getFileType() != null && handle.getCanvas().getFileType().equals("district"))
		    return true;
		
		return false;
	}

	@Override
	protected void initHandle(SVGHandle handle) {
		// TODO Auto-generated method stub
		// add by yux,2009-3-9
		
	}

	@Override
	protected void initSonModules() {
		MainPathShape shape = new MainPathShape(editor);
		this.addModuleAdapter(shape);
		DistrictAutoRela rela = new DistrictAutoRela(editor);
		this.addModuleAdapter(rela);
	}

}
