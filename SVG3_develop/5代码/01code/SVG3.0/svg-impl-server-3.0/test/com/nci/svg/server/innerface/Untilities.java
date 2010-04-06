package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Untilities {
	/**
	 * 打印两层哈希表里的数据
	 * 
	 * @param codeHash
	 */
	public static void displayCodeHash(Map codeHash) {
		Iterator it = codeHash.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			System.out.println(key);
			Object subObject = entry.getValue();
			if (subObject instanceof HashMap) {
				HashMap subHash = (HashMap) subObject;

				Iterator subIt = subHash.entrySet().iterator();
				while (subIt.hasNext()) {
					Map.Entry subEntry = (Map.Entry) subIt.next();
					Object subKey = subEntry.getKey();
					System.out.print("\t" + subKey + ":");
					Object value = subEntry.getValue();
					System.out.println(value + ";");
				}
			}
		}
	}

}
