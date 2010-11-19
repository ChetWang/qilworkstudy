/*
 * @(#)NameParserImpl.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.jndi;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * JNDI命名转换器
 * @author Qil.Wong
 *
 */
public class NameParserImpl implements NameParser {
    public Name parse(String name) throws NamingException {
        return new CompositeName(name);
    }
}
