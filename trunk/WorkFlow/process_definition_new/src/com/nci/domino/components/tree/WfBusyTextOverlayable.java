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
 * 流程树在繁忙时的提示性的覆盖组件,有个动画繁忙提示和文本信息
 * 
 * @author Qil.Wong
 * 
 */
public class WfBusyTextOverlayable extends WfOverlayable {

	private static final long serialVersionUID = 3294570839840797384L;
	/**
	 * 流程树覆盖的动画图片
	 */
	private InfiniteProgressPanel progressPanel;

	/**
	 * 流程树覆盖的文字标签
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
		// 这里必须要重新加载一遍，否则无法旋转
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
