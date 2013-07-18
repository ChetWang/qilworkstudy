package org.vlg.linghu;

import java.lang.reflect.Method;

public class MM7Utils {

	public String parseString(Object bean){
		StringBuilder sb = new StringBuilder();
		Method[] methods = bean.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().startsWith("get")) {
				try {
					Object value = m.invoke(bean, new Object[] {});
					sb.append(m.getName() + " --> " + value);
					sb.append(" | ");
				} catch (Exception e) {
				}
			}
		}
		return sb.toString();
	}

}
