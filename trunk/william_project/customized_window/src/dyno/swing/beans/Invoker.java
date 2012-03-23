/*
 * Invoker.java
 *
 * Created on 2007年6月14日, 下午5:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dyno.swing.beans;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * 一个简单的窗口动画显示工具
 * @author William Chen
 */
public class Invoker implements ActionListener{
    private static final int ANIMATION_INTERVAL=10;
    private static final int ANIMATION_FRAMES=10;
    /**
     * 调用此静态方法显示窗口，可以出现动画效果
     * 被显示的窗口是任何继承自Window的窗口，包括显示模态的对话框
     */
    public static void show(Window w){
        if(w.isVisible())
            return;
        new Invoker(w).invoke();
    }
    //要被显示的窗口
    private Window window;
    //窗口全部展开时大小
    private Dimension full_size;
    //动画定时器
    private Timer timer;
    //当前动画帧
    private int frameIndex;
    //私有构造函数，不允许直接访问
    private Invoker(Window w){
        //初始化
        window=w;
        full_size=window.getSize();
        timer=new Timer(15, this);
        frameIndex=0;
        window.setSize(0, 0);
    }
    //激活动画过程
    private void invoke(){
        if(!window.isVisible()){
            timer.start();
            window.setVisible(true);
        }
    }
    //动画的一帧处理方法
    public void actionPerformed(ActionEvent e) {
        //计算和设置当前帧尺寸
        int w=full_size.width*frameIndex/ANIMATION_FRAMES;
        int h=full_size.height*frameIndex/ANIMATION_FRAMES;
        window.setSize(w, h);
        if(frameIndex==ANIMATION_FRAMES){
            //最后一帧，停止时钟，释放资源
            timer.stop();
            timer=null;
            window=null;
            full_size=null;
        }else
            //前进一帧
            frameIndex++;
    }
}
