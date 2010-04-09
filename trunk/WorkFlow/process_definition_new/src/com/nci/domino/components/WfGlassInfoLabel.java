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
	// ��������
	private static final int ANIMATION_FRAMES = 80;
	private static final int ANIMATION_INTERVAL = 30;
	// ֡����
	private int frameIndex;
	// ʱ��
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
			// ���ݵ�ǰ֡��ʾ��ǰ͸���ȵ��������
			if (frameIndex > ANIMATION_FRAMES / 2f) {
				timer.setDelay(ANIMATION_INTERVAL);
				alpha = (float) (ANIMATION_FRAMES - frameIndex)
						/ (float) (ANIMATION_FRAMES / 2f);
			} else {
				alpha = (float) frameIndex / (float) (ANIMATION_FRAMES / 2f);
				if (alpha == 1) {
					timer.setDelay(2000);//����ʱ�ӳ���ʾ2��
					alpha = 0.999f; // Ϊ�˷�ֹ��alpha=1ʱ��˲ʱ��˸
				}
			}
			Graphics2D g2d = (Graphics2D) g;

			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha));
			// Renderer��Ⱦ����

			super.paint(g2d);
		} else {
			// ����ǵ�һ�Σ���������ʱ��
			frameIndex = 0;
			if (timer == null) {
				timer = new Timer(ANIMATION_INTERVAL, this);
			}
			timer.start();
		}
	}

	// �жϵ�ǰ�Ƿ����ڽ��ж���
	private boolean isAnimating() {
		return timer != null && timer.isRunning();
	}

	// �ر�ʱ�ӣ����³�ʼ��
	private void closeTimer() {
		if (isAnimating()) {
			timer.stop();
			frameIndex = 0;
			setText("");
		}
	}

	// ����ʱ�Ӵ����¼�
	public void actionPerformed(ActionEvent e) {
		// ǰ��һ֡
		frameIndex++;
		if (frameIndex > ANIMATION_FRAMES)
			// ���һ֡���رն���
			closeTimer();
		else
			// ���µ�ǰһ֡
			repaint();
	}
}
