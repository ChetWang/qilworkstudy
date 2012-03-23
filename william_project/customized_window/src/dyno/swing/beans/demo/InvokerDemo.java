/*
 * InvokerDemo.java
 *
 * Created on 2007年6月14日, 下午6:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans.demo;

import dyno.swing.beans.Invoker;
import java.awt.EventQueue;
import javax.swing.UIManager;

/**
 *
 * @author William Chen
 */
public class InvokerDemo {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                //使用Invoke.show方法动画显示窗口
                Invoker.show(new SampleFrame());
            }
        });
    }
}
