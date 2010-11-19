package com.creaway.inmemdb.slaveconn;

import java.util.Date;
import java.util.HashMap;

public class EqualTest {

	public static void main(String[] xx) {
		XBean b1 = new XBean();
		b1.x = 1;
		long t = System.currentTimeMillis();
		b1.d = new Date(t);
		XBean b2 = new XBean();
		b2.x = 1;
		b2.d = new Date(t);
		System.out.println(b2.equals(b1));
		HashMap<XBean, String> m = new HashMap<XBean, String>();
		m.put(b1, "xxxxxx");
		System.out.println(m.containsKey(b2));
	}

	public static class XBean {

		public int x;

		public Date d;

		public boolean equals(Object o) {
			if (o instanceof XBean) {
				XBean xb = (XBean) o;
				if (d == null && xb.d == null) {
					return x == xb.x;
				}
				if (d != null && xb.d != null) {
					return x == xb.x && d.equals(xb.d);
				}
			}
			return false;
		}

		public int hashCode() {
			int ramdom = 41;
			int dHash = d == null ? 0 : d.hashCode();
			return x ^ 3 + ramdom * dHash;
//			return 11123;
		}
	}

}
