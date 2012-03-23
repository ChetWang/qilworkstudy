/*
 * WordPicker.java
 *
 * Created on 2007-7-4, 16:26:16
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author William Chen
 */
public class WordPicker implements AWTEventListener {
    
    private static ComponentSensor sensor;
    static {
        sensor = new ComponentSensor();
        sensor.initialize();
    }
    private JComponent component;
    private ArrayList<Text> texts;
    private boolean stale;
    private PopupAction popupAction;
    private Clock clock;
    
    public WordPicker(JComponent component) {
        this.component = component;
        stale = true;
        List<Sensor> sensors=sensor.getSensor(component.getClass());
        for(Sensor sens:sensors){
            sens.addHandler(component, new InvocationHandler(){
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    stale=true;
                    return null;
                }
            });
        }
        popupAction = new PopupAction();
    }
    
    private Image offImage;
    
    Graphics getOffGraphics() {
        if (offImage == null) {
            offImage = component.createImage(1, 1);
        }
        return offImage.getGraphics();
    }
    private ProxyGraphics proxyGraphics;
    
    private void pickupTexts() {
        stale = false;
        if (proxyGraphics == null) {
            proxyGraphics = new ProxyGraphics((Graphics2D) getOffGraphics());
        } else {
            proxyGraphics.recycle((Graphics2D) getOffGraphics());
        }
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        clearDoubleBuffer(component, dbcomponents);
        component.paint(proxyGraphics);
        resetDoubleBuffer(dbcomponents);
        texts = proxyGraphics.getTexts();
        Collections.sort(texts);
    }
    
    private void mouseMoved(MouseEvent e) {
        if (stale) {
            pickupTexts();
        }
        if (DictPopupManager.isVisible()) {
            check_translation_popup(e);
            return;
        }
        if (clock != null) {
            if (clock.isStopped()) {
                clock.setStopped(false);
                clock.start();
            }
        } else {
            clock = new Clock(popupAction);
            clock.start();
        }
        clock.setStart(e.getWhen());
        popupAction.setLocation(e.getX(), e.getY());
    }
    
    private void check_translation_popup(MouseEvent e) {
        Text last = DictPopupManager.getLast();
        int x = e.getX();
        int y = e.getY();
        if (x < last.getX() || x > last.getX() + last.getWidth() || y < last.getY() || y > last.getY() + last.getHeight()) {
            if (clock != null) {
                if (clock.isStopped()) {
                    clock.setStopped(false);
                    clock.setStart(e.getWhen());
                    clock.start();
                }
            } else {
                clock = new Clock(popupAction);
                clock.setStart(e.getWhen());
                clock.start();
            }
        }
        popupAction.setLocation(e.getX(), e.getY());
    }
    
    private class PopupAction implements ActionListener {
        
        private int x;
        private int y;
        
        public PopupAction() {
        }
        
        public void setLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void actionPerformed(ActionEvent e) {
            Text text=searchText(texts, 0, texts.size()-1);
            if(text==null)
                DictPopupManager.hide();
            else if(!text.isVisible())
                DictPopupManager.show(clock, text, component, x, y);
        }
        private Text searchText(ArrayList<Text> texts, int low, int high){
            if(high<low)
                return null;
            int mid=(low+high)/2;
            Text midText=texts.get(mid);
            if(midText.contains(x, y))
                return midText;
            if(y>=midText.getY()&&y<=midText.getY()+midText.getHeight()){
                for(int i=mid;i<=high;i++){
                    Text tmp_text=texts.get(i);
                    if(tmp_text.getY()>midText.getY()+midText.getHeight())
                        break;
                    else if(tmp_text.contains(x, y))
                        return tmp_text;
                }
                for(int i=mid;i>=low;i--){
                    Text tmp_text=texts.get(i);
                    if(tmp_text.getY()+tmp_text.getHeight()<midText.getY())
                        break;
                    else if(tmp_text.contains(x, y))
                        return tmp_text;
                }
                return null;
            }else if(y<midText.getY()){
                return searchText(texts, low, mid-1);
            }else
                return searchText(texts, mid+1, high);
        }
    }
    
    private void resetDoubleBuffer(ArrayList<JComponent> dbcomponents) {
        for (JComponent jc : dbcomponents) {
            jc.setDoubleBuffered(true);
        }
    }
    
    private void clearDoubleBuffer(Component c, ArrayList<JComponent> dbcomponents) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (jc.isDoubleBuffered()) {
                jc.setDoubleBuffered(false);
                dbcomponents.add(jc);
            }
            int count = jc.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component comp = jc.getComponent(i);
                clearDoubleBuffer(comp, dbcomponents);
            }
        }
    }
    
    public void eventDispatched(AWTEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            switch (event.getID()) {
                case MouseEvent.MOUSE_ENTERED:
                    mouseEntered(mouseEvent);
                    break;
                case MouseEvent.MOUSE_EXITED:
                    mouseExited(mouseEvent);
                    break;
                case MouseEvent.MOUSE_MOVED:
                    mouseMoved(mouseEvent);
                    break;
            }
        }
    }
    
    private void mouseEntered(MouseEvent mouseEvent) {
    }
    
    private void mouseExited(MouseEvent mouseEvent) {
    }
    
    private void hidePopup() {
        if (clock != null && !clock.isStopped()) {
            clock.setStopped(true);
        }
        DictPopupManager.hide();
    }
}
