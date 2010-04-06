package com.nci.svg.sdk.ui.graphunit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.ui.EditorPanel;
import com.nci.svg.sdk.ui.GradientPanel;


public class NCISymbolPanel extends EditorPanel {

	private SymbolTypeBean symbolTypeBean;
	private int flow_xcap = 5;
	private int flow_ycap = 5;
	private int prefferedColumns = 3;
	private JPanel scrollPanel;
	private boolean hasInitiated = false;
	/**
	 * �Ƿ������һ��panel�����һ��panel����ʾͼԪ���������ڵ��ʱ����ʾ
	 */
	private boolean isLastPanel;

	public NCISymbolPanel(EditorAdapter editor) {
		super(editor);
	}

	public NCISymbolPanel(SymbolTypeBean symbolTypeBean, EditorAdapter editor,
			boolean isLastPanel) {
		super(editor);
		this.symbolTypeBean = symbolTypeBean;
		this.isLastPanel = isLastPanel;
		init();
	}

	/**
	 * ��ʼ��NCIGraphUnitPanel���
	 */
	public void init() {
		this.setLayout(new BorderLayout());
//		scrollPanel = new NCIBoxPane();
		scrollPanel = new GradientPanel();
//		scrollPanel.setBackground(Color.blue);
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
	 * ����������ͼԪ���
	 */
	public void initGraph() {
		if(hasInitiated)
			return;
		ArrayList<NCIThumbnailPanel> thumbnails = editor.getSymbolManager()
				.createThumnailList(symbolTypeBean,
						NCIThumbnailPanel.THUMBNAIL_OUTLOOK);
		LinkedHashMap<String, NCIThumbnailPanel> oneTypeThumbMap = new LinkedHashMap<String, NCIThumbnailPanel>(
				(int) (thumbnails.size() * 1.5));
		for (NCIThumbnailPanel thumbnail : thumbnails) {
			scrollPanel.add(thumbnail);
			oneTypeThumbMap.put(thumbnail.getText(), thumbnail);
		}
		//FIXME ����Ҫ����ͼԪ��ģ���������,
		//wangql fixed once @2008.12.12 15:03
		editor.getSymbolManager().getThumbnailShownMap().put(
				symbolTypeBean,
				oneTypeThumbMap);

		/** ��ʹJScrollPane��ʵ�ִ�ֱ������scroolPanel�ĳ�����Ҫ�ϸ��壬�����޷�������ʾ* */
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

	public JPanel getScrollPanel() {
		return scrollPanel;
	}
	
	public boolean isInitialized(){
		return hasInitiated;
	}
	
	/**
	 * ��ȡ���Ͷ���������ͼԪ����ģ����������Լ������־��������
	 * @return
	 */
	public SymbolTypeBean getSymbolTypeBean(){
		return symbolTypeBean;
	}
}
	

