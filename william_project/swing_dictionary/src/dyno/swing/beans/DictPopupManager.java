/*
 * DictPopupManager.java
 *
 * Created on 2007-7-4, 17:13:27
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author William Chen
 */
public class DictPopupManager implements MouseInputListener, ActionListener{
    static{
        screen_size=Toolkit.getDefaultToolkit().getScreenSize();
    }
    private static JWindow popup;
    private static Text last;
    private static Dimension screen_size;
    private static HashMap<Window, JWindow> popupCache=new HashMap<Window, JWindow>();
    private static JWindow nullWindow=new JWindow();
    private static boolean locked;
    private static int fixed_x, fixed_y;
    
    private static Dictionary dictionary=new CacheDictionary();
    
    private JWindow popupWindow;
    private int pressedx, pressedy;
    private Clock clock;
    
    private DictPopupManager(JWindow window, Clock clock){
        this.popupWindow=window;
        this.clock=clock;
    }
    private static JWindow getCachedWindow(Window win){
        if(win==null)
            return nullWindow;
        JWindow jwin=popupCache.get(win);
        if(jwin==null){
            if(win instanceof Frame)
                jwin=new JWindow((Frame)win);
            else
                jwin=new JWindow(win);
            popupCache.put(win, jwin);
        }
        return jwin;
    }

    public static void show(Clock clock, Text text, Component src, int x, int y) {
        if(!text.equals(last))
            hide();
        else if(isVisible())
            return;
        Window window=SwingUtilities.getWindowAncestor(src);
        if(src.isVisible() && window!=null && window.isVisible()){
            last=text;
            DictToolTip tooltip = new DictToolTip();
            tooltip.setLocked(locked);
            tooltip.setFont(new Font("Times New Roman", 0, 14));
            String word=last.getText();
            String translation=dictionary.translate(tooltip, word);
            if(translation!=null)
                tooltip.setTipText(translation);
            else
                dictionary.asynchronizedTranslate(clock, last, src, tooltip, x, y);
            int lx, ly;
            if(locked){
                Dimension prefSize = tooltip.getPreferredSize();
                lx=fixed_x-prefSize.width;
                ly=fixed_y;
            }else{
                Point screenLocation = src.getLocationOnScreen();
                Dimension prefSize = tooltip.getPreferredSize();
                if(screenLocation.y + text.getY() + text.getHeight() + prefSize.height < screen_size.height){
                    ly = screenLocation.y + text.getY() + text.getHeight();
                    if(screenLocation.x + text.getX() + text.getWidth() + prefSize.width < screen_size.width)
                        lx = screenLocation.x + text.getX() + text.getWidth();
                    else
                        lx = screen_size.width - prefSize.width;
                } else{
                    ly = screenLocation.y + text.getY() - prefSize.height;
                    if(screenLocation.x + text.getX() + text.getWidth() + prefSize.width < screen_size.width)
                        lx = screenLocation.x + text.getX() + text.getWidth();
                    else
                        lx = screen_size.width - prefSize.width;
                }
            }
            if(!locked || popup==null)
                popup=getCachedWindow(SwingUtilities.getWindowAncestor(src));
            popup.getContentPane().removeAll();
            popup.getContentPane().add(tooltip, BorderLayout.CENTER);
            popup.setLocation(lx, ly);
            popup.pack();
            DictPopupManager manager=new DictPopupManager(popup, clock);
            tooltip.addActionListener(manager);
            tooltip.addMouseListener(manager);
            tooltip.addMouseMotionListener(manager);
            last.setVisible(true);
            if(!popup.isVisible())
                popup.setVisible(true);
        }
    }
    public static Text getLast(){
        return last;
    }
    public static boolean isVisible(){
        return popup != null && last != null && last.isVisible() && popup.isVisible();
    }
    public static void hide(){
        if(!locked && popup!=null){
            popup.setVisible(false);
            popup=null;
        }
        if(last!=null){
            last.setVisible(false);
            last=null;
        }
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        pressedx=e.getX();
        pressedy=e.getY();
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
        if(clock!=null && !clock.isStopped()){
            clock.setStopped(true);
        }
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
        Point p=popupWindow.getLocationOnScreen();
        p.x+=e.getX()-pressedx;
        p.y+=e.getY()-pressedy;
        popupWindow.setLocation(p);
        if(locked)
            lockWindow();
    }
    
    private void lockWindow() {
        Point popupLocation=popupWindow.getLocationOnScreen();
        Dimension dim=popupWindow.getSize();
        fixed_x=popupLocation.x+dim.width;
        fixed_y=popupLocation.y;
    }
    public void mouseMoved(MouseEvent e) {
    }
    
    public void actionPerformed(ActionEvent e) {
        DictToolTip tip=(DictToolTip)e.getSource();
        locked=tip.isLocked();
        if(locked)
            lockWindow();
        else{
        }
    }
}
