/*
 * @(#)InMemDBServlet.java	1.0  08/23/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.startup;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.creaway.inmemdb.core.InMemDBServer;

/**
 * 内存数据库启动servlet，必须第一个启动。 <code>load-on-startup</code>设置为<b>0</b>
 * 
 * @author Qil.Wong
 * 
 */
public class InMemDBServlet extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8797718888122688649L;

	public void init() {
		getServletContext().setAttribute("inmemdb",
				InMemDBServer.getInstance().getJndiContextObject());
	}

	protected void doGet(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {
		doPost(httpservletrequest, httpservletresponse);
	}

	protected void doPost(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {

	}
}
