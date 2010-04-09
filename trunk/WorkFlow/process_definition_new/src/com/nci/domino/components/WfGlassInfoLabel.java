package com.nci.domino.components;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.jidesoft.swing.JideLabel;

public class WfGlassInfoLabel extends JideLabel implements ActionListener {

	private static final long serialVersionUID = -2094242412349580414L;
	// 常数定义
	private static final int ANIMATION_FRAMES = 80;
	private static final int ANIMATION_INTERVAL = 30;
	// 帧索引
	private int frameIndex;
	// 时钟
	private Timer timer;

	public WfGlassInfoLabel() {

	}

	public void setGlassText(String text) {
		if (timer != null && timer.isRunning()) {
			timer.stop();
		}
		setText(text);
	}

	public void paint(Graphics g) {
		if (isAnimating()) {
			float alpha = 0;
			// 根据当前帧显示当前透明度的内容组件
			if (frameIndex > ANIMATION_FRAMES / 2f) {
				timer.setDelay(ANIMATION_INTERVAL);
				alpha = (float) (ANIMATION_FRAMES - frameIndex)
						/ (float) (ANIMATION_FRAMES / 2f);
			} else {
				alpha = (float) frameIndex / (float) (ANIMATION_FRAMES / 2f);
				if (alpha == 1) {
					timer.setDelay(2000);//最亮时延长显示2秒
					alpha = 0.999f; // 为了防止在alpha=1时的瞬时闪烁
				}
			}
			Graphics2D g2d = (Graphics2D) g;

			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha));
			// Renderer渲染机制

			super.paint(g2d);
		} else {
			// 如果是第一次，启动动画时钟
			frameIndex = 0;
			if (timer == null) {
				timer = new Timer(ANIMATION_INTERVAL, this);
			}
			timer.start();
		}
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
			setText("");
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
