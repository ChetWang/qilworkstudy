package com.nci.svg.sdk;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import sun.misc.Launcher;
/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-24
 * @���ܣ���̬JVM������
 *
 */
public class NCIClassLoader {
	

    /**
     * �����ص���
     */
    private static Field classes;
    
    /**
     * ����URL�ķ���
     */
    private static Method addURL;
    

    static {
        try {
            classes = ClassLoader.class.getDeclaredField("classes");
            addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        } catch (Exception e) {
            //won't happen ,but remain it
            //throw new RootException(e);
            e.printStackTrace();
        }
        classes.setAccessible(true);
        addURL.setAccessible(true);
    }
    /**
     * ϵͳ�������
     */
    private static URLClassLoader systemLoader = (URLClassLoader) getSystemClassLoader();
    /**
     * �������������
     */
    private static URLClassLoader extLoader = (URLClassLoader) getExtClassLoader();
    
    /**
     * ���������ƴ�ϵͳ��������У���ȡ�����
     * 
     * @param className��������
     * @return�������
     * @throws ClassNotFoundException����δ�ڼ������б���ȷ��ѯ��
     */
    public static Class loadSystemClass(String className) throws ClassNotFoundException
    {
    	return systemLoader.loadClass(className);
    }
    
    /**
     * ���������ƴӵ�������������У���ȡ�����
     * 
     * @param className��������
     * @return�������
     * @throws ClassNotFoundException����δ�ڼ������б���ȷ��ѯ��
     */
    public static Class loadExtClass(String className) throws ClassNotFoundException
    {
    	return extLoader.loadClass(className);
    } 

    /**
     * ��ȡϵͳ�������
     * @return��ϵͳ�������
     */
    public static ClassLoader getSystemClassLoader() {
    	ClassLoader loader = ClassLoader.getSystemClassLoader();
    	System.out.println("System ClassLoader:"+loader.toString());
        return loader;
    }

    /**
     * ��ȡ�������������
     * @return���������������
     */
    public static ClassLoader getExtClassLoader() {
    	ClassLoader loader = getSystemClassLoader().getParent();
    	System.out.println("Ext ClassLoader:"+loader.toString());
        return loader;
    }

    /**
     * ��ϵͳ���������ȡ�ѱ����ص����嵥
     * @return�����嵥List
     */
    public static List getClassesLoadedBySystemClassLoader() {
        return getClassesLoadedByClassLoader(getSystemClassLoader());
    }

    /**
     * �ӵ��������������ȡ�ѱ����ص����嵥
     * @return�����嵥List
     */
    public static List getClassesLoadedByExtClassLoader() {
        return getClassesLoadedByClassLoader(getExtClassLoader());
    }

    /**
     * �������������ȡ�ѱ����ص����嵥
     * @param cl�������������
     * @return�����嵥List
     */
    public static List getClassesLoadedByClassLoader(ClassLoader cl) {
        try {
            return (List) classes.get(cl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @return
     */
    public static URL[] getBootstrapURLs() {
        return Launcher.getBootstrapClassPath().getURLs();
    }

    /**
     * 
     * @return
     */
    public static URL[] getSystemURLs() {
        return systemLoader.getURLs();
    }

    /**
     * 
     * @return
     */
    public static URL[] getExtURLs() {
        return extLoader.getURLs();
    }

    private static void list(PrintStream ps, URL[] classPath) {
        for (int i = 0; i < classPath.length; i++) {
            ps.println(classPath[i]);
        }
    }

    /**
     * 
     */
    public static void listBootstrapClassPath() {
        listBootstrapClassPath(System.out);
    }

    /**
     * 
     * @param ps
     */
    public static void listBootstrapClassPath(PrintStream ps) {
        ps.println("BootstrapClassPath:");
        list(ps, getBootstrapClassPath());
    }
    /**
     * 
     */
    public static void listSystemClassPath() {
        listSystemClassPath(System.out);
    }
    /**
     * 
     * @param ps
     */
    public static void listSystemClassPath(PrintStream ps) {
        ps.println("SystemClassPath:");
        list(ps, getSystemClassPath());
    }
    /**
     * 
     */
    public static void listExtClassPath() {
        listExtClassPath(System.out);
    }
    /**
     * 
     * @param ps
     */
    public static void listExtClassPath(PrintStream ps) {
        ps.println("ExtClassPath:");
        list(ps, getExtClassPath());
    }
    /**
     * 
     */
    public static URL[] getBootstrapClassPath() {
        return getBootstrapURLs();
    }
    /**
     * 
     */
    public static URL[] getSystemClassPath() {
        return getSystemURLs();
    }

    /**
     * ��ȡext(������)���URL
     * @return
     */
    public static URL[] getExtClassPath() {
        return getExtURLs();
    }

    /**
     * ��URL��ʾ��jar�ļ����ؽ�ϵͳ�������
     * @param url
     */
    public static void addURL2SystemClassLoader(URL url) {
        try {
            addURL.invoke(systemLoader, new Object[]{url});
        } catch (Exception e) {
            //throw new RootException(e);
            e.printStackTrace();
        }
    }

    /**
     * ��URL��ʾ��jar�ļ����ؽ��������������
     * @param url
     */
    public static void addURL2ExtClassLoader(URL url) {
        try {
            addURL.invoke(extLoader, new Object[]{url});
        } catch (Exception e) {
            //throw new RootException(e);
            e.printStackTrace();
        }
    }

    /**
     * ��ָ��·�����ļ���Ŀ¼������ϵͳ���������
     * @param path���ļ���Ŀ¼��
     */
    public static void addClassPath(String path) {
        addClassPath(new File(path));
    }
    /**
     * ��ָ��·�����ļ���Ŀ¼���������������������
     * @param path���ļ���Ŀ¼��
     */
    public static void addExtClassPath(String path) {
        addExtClassPath(new File(path));
    }

    /**
     * ���ļ���Ŀ¼���ؽ�systemClassPath
     * @param dirOrJar���ļ���Ŀ¼
     */
    public static synchronized void addClassPath(File dirOrJar) {
        try {
            addURL2SystemClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ļ���Ŀ¼���ص�ExtClassPath
     * @param dirOrJar���ļ���Ŀ¼
     */
    public static void addExtClassPath(File dirOrJar) {
        try {
            addURL2ExtClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            //throw new RootException(e);
            e.printStackTrace();
        }
    }
}
