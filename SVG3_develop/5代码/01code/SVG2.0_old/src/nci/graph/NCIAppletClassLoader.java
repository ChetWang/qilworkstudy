/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.graph;

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
 * �������������svgƽ̨applet����Ƚϴ�ÿ�δӷ��������ػ����Ĵ�����Դ��
 * ��������ṩһ�������������applet��һ������ʱ���ص�jar�ļ����ؽ�����
 * @author Qil.Wong
 */
public class NCIAppletClassLoader {
	

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

    public static ClassLoader getSystemClassLoader() {
    	ClassLoader loader = ClassLoader.getSystemClassLoader();

        return loader;
    }

    public static ClassLoader getExtClassLoader() {
    	ClassLoader loader = getSystemClassLoader().getParent();

        return loader;
    }

    public static List getClassesLoadedBySystemClassLoader() {
        return getClassesLoadedByClassLoader(getSystemClassLoader());
    }

    public static List getClassesLoadedByExtClassLoader() {
        return getClassesLoadedByClassLoader(getExtClassLoader());
    }

    public static List getClassesLoadedByClassLoader(ClassLoader cl) {
        try {
            return (List) classes.get(cl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL[] getBootstrapURLs() {
        return Launcher.getBootstrapClassPath().getURLs();
    }

    public static URL[] getSystemURLs() {
        return systemLoader.getURLs();
    }

    public static URL[] getExtURLs() {
        return extLoader.getURLs();
    }

    private static void list(PrintStream ps, URL[] classPath) {
        for (int i = 0; i < classPath.length; i++) {
            ps.println(classPath[i]);
        }
    }

    public static void listBootstrapClassPath() {
        listBootstrapClassPath(System.out);
    }

    public static void listBootstrapClassPath(PrintStream ps) {
        ps.println("BootstrapClassPath:");
        list(ps, getBootstrapClassPath());
    }

    public static void listSystemClassPath() {
        listSystemClassPath(System.out);
    }

    public static void listSystemClassPath(PrintStream ps) {
        ps.println("SystemClassPath:");
        list(ps, getSystemClassPath());
    }

    public static void listExtClassPath() {
        listExtClassPath(System.out);
    }

    public static void listExtClassPath(PrintStream ps) {
        ps.println("ExtClassPath:");
        list(ps, getExtClassPath());
    }

    public static URL[] getBootstrapClassPath() {
        return getBootstrapURLs();
    }

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

    public static void addClassPath(String path) {
        addClassPath(new File(path));
    }

    public static void addExtClassPath(String path) {
        addExtClassPath(new File(path));
    }

    /**
     * ���ļ���Ŀ¼���ؽ�ClassPath
     * @param dirOrJar
     */
    public static void addClassPath(File dirOrJar) {
        try {
            addURL2SystemClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void addExtClassPath(File dirOrJar) {
        try {
            addURL2ExtClassLoader(dirOrJar.toURI().toURL());
        } catch (MalformedURLException e) {
            //throw new RootException(e);
            e.printStackTrace();
        }
    }

    
//    private static void loadExternalClasspath() {
//        String appJarPath = System.getProperty("user.dir") + "/.svg";
//        File ext_dir = new File(appJarPath);
//        File[] jars = ext_dir.listFiles();
//        if (jars != null) {
//            for (int i = 0; i < jars.length; i++) {
//                if (jars[i].getName().lastIndexOf(".jar") >= 0) {
//                    AppletClassLoader.addExtClassPath(jars[i]);
//                }
//            }
//        }
//    }
}

