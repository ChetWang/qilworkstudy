package com.nci.domino.components.autocomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import com.nci.domino.components.table.tablesorter.ColumnComparator;
import com.nci.domino.help.pinyin.PinyinHelper;

/**
 * 
 * @author Qil.Wong
 */
public class DefaultCompletionFilter implements CompletionFilter {

	private Vector<DefaultMutableTreeNode> vector;

	public DefaultCompletionFilter() {
		vector = new Vector<DefaultMutableTreeNode>();
	}

	public DefaultCompletionFilter(Vector<DefaultMutableTreeNode> v) {
		vector = v;
	}

	public List filter(String text) {
		List list = new ArrayList();
		String txt = text.trim();
		int length = txt.length();
		for (int i = 0; i < vector.size(); i++) {
			Object o = vector.get(i).getUserObject();
			String str = o.toString();
			if (length == 0 || str.indexOf(text) >= 0)
				list.add(o);
		}
		Collections.sort(list, new MyComparator());
		return list;
	}

	public Vector<DefaultMutableTreeNode> getVector() {
		return vector;
	}

	private class MyComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			if (o1 != null && o2 != null) {
				return PinyinHelper.getAllPinyin(o1.toString()).compareTo(
						PinyinHelper.getAllPinyin(o2.toString()));
			}
			return 1;
		}

	}
}
