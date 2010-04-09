package com.nci.domino.beans.desyer;

import java.util.Comparator;

/**
 * wofo bean的比较器，用于顺序排序，暂时是transition放在最后
 * 
 * @author Qil.Wong
 * 
 */
public class WofoBeanComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if (o2 instanceof WofoTransitionBean) {
			return -1;
		}
		return 0;
	}

}
