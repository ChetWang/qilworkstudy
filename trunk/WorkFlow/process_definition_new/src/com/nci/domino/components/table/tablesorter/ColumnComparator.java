package com.nci.domino.components.table.tablesorter;

import java.text.Collator;
import java.util.Comparator;
import java.util.Vector;

/**
 * 
 * <p>
 * Title:ColumnComparator
 * </p>
 * 
 * <p>
 * Description: �Բ�ѯ�Ľ���б��������ͨ��˳��Ϊ����--Ӣ����ĸ--���� ���У�Ӣ�Ĳ��ִ�Сд��������ƴ��˳������
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005.10
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author ������
 * @version 1.0
 */
public class ColumnComparator implements Comparator {
	Collator myCollator = Collator.getInstance();// ��ȡ�������֣�����Ϊ���ģ��Ա�����Ľ��а�ƴ��˳���������

	public ColumnComparator() {

	}

	/**
	 * ���н����໥�Ƚ�
	 * 
	 * @param index
	 *            int ��ǰ�е����
	 * @param ascending
	 *            boolean �������ǽ�������Ϊ1������Ϊ0
	 */
	public ColumnComparator(int index, boolean ascending) {
		this.index = index;
		this.ascending = ascending;
	}

	/**
	 * �Ա��е�ÿһ�а�ָ�����н��бȽ� ������ź����,���о��ƶ�������λ��
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object one, Object two) {
		if (one instanceof Vector && two instanceof Vector) {
			Vector vOne = (Vector) one;
			Vector vTwo = (Vector) two;
			Object oOne = vOne.elementAt(index);
			Object oTwo = vTwo.elementAt(index);
			Comparable cOne;
			Comparable cTwo;
			if (oOne instanceof Comparable && oTwo instanceof Comparable) {
				cOne = (Comparable) oOne;
				cTwo = (Comparable) oTwo;

			} else {
				cOne = oOne == null ? "" : oOne.toString();
				cTwo = oTwo == null ? "" : oTwo.toString();
			}
			if (oOne instanceof Integer) {
				asc = cOne.compareTo(cTwo);
			} else {
				try {
					// asc = myCollator.compare(PinyinHelper.getAllPinyin(cOne
					// .toString()), PinyinHelper.getAllPinyin(cTwo
					// .toString()));
					asc = myCollator.compare(cOne, cTwo);
					// ��Ϊcompare(cOne,
					// cTwo)�ǽ�2������ת��Ϊ
					// String���бȽϣ���Date���޷���String����ת��
					// ��Ϊ myCollator.compare(cOne, cTwo)�ܶ����Ľ�����Ч����
					// �������Ȼ���Ҫ�õ������������ƴ������
				} catch (ClassCastException cce) {
					asc = cOne.compareTo(cTwo);// ����׽��Dateת��String�쳣ʱ����ִ��cOne.compareTo(cTwo)��
					// �����������������֧�ֲ��Ǻܺã�����Unicode��������
				}
			}
			if (cTwo instanceof Integer) {
				dea = cTwo.compareTo(cOne);
			} else {
				try {
					// dea = myCollator.compare(PinyinHelper.getAllPinyin(cTwo
					// .toString()), PinyinHelper.getAllPinyin(cOne
					// .toString()));
					dea = myCollator.compare(cTwo, cOne);
				} catch (ClassCastException cce) {
					dea = cTwo.compareTo(cOne);
				}
			}
			if (ascending) {
				return asc;
			} else {
				return dea;
			}
		}
		return 1;
	}

	protected int asc;
	protected int dea;
	protected int index;
	protected boolean ascending = true;
}
