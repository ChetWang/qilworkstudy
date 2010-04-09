package com.nci.domino.components;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 淡入式显示内容组件的容器
 * 
 * @author Qil.Wong
 */
public class GlassPanel extends JPanel implements ActionListener {
	// 常数定义
	private static final int ANIMATION_FRAMES = 30;
	private static final int ANIMATION_INTERVAL = 30;
	// 帧索引
	private int frameIndex;
	// 时钟
	private Timer timer;

	public GlassPanel() {
	}

	public void paint(Graphics g) {
//		if (isAnimating()) {
//			// 根据当前帧显示当前透明度的内容组件
//			float alpha = (float) frameIndex / ANIMATION_FRAMES;
//			Graphics2D g2d = (Graphics2D) g;
//			g2d.setComposite(AlphaComposite.getInstance(
//					AlphaComposite.SRC_OVER, alpha));
//			// Renderer渲染机制
//			super.paint(g2d);
//		} else {
//			// 如果是第一次，启动动画时钟
//			frameIndex = 0;
//			timer = new Timer(ANIMATION_INTERVAL, this);
//			timer.start();
//		}
		 super.paint(g);
	}

	// 判断当前是否正在进行动画
	private boolean isAnimating() {
		return timer != null && timer.isRunning();
	}

	// 关闭时钟，重新初始化
	private void closeTimer() {
		if (isAnimating()) {
			timer.stop();
			frameIndex = 0;
			timer = null;
		}
	}

	// 动画时钟处理事件
	public void actionPerformed(ActionEvent e) {
		// 前进一帧
		frameIndex++;
		if (frameIndex > ANIMATION_FRAMES)
			// 最后一帧，关闭动画
			closeTimer();
		else
			// 更新当前一帧
			repaint();
	}
}
