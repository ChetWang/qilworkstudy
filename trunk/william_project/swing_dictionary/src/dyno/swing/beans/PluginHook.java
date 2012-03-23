/*
 * PluginHook.java
 *
 * Created on 2007年4月6日, 上午12:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author rehte
 */
public class PluginHook extends TimerTask{
    static{
        install();
    }
    private static final int DAEMON_INTERVAL=5000;
    private static boolean ENABLED;
    private static File lock;
    private static MouseListenerTrigger trigger;
    private static Timer timer;
    public static MouseListenerTrigger getTrigger(){
        if(trigger==null)
            trigger=new MouseListenerTrigger();
        return trigger;
    }
    private static File getLock(){
        if(lock==null){
            String home=System.getProperty("java.home");
            File file=new File(home);
            lock=new File(new File(file,"lib"), "plugin.flag");
        }
        return lock;
    }
    private static boolean isEnabled(){
        return getLock().exists();
    }
    private static void setEnabled(boolean enabled){
        if(ENABLED==enabled)
            return;
        if(ENABLED){
            DictPopupManager.hide();
            Toolkit.getDefaultToolkit().removeAWTEventListener(getTrigger());
            ENABLED=false;
        }else{
            Toolkit.getDefaultToolkit()
            .addAWTEventListener(getTrigger(),
                    AWTEvent.MOUSE_MOTION_EVENT_MASK
                    | AWTEvent.MOUSE_EVENT_MASK);
            ENABLED=true;
        }
    }
    private static void refresh(){
        setEnabled(isEnabled());
    }
    public static void install(){
        refresh();
        timer=new Timer(true);
        timer.schedule(new PluginHook(), DAEMON_INTERVAL, DAEMON_INTERVAL);
    }
    public void run() {
        refresh();
    }
}
