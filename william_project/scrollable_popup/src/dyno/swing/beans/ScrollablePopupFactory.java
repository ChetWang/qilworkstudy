/*
 * ScrollablePopupFactory.java
 *
 * Created on June 12, 2007, 9:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * ����ʽ�������ڹ�����
 * �޸���SKY�����bug:
 * ��������һ��,��һ������л�������.��JDesktop��JInternalFrame�Ҽ������˵���,
 * ��������߽糬����JInternalFrame�ı߽���ʾ��JDesktop��,���Կ�������Ҳ���Ȼ���������,
 * ���ڸ��������γɶ���Ч����.�����˵�������������ֱ�ӳ���,Ҫ����͸���͸����ˡ�
 *
 * @author William Chen
 * @version 1.1
 */
public class ScrollablePopupFactory extends PopupFactory{
    //�Ƿ���������ȱʡ��
    private boolean horizontalExtending;
    //�Ƿ�ֱ������ȱʡ��
    private boolean verticalExtending;
    /**
     * Creates a new instance of ScrollablePopupFactory
     */
    public ScrollablePopupFactory(){
    }
    /**
     * @param h �Ƿ�������
     * @param v �Ƿ�ֱ����
     */
    public ScrollablePopupFactory(boolean h, boolean v) {
        horizontalExtending=h;
        verticalExtending=v;
    }
    /**
     * ����PopupFactory.getPopup�����ṩ�Զ���Popup����
     *
     */
    @Override
    public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
        //����ݺᶼ��������ֱ��ʹ��ȱʡ�ĵ���ʽ����
        if(!(horizontalExtending||verticalExtending))
            return super.getPopup(owner, contents, x, y);
        
        Dimension preferedSize=contents.getPreferredSize();
        //���ô��ڵĳ�ʼ���Ż��ߴ磬ע��Popup��������ߴ����������ڴ�С��
        //Ϊ�����һ֡���ھ�ȫ����ʾ����Ҫ����һ��preferred size
        contents.setPreferredSize(new Dimension(
                horizontalExtending?0:preferedSize.width,
                verticalExtending?0:preferedSize.height));
        //��ȡȱʡ��Popup
        Popup popup = super.getPopup(owner, contents, x, y);
        //Ŀǰû�кð취�жϵ��������Ǻ������ͣ�ֻ�������������ж�
        String name=popup.getClass().getName();
        if(name.equals("javax.swing.PopupFactory$HeavyWeightPopup")){
            //�������ĵ������ڣ��䶥������ΪJWindow
            return new PopupProxy(
                    popup, 
                    contents,
                    SwingUtilities.getWindowAncestor(contents), 
                    preferedSize);
        }else{
            //�������ĵ�������
            if(contents instanceof JToolTip)
                //��������JToolTip�����丸���������Ƕ�������
                return new PopupProxy(
                        popup,
                        contents,
                        contents.getParent(), 
                        preferedSize);
            else
                //��������ʽ���������������Ƕ�������
                return new PopupProxy(
                        popup,
                        contents,
                        contents, 
                        preferedSize);
        }
    }
    /**
     * �������һ��Popup��������ʵPopup����ʾ���̶�������
     */
    class PopupProxy extends Popup implements ActionListener{
        //һЩ����
        private static final int ANIMATION_FRAME_INTERVAL=10;
        private static final int ANIMATION_FRAMES=10;
        //������ĵ���ʽ���ڣ��������ʽ�����Ǵ�ȱʡ�����Ƕ���õġ�
        private Popup popupProxy;
        //��ǰ�������ڵĶ�������
        private Component popupContainer;
        //��ǰ�������ڵ��������
        private Component popupContent;
        //����ʽ�������ճߴ�
        private Dimension fullSize;
        //����ʱ��
        private Timer timer;
        //�����ĵ�ǰ֡
        private int frameIndex;
        
        public PopupProxy(Popup popup, Component content, Component container, Dimension size){
            //��ʼ��
            popupProxy=popup;
            popupContent=content;
            popupContainer=container;
            fullSize=size;
        }
        /**
         * ����show�������������߳�
         */
        @Override
        public void show() {
            //��������ʾ
            popupProxy.show();
            //�ָ�ԭʼpreferred size
            popupContent.setPreferredSize(null);
            //��ʼ��Ϊ��һ֡
            frameIndex=1;
            //��������ʱ��
            timer=new Timer(ANIMATION_FRAME_INTERVAL, this);
            timer.start();
        }
        /**
         * ����hide���رտ��ܵ�ʱ��
         */
        @Override
        public void hide() {
            if(timer!=null&&timer.isRunning())
                closeTimer();
            //���������ڹر�
            popupProxy.hide();
        }
        //����ʱ���¼��Ĵ�������һ֡
        public void actionPerformed(ActionEvent e) {
            //���õ�ǰ֡������������ĳߴ�
            popupContainer.setSize(
                    horizontalExtending?
                        fullSize.width*frameIndex/ANIMATION_FRAMES:
                        fullSize.width,
                    verticalExtending?
                        fullSize.height*frameIndex/ANIMATION_FRAMES:
                        fullSize.height);
            
            if(frameIndex==ANIMATION_FRAMES)
                //���һ֡
                closeTimer();
            else
                //ǰ��һ֡
                frameIndex++;
        }
        private void closeTimer(){
            //�ر�ʱ��
            timer.stop();
            timer=null;
        }
    }
}
