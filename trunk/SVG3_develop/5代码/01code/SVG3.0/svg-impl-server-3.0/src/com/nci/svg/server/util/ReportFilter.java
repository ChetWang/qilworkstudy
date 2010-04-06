package com.nci.svg.server.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 * 标题：ReportFilter.java
 * </p>
 * <p>
 * 描述： servlet请求过滤类，将servlet的请求中的编码修改为UTF-8
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-15
 * @version 1.0
 */
public class ReportFilter implements Filter {

	protected String encoding;
	protected FilterConfig filterConfig;

	public ReportFilter() {
		encoding = null;
		filterConfig = null;
	}

	public void destroy() {
		encoding = null;
		filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// if(encoding != null)
		request.setCharacterEncoding("ISO-8859-1");
		// System.out.println(request.getCharacterEncoding());
		Map reqmap = request.getParameterMap();
		Iterator iterator = reqmap.keySet().iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (obj instanceof String) {
				String name = (String) obj;
				String[] value = (String[]) reqmap.get(name);
				for (int i = 0; i < value.length; i++) {
					value[i] = new String(value[i].getBytes("ISO-8859-1"),
							encoding);
//					String temp = new String(value[i].getBytes("ISO-8859-1"),
//							"GBK");
//					System.out.println(temp);
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		encoding = filterConfig.getInitParameter("encoding");
	}
}
