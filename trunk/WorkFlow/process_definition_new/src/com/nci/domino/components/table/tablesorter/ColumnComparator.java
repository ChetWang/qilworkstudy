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
 * Description: 对查询的结果列表进行排序，通常顺序为数字--英文字母--汉字 其中，英文不分大小写，汉字以拼音顺序排列
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
 * @author 汪棋良
 * @version 1.0
 */
public class ColumnComparator implements Comparator {
	Collator myCollator = Collator.getInstance();// 获取本地语种，这里为中文，以便对中文进行按拼音顺序进行排序

	public ColumnComparator() {

	}

	/**
	 * 对列进行相互比较
	 * 
	 * @param index
	 *            int 当前列的序号
	 * @param ascending
	 *            boolean 是升序还是降序，升序为1，降序为0
	 */
	public ColumnComparator(int index, boolean ascending) {
		this.index = index;
		this.ascending = ascending;
	}

	/**
	 * 对表中的每一行按指定的列进行比较 当表格排好序后,各行就移动到了新位置
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
					// 因为compare(cOne,
					// cTwo)是将2个对象转化为
					// String进行比较，但Date型无法跟String进行转换
					// 因为 myCollator.compare(cOne, cTwo)能对中文进行有效排序
					// 所以首先还是要用到这个方法（按拼音排序）
				} catch (ClassCastException cce) {
					asc = cOne.compareTo(cTwo);// 当捕捉到Date转换String异常时，就执行cOne.compareTo(cTwo)，
					// 这个方法对中文排序支持不是很好，它以Unicode进行排序。
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
