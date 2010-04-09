package com.nci.domino.components.tree;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jidesoft.swing.InfiniteProgressPanel;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;
import com.nci.domino.components.WfOverlayable;

/**
 * �������ڷ�æʱ����ʾ�Եĸ������,�и�������æ��ʾ���ı���Ϣ
 * 
 * @author Qil.Wong
 * 
 */
public class WfBusyTextOverlayable extends WfOverlayable {

	private static final long serialVersionUID = 3294570839840797384L;
	/**
	 * ���������ǵĶ���ͼƬ
	 */
	private InfiniteProgressPanel progressPanel;

	/**
	 * ���������ǵ����ֱ�ǩ
	 */
	private StyledLabel infoLabel;

	private String busyText;

	public WfBusyTextOverlayable(JComponent c, String busyText) {
		super(c);
		progressPanel = new InfiniteProgressPanel() {

			private static final long serialVersionUID = 8420090103660722714L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(20, 20);
			}
		};
		infoLabel = StyledLabelBuilder.createStyledLabel(busyText);
		infoLabel.setOpaque(false);
		this.busyText = busyText;
		FlowLayout layout = new FlowLayout();
		JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setLayout(layout);
		infoPanel.add(progressPanel);
		infoPanel.add(infoLabel);
		addOverlayComponent(infoPanel);
	}

	@Override
	public void setOverlayVisible(boolean flag) {
		super.setOverlayVisible(flag);
		// �������Ҫ���¼���һ�飬�����޷���ת
		progressPanel.setVisible(flag);
	}

	public void setOpaque(boolean flag) {
		super.setOpaque(flag);
		if (infoLabel != null)
			infoLabel.setOpaque(flag);
	}

	public String getBusyText() {
		return busyText;
	}

	public void setBusyText(String busyText) {
		this.busyText = busyText;
		StyledLabelBuilder.setStyledText(infoLabel, busyText);
	}

	public InfiniteProgressPanel getProgressPanel() {
		return progressPanel;
	}

}
