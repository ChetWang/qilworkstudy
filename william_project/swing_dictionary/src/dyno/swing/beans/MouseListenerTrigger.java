/*
 * MyClass.java
 *
 * Created on 2007-4-2, 23:23:46
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author rehte
 */
public class MouseListenerTrigger implements AWTEventListener {
    public MouseListenerTrigger() {
    }
    public void eventDispatched(AWTEvent event) {
        Object source=event.getSource();
        if(source != null && 
                event instanceof MouseEvent && 
                !(source instanceof Component && 
                isChildOfTooltip((Component)source))){
            if (source instanceof JComponent) {
                JComponent component=(JComponent) source;
                WordPicker picker=(WordPicker)component.getClientProperty("word.picker");
                if(picker==null){
                    picker=new WordPicker(component);
                    component.putClientProperty("word.picker", picker);
                }
                picker.eventDispatched(event);
            }else if(source instanceof Container){
                Container container=(Container)source;
                addMouseInputAdapter(container);
            }
        }
    }
    private boolean isChildOfTooltip(Component comp){
        if(comp instanceof JToolTip)
            return true;
        Container c=comp.getParent();
        if(c==null)
            return false;
        return isChildOfTooltip(c);
    }
    private void addMouseInputAdapter(Container container) {
        if(container instanceof JComponent){
            JComponent component=(JComponent)container;
            MouseInputAdapter adapter=(MouseInputAdapter) component.getClientProperty("mouse.input.adapter");
            if(adapter==null){
                adapter=new MouseInputAdapter(){};
                component.putClientProperty("mouse.input.adapter", adapter);
                component.addMouseListener(adapter);
                component.addMouseMotionListener(adapter);
            }
        }
        int count=container.getComponentCount();
        for(int i=0;i<count;i++){
            Component component=container.getComponent(i);
            if(component instanceof Container)
                addMouseInputAdapter((Container)component);
        }
    }
}
