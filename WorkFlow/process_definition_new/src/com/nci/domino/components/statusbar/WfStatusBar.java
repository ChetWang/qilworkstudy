/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WfStatusBar.java
 *
 * Created on 2009-11-23, 16:04:08
 */
package com.nci.domino.components.statusbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideLabel;
import com.jidesoft.swing.JideSwingUtilities;
import com.nci.domino.WfEditor;
import com.nci.domino.components.WfGlassInfoLabel;
import com.nci.domino.help.Functions;

/**
 * ������״̬��
 * 
 * @author Qil.Wong
 */
public class WfStatusBar extends JPanel {

	private static final long serialVersionUID = -7869437351050682627L;
	// ������
	private JProgressBar progressBar;

	private WfEditor editor;
	// ��Ϣ��ʾ��ʾ��ǩ
	private JideLabel busyInfoLabel;
	// �������հ�ť
	private JideButton gcBtn;

	private JideButton helpBtn;

	private JideButton reshowBtn;

	private JideButton extBtn;

	private WfGlassInfoLabel simpleActionInfoLabel;

	private String lastInfo = "";

	private Timer showInfoTimer;

	private List<String> infos = new ArrayList<String>();

	private Object lock = new Object();

	/** Creates new form WfStatusBar */
	public WfStatusBar(WfEditor editor) {
		this.editor = editor;
		initComponents();
		showInfoTimer = new Timer(1000, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showInfos();

			}
		});
	}

	/**
	 * ��ȡ�༭��
	 * 
	 * @return the editor
	 */
	public WfEditor getEditor() {
		return editor;
	}

	/**
	 * ��ʼ��״̬��
	 */
	private void initComponents() {
		progressBar = new WfProgressBar();
		progressBar.setPreferredSize(new Dimension(110, 16));
		busyInfoLabel = new JideLabel();
		busyInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		simpleActionInfoLabel = new WfGlassInfoLabel();
		simpleActionInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5,
				0, 5));
		gcBtn = new JideButton();

		gcBtn.setIcon(Functions.getImageIcon("gc.gif"));
		gcBtn.setToolTipText("��Դ����");
		gcBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.gc();
			}
		});
		gcBtn.requestFocus(false);
		helpBtn = new JideButton();
		helpBtn.requestFocus(false);
		helpBtn.setIcon(Functions.getImageIcon("help.gif"));
		helpBtn.setToolTipText("��ʾ����");
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		reshowBtn = new JideButton();
		reshowBtn.setIcon(Functions.getImageIcon("reshow_info.gif"));
		reshowBtn.setToolTipText("������һ��ʾ");
		reshowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showGlassInfo(lastInfo);
			}
		});
		extBtn = new JideButton();
		extBtn.setIcon(Functions.getImageIcon("externalize.gif"));
		extBtn.setToolTipText("��������ⲿ�򿪱༭");
		extBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.openJFrameOnApplet();
			}
		});
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints constrains = new GridBagConstraints();
		constrains.gridx = 0;
		constrains.gridy = 0;
		constrains.anchor = GridBagConstraints.WEST;
		add(helpBtn, constrains);

		constrains.gridx = 1;
		add(reshowBtn, constrains);
		constrains.gridx = 2;
		add(extBtn, constrains);
		constrains.gridx = 3;
		add(simpleActionInfoLabel, constrains);
		JPanel glue = new JPanel();
		glue.setOpaque(false);
		constrains.gridx = 4;
		constrains.anchor = GridBagConstraints.CENTER;
		constrains.weightx = 1;
		add(glue, constrains);

		constrains.weightx = 0;
		constrains.anchor = GridBagConstraints.EAST;
		constrains.gridx = 5;
		add(busyInfoLabel, constrains);
		constrains.gridx = 6;
		add(progressBar, constrains);

		constrains.gridx = 7;
		add(gcBtn, constrains);
		if (editor.getClientProperty("appletOrFrame") instanceof JApplet == false) {
			extBtn.setVisible(false);
		}
	}

	/**
	 * ��״̬������ʾ������Ϣ��������������
	 * 
	 * @param text
	 *            ������Ϣ
	 */
	public void startShowBusyInfo(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (lock) {
					infos.add(text);
					if (!showInfoTimer.isRunning()) {
						progressBar.setIndeterminate(true);
						showInfoTimer.start();
					}
				}
			}
		});
	}

	/**
	 * ֹͣ������Ϣ��ʾ����ȡ������������
	 */
	public void stopShowInfo(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (lock) {
					infos.remove(text);
					if (infos.size() == 0) {
						busyInfoLabel.setText("");
						progressBar.setIndeterminate(false);
						showInfoTimer.stop();
					}
				}
			}
		});

	}

	private void showInfos() {
		synchronized (lock) {
			if (infos.size() == 0) {
				busyInfoLabel.setText("");
				progressBar.setIndeterminate(false);
				this.showInfoTimer.stop();
			} else {
				String s = busyInfoLabel.getText();
				int index = infos.indexOf(s);
				if (index + 1 < infos.size()) {
					busyInfoLabel.setText(infos.get(index + 1));
				} else {
					busyInfoLabel.setText(infos.get(0));
				}
			}
		}
	}

	/**
	 * ��ʾ�����������ı���һ��ʱ�����Զ���ʧ
	 * 
	 * @param text
	 *            �����������ı�
	 */
	public void showGlassInfo(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				simpleActionInfoLabel.setGlassText(text);
				lastInfo = text;
			}
		});
	}

	public void paintComponent(Graphics g) {
		Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
		JideSwingUtilities.fillGradient((Graphics2D) g, rect, new Color(222,
				222, 222), Color.lightGray, false);

	}

	private class WfProgressBar extends JProgressBar {

		private static final long serialVersionUID = 5967883581256092077L;

		public void paintComponent(Graphics g) {
			// if (!isOpaque()) {
			// super.paintComponent(g);
			// return;
			// }
			// ���ཥ�����ƿ��Խ��汳��
			Graphics2D g2d = (Graphics2D) g;
			int w = getWidth();
			int h = getHeight();

			// ���ƽ���������wΪ0ʱ���������£�hΪ0ʱ����������
			GradientPaint gp = new GradientPaint(0, 0,
					new Color(220, 220, 220), w, h, new Color(200, 210, 220));

			g2d.setPaint(gp);
			g2d.fillRect(0, 0, w, h);

			setOpaque(false);
			super.paintComponent(g);
			setOpaque(true);
		}
	}

	public JideButton getExtBtn() {
		return extBtn;
	}
}
