/*
 * Sensor.java
 *
 * Created on 2007-7-22, 17:29:55
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author William Chen
 */
public class Sensor {
    private List<Listener> wrappers;

    public Sensor(Element eSensor) {
        String component_class_name = eSensor.getAttribute("component");
        wrappers = new ArrayList<Listener>();
        NodeList list = eSensor.getElementsByTagName("listener");
        if (list != null && list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                Element eListener = (Element) list.item(i);
                wrappers.add(new Listener(component_class_name, eListener));
            }
        }
    }

    public void addHandler(JComponent source, InvocationHandler handler) {
        for (Listener wrapper : wrappers) {
            wrapper.addHandler(source, handler);
        }
    }
}
