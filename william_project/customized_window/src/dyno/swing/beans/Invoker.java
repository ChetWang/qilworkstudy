/*
 * Invoker.java
 *
 * Created on 2007��6��14��, ����5:29
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
 * һ���򵥵Ĵ��ڶ�����ʾ����
 * @author William Chen
 */
public class Invoker implements ActionListener{
    private static final int ANIMATION_INTERVAL=10;
    private static final int ANIMATION_FRAMES=10;
    /**
     * ���ô˾�̬������ʾ���ڣ����Գ��ֶ���Ч��
     * ����ʾ�Ĵ������κμ̳���Window�Ĵ��ڣ�������ʾģ̬�ĶԻ���
     */
    public static void show(Window w){
        if(w.isVisible())
            return;
        new Invoker(w).invoke();
    }
    //Ҫ����ʾ�Ĵ���
    private Window window;
    //����ȫ��չ��ʱ��С
    private Dimension full_size;
    //������ʱ��
    private Timer timer;
    //��ǰ����֡
    private int frameIndex;
    //˽�й��캯����������ֱ�ӷ���
    private Invoker(Window w){
        //��ʼ��
        window=w;
        full_size=window.getSize();
        timer=new Timer(15, this);
        frameIndex=0;
        window.setSize(0, 0);
    }
    //���������
    private void invoke(){
        if(!window.isVisible()){
            timer.start();
            window.setVisible(true);
        }
    }
    //������һ֡��������
    public void actionPerformed(ActionEvent e) {
        //��������õ�ǰ֡�ߴ�
        int w=full_size.width*frameIndex/ANIMATION_FRAMES;
        int h=full_size.height*frameIndex/ANIMATION_FRAMES;
        window.setSize(w, h);
        if(frameIndex==ANIMATION_FRAMES){
            //���һ֡��ֹͣʱ�ӣ��ͷ���Դ
            timer.stop();
            timer=null;
            window=null;
            full_size=null;
        }else
            //ǰ��һ֡
            frameIndex++;
    }
}