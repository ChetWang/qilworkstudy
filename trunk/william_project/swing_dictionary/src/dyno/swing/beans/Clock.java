/*
 * Clock.java
 *
 * Created on 2007年4月5日, 下午11:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;

/**
 *
 * @author rehte
 */
public class Clock implements Runnable{
    private static Executor executor=Executors.newCachedThreadPool();
    private static final long DEFAULT_DELAY=250;
    private long threshold=DEFAULT_DELAY;
    private ActionListener listener;
    private long start;
    private boolean stopped;
    /** Creates a new instance of Clock */
    public Clock(ActionListener l) {
        listener=l;
    }
    public void start(){
        executor.execute(this);
    }
    public void setStart(long s){
        start=s;
    }
    public void run(){
        while(!stopped){
            try{
                Thread.sleep(threshold);
            }catch(InterruptedException e){}
            long now=System.currentTimeMillis();
            if(now-start>=threshold){
                if(!stopped){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run() {
                            listener.actionPerformed(null);
                        }
                    });
                }
                break;
            }else if(stopped){
                break;
            }
        }
        stopped=true;
    }
    public boolean isStopped(){
        return stopped;
    }
    public void setStopped(boolean s){
        stopped=s;
    }
}
