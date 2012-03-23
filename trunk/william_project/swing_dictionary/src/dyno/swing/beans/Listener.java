package dyno.swing.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Listener {
    private String component_class_name;
    private String source;
    private String constraints;
    private String listener_class_name;
    private List<String> events;
    
    private Method addMethod;
    private Class listener_class;
    private Method getMethod;
    private Class component_class;
    
    public Listener(String component_class_name, Element eListener) {
        this.component_class_name = component_class_name;
        source = eListener.getAttribute("source");
        if (source == null || source.length() == 0) {
            source = "this";
        }
        constraints = eListener.getAttribute("constraints");
        if (constraints == null || constraints.length() == 0) {
            constraints = null;
        }
        listener_class_name = eListener.getAttribute("class");
        events = new ArrayList<String>();
        NodeList list = eListener.getElementsByTagName("event");
        if (list != null && list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                Element eEvent = (Element) list.item(i);
                String event_name = eEvent.getAttribute("name");
                events.add(event_name);
            }
        }
    }
    
    public void addHandler(JComponent src, InvocationHandler handler) {
        Object target = src;
        if (!source.equals("this")) {
            target = getTarget(src);
        }
        if (target != null) {
            addTargetHandler(target, handler);
        }
    }
    
    private void addTargetHandler(Object target, final InvocationHandler handler) {
        try {
            Class clazz = getListenerClass();
            if(clazz!=null){
                InvocationHandler h = new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Class clazz=method.getReturnType();
                        String method_name=method.getName();
                        if(clazz.isPrimitive() && clazz==boolean.class)
                            return Boolean.FALSE;
                        else if (events!=null && events.contains(method_name))
                            return handler.invoke(proxy, method, args);
                        return null;
                    }
                };
                Object listenerObject = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, h);
                Method method=getAddMethod(target.getClass());
                if(method!=null){
                    if(constraints!=null)
                        method.invoke(target, constraints, listenerObject);
                    else
                        method.invoke(target, listenerObject);
                }
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private Method getAddMethod(Class clazz){
        if (addMethod==null){
            try {
                String lName = listener_class_name;
                int index = lName.lastIndexOf('.');
                if (index != -1) {
                    lName = lName.substring(index + 1);
                }
                String addName = "add" + lName;
                Class listenerClass=getListenerClass();
                if(listenerClass!=null){
                    if(constraints!=null)
                        addMethod = clazz.getMethod(addName, String.class, listenerClass);
                    else
                        addMethod = clazz.getMethod(addName, listenerClass);
                }
            }  catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return addMethod;
    }
    private Class getListenerClass() {
        if (listener_class==null){
            try {
                listener_class = Class.forName(listener_class_name, true, Thread.currentThread().getContextClassLoader());
            }  catch (ClassNotFoundException ex) {
            }
        }
        return listener_class;
    }
    private Object getTarget(JComponent src) {
        try {
            return getGetMethod().invoke(src);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private Method getGetMethod() {
        if (getMethod==null) {
            try {
                String method_name = "get" + Character.toUpperCase(source.charAt(0)) + source.substring(1);
                getMethod = getComponentClass().getMethod(method_name);
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getMethod;
    }
    private Class getComponentClass(){
        if (component_class==null){
            try {
                component_class = Class.forName(this.component_class_name, true, Thread.currentThread().getContextClassLoader());
            }  catch (ClassNotFoundException ex) {
            }
        }
        return component_class;
    }
}