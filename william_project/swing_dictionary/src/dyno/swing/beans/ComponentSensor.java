/*
 * ComponentEventInvoker.java
 *
 * Created on 2007-7-22, 17:17:17
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author William Chen
 */
public class ComponentSensor {
    private HashMap<Class<? extends JComponent>, Sensor> sensors;
    
    public ComponentSensor() {
        sensors = new HashMap<Class<? extends JComponent>, Sensor>();
    }
    
    public void initialize() {
        try {
            InputStream input = getClass().getResourceAsStream("sensor.xml");
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            Element eSensors = document.getDocumentElement();
            NodeList nodes = eSensors.getElementsByTagName("sensor");
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element eSensor = (Element) nodes.item(i);
                    String className = eSensor.getAttribute("component");
                    try{
                        Class<? extends JComponent> clazz = parseClass(className);
                        sensors.put(clazz, new Sensor(eSensor));
                    }catch(ClassNotFoundException ex){
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Class<? extends JComponent> parseClass(String className)
    throws ClassNotFoundException {
        Class clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
        if (JComponent.class.isAssignableFrom(clazz))
            return (Class<? extends JComponent>) clazz;
        else
            throw new IllegalArgumentException(className+" must be instance of JComponent");
    }
    
    public List<Sensor> getSensor(Class<? extends JComponent> comp_clazz) {
        ArrayList<Sensor> sensor_array=new ArrayList<Sensor>();
        init_sensor(comp_clazz, sensor_array);
        return sensor_array;
    }
    private void init_sensor(Class<? extends JComponent> comp_clazz, ArrayList<Sensor> array){
        Class superclass=comp_clazz.getSuperclass();
        if(superclass!=null && JComponent.class.isAssignableFrom(superclass))
            init_sensor(superclass, array);
        Sensor sensor=sensors.get(comp_clazz);
        if(sensor!=null)
            array.add(sensor);
    }
}
