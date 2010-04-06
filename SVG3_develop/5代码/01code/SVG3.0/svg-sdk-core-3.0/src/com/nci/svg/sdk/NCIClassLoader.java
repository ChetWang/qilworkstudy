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
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-24
 * @功能：动态JVM操作类
 *
 */
public class NCIClassLoader {
	

    /**
     * 欲加载的类
     */
    private static Field classes;
    
    /**
     * 加载URL的方法
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
     * 系统类加载器
     */
    private static URLClassLoader systemLoader = (URLClassLoader) getSystemClassLoader();
    /**
     * 第三方类加载器
     */
    private static URLClassLoader extLoader = (URLClassLoader) getExtClassLoader();
    
    /**
     * 根据类名称从系统类加载器中，获取类对象
     * 
     * @param className：类名称
     * @return：类对象
     * @throws ClassNotFoundException：类未在加载器中被正确查询到
     */
    public static Class loadSystemClass(String className) throws ClassNotFoundException
    {
    	return systemLoader.loadClass(className);
    }
    
    /**
     * 根据类名称从第三方类加载器中，获取类对象
     * 
     * @param className：类名称
     * @return：类对象
     * @throws ClassNotFoundException：类未在加载器中被正确查询到
     */
    public static Class loadExtClass(String className) throws ClassNotFoundException
    {
    	return extLoader.loadClass(className);
    } 

    /**
     * 获取系统类加载器
     * @return：系统类加载器
     */
    public static ClassLoader getSystemClassLoader() {
    	ClassLoader loader = ClassLoader.getSystemClassLoader();
    	System.out.println("System ClassLoader:"+loader.toString());
        return loader;
    }

    /**
     * 获取第三方类加载器
     * @return：第三方类加载器
     */
    public static ClassLoader getExtClassLoader() {
    	ClassLoader loader = getSystemClassLoader().getParent();
    	System.out.println("Ext ClassLoader:"+loader.toString());
        return loader;
    }

    /**
     * 从系统类加载器获取已被加载的类清单
     * @return：类清单List
     */
    public static List getClassesLoadedBySystemClassLoader() {
        return getClassesLoadedByClassLoader(getSystemClassLoader());
    }

    /**
     * 从第三方类加载器获取已被加载的类清单
     * @return：类清单List
     */
    public static List getClassesLoadedByExtClassLoader() {
        return getClassesLoadedByClassLoader(getExtClassLoader());
    }

    /**
     * 根据类加载器获取已被加载的类清单
     * @param cl：类加载器对象
     * @return：类清单List
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
     * 获取ext(第三方)类的URL
     * @return
     */
    public static URL[] getExtClassPath() {
        return getExtURLs();
    }

    /**
     * 将URL表示的jar文件加载进系统类加载器
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
     * 将URL表示的jar文件加载进第三方类加载器
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
     * 将指定路径的文件或目录加载至系统类加载器中
     * @param path：文件或目录名
     */
    public static void addClassPath(String path) {
        addClassPath(new File(path));
    }
    /**
     * 将指定路径的文件或目录加载至第三方类加载器中
     * @param path：文件或目录名
     */
    public static void addExtClassPath(String path) {
        addExtClassPath(new File(path));
    }

    /**
     * 将文件或目录加载进systemClassPath
     * @param dirOrJar：文件或目录
     */
    public static synchronized void addClassPath(File dirOrJar) {
        try {
            addURL2SystemClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件或目录加载到ExtClassPath
     * @param dirOrJar：文件或目录
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
