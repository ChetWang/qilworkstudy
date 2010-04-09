package com.nci.domino.components;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * ����ʽ��ʾ�������������
 * 
 * @author Qil.Wong
 */
public class GlassPanel extends JPanel implements ActionListener {
	// ��������
	private static final int ANIMATION_FRAMES = 30;
	private static final int ANIMATION_INTERVAL = 30;
	// ֡����
	private int frameIndex;
	// ʱ��
	private Timer timer;

	public GlassPanel() {
	}

	public void paint(Graphics g) {
//		if (isAnimating()) {
//			// ���ݵ�ǰ֡��ʾ��ǰ͸���ȵ��������
//			float alpha = (float) frameIndex / ANIMATION_FRAMES;
//			Graphics2D g2d = (Graphics2D) g;
//			g2d.setComposite(AlphaComposite.getInstance(
//					AlphaComposite.SRC_OVER, alpha));
//			// Renderer��Ⱦ����
//			super.paint(g2d);
//		} else {
//			// ����ǵ�һ�Σ���������ʱ��
//			frameIndex = 0;
//			timer = new Timer(ANIMATION_INTERVAL, this);
//			timer.start();
//		}
		 super.paint(g);
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
			timer = null;
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
