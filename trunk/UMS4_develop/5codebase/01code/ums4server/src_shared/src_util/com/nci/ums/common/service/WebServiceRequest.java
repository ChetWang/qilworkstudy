// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   WebServiceRequest.java

package com.nci.ums.common.service;


public interface WebServiceRequest
{

    public abstract Object invoke(String s, String s1, Object aobj[]);

    public abstract Object invoke(Object aobj[]);
}
