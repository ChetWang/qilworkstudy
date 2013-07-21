package org.vlg.linghu.vac;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.axis.transport.http.AxisServlet;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class VACAxisServlet extends AxisServlet {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext())
				.getAutowireCapableBeanFactory().autowireBean(this);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				getServletContext());
	}

}
