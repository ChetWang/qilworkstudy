package com.nci.domino.beans.desyer;

import java.util.Comparator;

/**
 * wofo bean�ıȽ���������˳��������ʱ��transition�������
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
