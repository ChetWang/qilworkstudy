/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.util;

import java.text.SimpleDateFormat;

/**
 *
 * @author Qil.Wong
 */
public class KnoUtil {

    public static boolean silent;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static byte[] sdfLock = new byte[0];
    public static String dbUrl;

    public static void print(Object msg) {
        if (!silent) {
            System.out.println(msg);
        }
    }

    public static void print(int msg) {
        if (!silent) {
            System.out.println(msg);
        }
    }

    public static SimpleDateFormat getDateFormat() {
        synchronized (sdfLock) {
            return sdf;
        }
    }

    public static String getCurrentTimeStr(String format) {
        SimpleDateFormat current = new SimpleDateFormat(format);
        String currentTime = current.format(new java.util.Date());
        return currentTime;
    }
}
