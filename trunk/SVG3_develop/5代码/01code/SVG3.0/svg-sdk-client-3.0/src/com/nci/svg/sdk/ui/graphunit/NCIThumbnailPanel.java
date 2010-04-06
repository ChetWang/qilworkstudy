/*
 * NCIThumbnailPanel.java
 *
 * Created on April 5, 2008, 9:29 PM
 */
package com.nci.svg.sdk.ui.graphunit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.BevelBorder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.shape.GraphUnitImageShape;
import com.nci.svg.sdk.shape.vhpath.VHPathShape;
import com.nci.svg.sdk.ui.EditorPanel;

import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * 
 * @author Qil.Wong
 */
public class NCIThumbnailPanel extends EditorPanel {

	private Document doc;
	private String graphUnitID = "";

	private NCIEquipSymbolBean symbolBean;

	public static final int THUMBNAIL_COMBOBOX = 0;
	public static final int THUMBNAIL_OUTLOOK = 1;
	public static Dimension outlookPrefferedSize = new Dimension(48, 64);
	public static Dimension comboPrefferedSize = new Dimension(16, 16);

	/** Creates new form NCIThumbnailPanel */
	public NCIThumbnailPanel(EditorAdapter editor) {
		super(editor);
		initComponents();
		// 这个一定要加上，限制JSvgCanvas的大小，使用preferredsize不能严格控制
		svgCanvas.setSize(comboPrefferedSize);
		svgCanvas.setRecenterOnResize(true);
		svgCanvas.setOpaque(true);

	}

	public NCIThumbnailPanel(int type, EditorAdapter editor) {
		super(editor);
		switch (type) {
		case THUMBNAIL_COMBOBOX: {
			initComponents();
			svgCanvas.setSize(comboPrefferedSize);
			break;
		}
		case THUMBNAIL_OUTLOOK: {
			iniOutlookPanelComponent();
			break;
		}
		}
		// 这个一定要加上，限制JSvgCanvas的大小，使用preferredsize不能严格控制
		svgCanvas.setRecenterOnResize(true);
		svgCanvas.setBackground(Constants.THUMBNAIL_RELEASED_BACKGROUND);
	}

	/**
	 * 添加到左侧树OutlookPane时的初始化方法
	 */
	private void iniOutlookPanelComponent() {
		textLabel = new javax.swing.JLabel();
		textLabel.setBackground(Color.white);
		svgCanvas = new org.apache.batik.swing.JSVGCanvas();
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setLayout(new BorderLayout());
		this.add(svgCanvas, BorderLayout.CENTER);
		this.add(textLabel, BorderLayout.SOUTH);
		this.setPreferredSize(outlookPrefferedSize);
		addMouseListeners();
	}

