package com.nci.svg.district;

import org.w3c.dom.Element;

import com.nci.svg.sdk.shape.GroupBreakerIF;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-27
 * @���ܣ�
 *
 */
public class DistrictGroupBreak implements GroupBreakerIF {

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.shape.GroupBreakerIF#breakGroup(org.w3c.dom.Element)
	 */
	@Override
	public boolean breakGroup(Element groupElement) {
		// TODO Auto-generated method stub
		// add by yux,2009-3-27
		return true;
	}

}
