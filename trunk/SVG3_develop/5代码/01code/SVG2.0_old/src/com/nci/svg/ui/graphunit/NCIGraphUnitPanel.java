package com.nci.svg.ui.graphunit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.nci.svg.graphunit.NCIGraphUnitTypeBean;
import com.nci.svg.ui.EditorPanel;

import fr.itris.glips.svgeditor.Editor;

public class NCIGraphUnitPanel extends EditorPanel {

	private NCIGraphUnitTypeBean equipSymbol;
	private int flow_xcap = 5;
	private int flow_ycap = 5;
	private int prefferedColumns = 3;
	private JPanel scrollPanel;
	private boolean hasInitiated = false;
	/**
	 * 是否是最后一个panel，最后一个panel才显示图元，其他的在点击时才显示
	 */
	private boolean isLastPanel;

	public NCIGraphUnitPanel(Editor editor) {
		super(editor);
	}

	public NCIGraphUnitPanel(NCIGraphUnitTypeBean equipSymbol, Editor editor,
			boolean isLastPanel) {
		super(editor);
		this.equipSymbol = equipSymbol;
		this.isLastPanel = isLastPanel;
		init();
	}

	/**
	 * 初始化NCIGraphUnitPanel面板
	 */
	public void init() {
		this.setLayout(new BorderLayout());
//		scrollPanel = new NCIBoxPane();
		scrollPanel = new JPanel();
//		scrollPanel.setPaintGradient(false);
		// scrollPanel.setXorColor(new java.awt.Color(204, 129, 217));
		JScrollPane scroll = new JScrollPane();
		// scrollPanel.setBackground(Color.white);

		this.add(scroll, BorderLayout.CENTER);
		scrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT, flow_xcap,
				flow_ycap));
		scroll.setViewportView(scrollPanel);
		scroll.setAutoscrolls(true);
		if (isLastPanel) {
			initGraph();
		}

	}

	/**
	 * 往面板中添加图元组件
	 */
	public void initGraph() {
		if(hasInitiated)
			return;
		ArrayList<NCIThumbnailPanel> thumbnails = editor.getGraphUnitManager()
				.getThumnailList(equipSymbol,
						NCIThumbnailPanel.THUMBNAIL_OUTLOOK);
		LinkedHashMap<String, NCIThumbnailPanel> oneTypeThumbMap = new LinkedHashMap<String, NCIThumbnailPanel>(
				(int) (thumbnails.size() * 1.5));
		for (NCIThumbnailPanel thumbnail : thumbnails) {
			scrollPanel.add(thumbnail);
			oneTypeThumbMap.put(thumbnail.getText(), thumbnail);
		}
		int index = equipSymbol.getGraphUnitType().indexOf("=");
		editor.getThumnailsMap().put(
				equipSymbol.getGraphUnitType().substring(0, index),
				oneTypeThumbMap);

		/** 能使JScrollPane能实现垂直滚动；scroolPanel的长宽需要严格定义，否则无法完整显示* */
		scrollPanel
				.setPreferredSize(new Dimension(
						(NCIThumbnailPanel.outlookPrefferedSize.width + flow_xcap)
								* prefferedColumns,
						(int) thumbnails.size()
								/ prefferedColumns
								* (NCIThumbnailPanel.outlookPrefferedSize.height + flow_ycap)));
		hasInitiated = true;
		System.gc();
	}

	public NCIGraphUnitTypeBean getEquipSymbol() {
		return equipSymbol;
	}

	public void setEquipSymbol(NCIGraphUnitTypeBean equipSymbol) {
		this.equipSymbol = equipSymbol;
	}

	public JPanel getScrollPanel() {
		return scrollPanel;
	}
	
	public boolean isInitialized(){
		return hasInitiated;
	}
}