	/**
	 * 为缩略图图元面板增加鼠标事件
	 */
	private void addMouseListeners() {
		MouseAdapter adapter = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				mouseAction(Constants.MOUSE_PRESSED, e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseAction(Constants.MOUSE_RELEASED, e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseAction(Constants.MOUSE_ENTERED, e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseAction(Constants.MOUSE_EXITED, e);
			}
		};
		svgCanvas.addMouseListener(adapter);
		textLabel.addMouseListener(adapter);
	}

	/**
	 * 鼠标事件的详细内容
	 * 
	 * @param mouseSatus
	 *            鼠标状态，参见Constants类
	 * @see com.nci.svg.sdk.client.util.Constants
	 */
	private void mouseAction(int mouseStatus, MouseEvent e) {
		if (mouseStatus == Constants.MOUSE_PRESSED
				|| mouseStatus == Constants.MOUSE_RELEASED) {
			editor.getSymbolManager().setMouseStatus(mouseStatus);
			if (mouseStatus == Constants.MOUSE_PRESSED) {
				editor.getSymbolSession().setSelectedThumbnail(this);
				Element root = this.getDocument().getDocumentElement();
				String type = root.getAttribute("connectLine");
				if ((type != null && type.equals("true"))
						|| editor.getSymbolSession().isDirectDrawPath(
								this.getText())) { // 连接线要特殊处理
					PathShape path = (PathShape) editor
							.getModuleByID(PathShape.MODULE_ID);
					path.enableConnectedPath(true);
					path.notifyDrawingMode();
				} else {
					type = root.getAttribute("vhconnect");// 只有水平垂直方向的连接线
					if (type != null && type.equals("true")) {
						VHPathShape path = (VHPathShape) editor
								.getModuleByID(VHPathShape.MODULE_ID);
						path.setCurrentAction("FoldLineShape");
						path.setModuleMode(0);
						path.enableConnectedPath(true);
						path.notifyDrawingMode();
					} else {
						GraphUnitImageShape guImageShape = (GraphUnitImageShape) editor
								.getModuleByID(GraphUnitImageShape.MODULE_ID);
						guImageShape.notifyDrawingMode();
					}
				}

			}

		}
		setCursorShape(mouseStatus);
	}

	/**
	 * 设置鼠标形状
	 * 
	 * @param mouseStatus
	 */
	private void setCursorShape(int mouseStatus) {
		switch (mouseStatus) {
		case Constants.MOUSE_PRESSED:
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			break;
		case Constants.MOUSE_RELEASED:
			this.setCursor(Cursor.getDefaultCursor());
			break;
		case Constants.MOUSE_ENTERED:
			if (editor.getSymbolManager().getMouseStatus() != Constants.MOUSE_PRESSED)
				this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			break;
		case Constants.MOUSE_EXITED:
			if (editor.getSymbolManager().getMouseStatus() != Constants.MOUSE_PRESSED)
				this.setCursor(Cursor.getDefaultCursor());
			break;
		}
	}

	/**
	 * 设置名称
	 * 
	 * @param text
	 */
	public void setText(String text) {
		textLabel.setText(text);
		svgCanvas.setToolTipText(text);
	}

	/**
	 * 设置用于显示的svg文件的URI
	 * 
	 * @param uri
	 */
	public void setURI(String uri) {
		svgCanvas.setURI(uri);

	}

	/**
	 * 设置用于显示的SVGDocument对象
	 * 
	 * @param doc
	 */
	public void setDocument(Document doc) {
		this.doc = doc;
		svgCanvas.setDocument(doc);
	}

	/**
	 * 获取当前对象中的SVGDocument
	 * 
	 * @return 显示的svg的document
	 */
	public Document getDocument() {
		return doc;
	}

	/**
	 * 获取svg图元的名称
	 * 
	 * @return 名称
	 */
	public String getText() {
		return textLabel.getText();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		textLabel = new javax.swing.JLabel();
		svgCanvas = new org.apache.batik.swing.JSVGCanvas();

		org.jdesktop.layout.GroupLayout svgCanvasLayout = new org.jdesktop.layout.GroupLayout(
				svgCanvas);
		svgCanvas.setLayout(svgCanvasLayout);
		svgCanvasLayout.setHorizontalGroup(svgCanvasLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(0, 16,
				Short.MAX_VALUE));
		svgCanvasLayout.setVerticalGroup(svgCanvasLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(0, 16,
				Short.MAX_VALUE));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup().add(textLabel,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 62,
						Short.MAX_VALUE).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(svgCanvas,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createParallelGroup(
						org.jdesktop.layout.GroupLayout.TRAILING, false).add(
						org.jdesktop.layout.GroupLayout.LEADING, textLabel,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE).add(
						org.jdesktop.layout.GroupLayout.LEADING, svgCanvas,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16,
						Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private org.apache.batik.swing.JSVGCanvas svgCanvas;
	private javax.swing.JLabel textLabel;

	// End of variables declaration//GEN-END:variables

	/**
	 * 获取缩略图面板上的图元代表的ID
	 * 
	 * @return String 图元ID
	 */
	public String getGraphUnitID() {
		return graphUnitID;
	}

	/**
	 * 设置缩略图面板上的图元代表的ID
	 * 
	 * @param graphUnitID
	 *            图元ID
	 */
	public void setGraphUnitID(String graphUnitID) {
		this.graphUnitID = graphUnitID;
	}

	/**
	 * 获取缩略图面板上的图元对象
	 * 
	 * @return NCIEquipSymbolBean 图元对象
	 */
	public NCIEquipSymbolBean getSymbolBean() {
		return symbolBean;
	}

	/**
	 * 设置缩略图面板上的图元对象
	 * 
	 * @param symbolBean
	 *            图元对象
	 */
	public void setSymbolBean(NCIEquipSymbolBean symbolBean) {
		this.symbolBean = symbolBean;
		if (!symbolBean.isReleased()) {
			svgCanvas.setBackground(Constants.THUMBNAIL_PERSONAL_BACKGROUND);
		}
	}

	public org.apache.batik.swing.JSVGCanvas getSvgCanvas() {
		return svgCanvas;
	}

	public void setSvgCanvas(org.apache.batik.swing.JSVGCanvas svgCanvas) {
		this.svgCanvas = svgCanvas;
	}

	public javax.swing.JLabel getTextLabel() {
		return textLabel;
	}

	public void setTextLabel(javax.swing.JLabel textLabel) {
		this.textLabel = textLabel;
	}
}
