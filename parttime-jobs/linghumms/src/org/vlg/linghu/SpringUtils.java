package org.vlg.linghu;

public class SpringUtils {
	
	public static Object getBean(String beanName){
		return InitServlet.sprintContext.getBean(beanName);
	}

}
