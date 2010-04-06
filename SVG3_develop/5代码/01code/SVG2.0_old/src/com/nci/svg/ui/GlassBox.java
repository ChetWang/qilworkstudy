package com.nci.svg.ui;

import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.event.ActionEvent;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
/**
 * ���뵭��ʽ��ʾ�������������
 * @author William Chen
 */
public class GlassBox extends JComponent implements ActionListener{
    //��������
    private static final int ANIMATION_FRAMES=50;
    private static final int ANIMATION_INTERVAL=10;
    //֡����
    private int frameIndex;
    //ʱ��
    private Timer timer;

    /** Creates a new instance of GlassBox */
    public GlassBox() {
    }

    public void paint(Graphics g){
        if(isAnimating()){
            //���ݵ�ǰ֡��ʾ��ǰ͸���ȵ��������
            float alpha=(float)frameIndex/(float)ANIMATION_FRAMES;
            Graphics2D g2d=(Graphics2D)g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            //Renderer��Ⱦ����
            super.paint(g2d);
        }else{
            //����ǵ�һ�Σ���������ʱ��
            frameIndex=0;
            timer=new Timer(ANIMATION_INTERVAL, this);
        }
    }
    //�жϵ�ǰ�Ƿ����ڽ��ж���
    private boolean isAnimating(){
        return timer!=null && timer.isRunning();
    }
    //�ر�ʱ�ӣ����³�ʼ��
    private void closeTimer() {
        if(isAnimating()){
            timer.stop();
            frameIndex=0;
            timer=null;
        }
    }
    //����ʱ�Ӵ����¼�
    public void actionPerformed(ActionEvent e) {
        //ǰ��һ֡
        frameIndex++;
        if(frameIndex>=ANIMATION_FRAMES)
            //���һ֡���رն���
            closeTimer();
        else//���µ�ǰһ֡
            repaint();
    }
}
