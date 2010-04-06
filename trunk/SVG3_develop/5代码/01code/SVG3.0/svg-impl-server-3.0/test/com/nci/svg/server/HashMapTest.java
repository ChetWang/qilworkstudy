package com.nci.svg.server;

import java.util.HashMap;

public class HashMapTest {
	public static void main(String[] args) {
		HashMap a = new HashMap();
		HashMap b = new HashMap();
		
		a.put("a", "aaa");
		a.put("b", "bbb");
		a.put("c", "ccc");
		a.put("d", "ddd");
		
		b.put("1", "111");
		b.put("2", "222");
		b.put("3", "333");
		b.put("4", "444");
		b.put("a", "111aaa");
		
		a.putAll(b);
		System.out.println(a);
	}
}
