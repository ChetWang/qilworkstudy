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
 * 滚动式弹出窗口工厂类
 * 修改了SKY提出的bug:
 * “试验了一下,在一般面板中还算正常.在JDesktop的JInternalFrame右键弹出菜单中,
 * 如果弹出边界超过该JInternalFrame的边界显示到JDesktop中,可以看出绘制也是先画顶层容器,
 * 再在该容器上形成动画效果的.弹出菜单后面区域仍是直接出来,要是能透明就更好了”
 *
 * @author William Chen
 * @version 1.1
 */
public class ScrollablePopupFactory extends PopupFactory{
    //是否横向滚动，缺省不
    private boolean horizontalExtending;
    //是否垂直滚动，缺省不
    private boolean verticalExtending;
    /**
     * Creates a new instance of ScrollablePopupFactory
     */
    public ScrollablePopupFactory(){
    }
    /**
     * @param h 是否横向滚动
     * @param v 是否垂直滚动
     */
    public ScrollablePopupFactory(boolean h, boolean v) {
        horizontalExtending=h;
        verticalExtending=v;
    }
    /**
     * 覆盖PopupFactory.getPopup方法提供自定义Popup代理
     *
     */
    @Override
    public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
        //如果纵横都不滚动，直接使用缺省的弹出式窗口
        if(!(horizontalExtending||verticalExtending))
            return super.getPopup(owner, contents, x, y);
        
        Dimension preferedSize=contents.getPreferredSize();
        //设置窗口的初始最优化尺寸，注意Popup根据这个尺寸决定最初窗口大小，
        //为避免第一帧窗口就全部显示，需要重置一下preferred size
        contents.setPreferredSize(new Dimension(
                horizontalExtending?0:preferedSize.width,
                verticalExtending?0:preferedSize.height));
        //获取缺省的Popup
        Popup popup = super.getPopup(owner, contents, x, y);
        //目前没有好办法判断弹出窗口是何种类型，只好用类名字来判断
        String name=popup.getClass().getName();
        if(name.equals("javax.swing.PopupFactory$HeavyWeightPopup")){
            //重量级的弹出窗口，其顶层容器为JWindow
            return new PopupProxy(
                    popup, 
                    contents,
                    SwingUtilities.getWindowAncestor(contents), 
                    preferedSize);
        }else{
            //轻量级的弹出窗口
            if(contents instanceof JToolTip)
                //如果组件是JToolTip，则其父亲容器就是顶层容器
                return new PopupProxy(
                        popup,
                        contents,
                        contents.getParent(), 
                        preferedSize);
            else
                //其他弹出式窗口则组件本身就是顶层容器
                return new PopupProxy(
                        popup,
                        contents,
                        contents, 
                        preferedSize);
        }
    }
    /**
     * 这个类是一个Popup代理，将真实Popup的显示过程动画弹出
     */
    class PopupProxy extends Popup implements ActionListener{
        //一些常量
        private static final int ANIMATION_FRAME_INTERVAL=10;
        private static final int ANIMATION_FRAMES=10;
        //被代理的弹出式窗口，这个弹出式窗口是从缺省工厂那儿获得的。
        private Popup popupProxy;
        //当前弹出窗口的顶层容器
        private Component popupContainer;
        //当前弹出窗口的内容组件
        private Component popupContent;
        //弹出式窗口最终尺寸
        private Dimension fullSize;
        //动画时钟
        private Timer timer;
        //动画的当前帧
        private int frameIndex;
        
        public PopupProxy(Popup popup, Component content, Component container, Dimension size){
            //初始化
            popupProxy=popup;
            popupContent=content;
            popupContainer=container;
            fullSize=size;
        }
        /**
         * 覆盖show方法启动动画线程
         */
        @Override
        public void show() {
            //代理窗口显示
            popupProxy.show();
            //恢复原始preferred size
            popupContent.setPreferredSize(null);
            //初始化为第一帧
            frameIndex=1;
            //启动动画时钟
            timer=new Timer(ANIMATION_FRAME_INTERVAL, this);
            timer.start();
        }
        /**
         * 重载hide，关闭可能的时钟
         */
        @Override
        public void hide() {
            if(timer!=null&&timer.isRunning())
                closeTimer();
            //代理弹出窗口关闭
            popupProxy.hide();
        }
        //动画时钟事件的处理，其中一帧
        public void actionPerformed(ActionEvent e) {
            //设置当前帧弹出窗口组件的尺寸
            popupContainer.setSize(
                    horizontalExtending?
                        fullSize.width*frameIndex/ANIMATION_FRAMES:
                        fullSize.width,
                    verticalExtending?
                        fullSize.height*frameIndex/ANIMATION_FRAMES:
                        fullSize.height);
            
            if(frameIndex==ANIMATION_FRAMES)
                //最后一帧
                closeTimer();
            else
                //前进一帧
                frameIndex++;
        }
        private void closeTimer(){
            //关闭时钟
            timer.stop();
            timer=null;
        }
    }
}
