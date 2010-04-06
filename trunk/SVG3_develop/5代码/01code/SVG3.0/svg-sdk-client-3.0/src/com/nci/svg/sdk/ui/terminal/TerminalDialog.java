/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-12-3
 * @功能：拓扑点维护窗口
 *
 */
package com.nci.svg.sdk.ui.terminal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.ui.ComboPanel;
import com.nci.svg.sdk.ui.EditPanel;
import com.nci.svg.sdk.ui.NCIButtonPanel;
import com.nci.svg.sdk.ui.NciCustomDialog;

/**
 * 
 * @author yx.nci
 */
public class TerminalDialog extends javax.swing.JDialog {

	private EditorAdapter editor = null;
	private Document doc = null;
	private DefaultListModel listModel = null;
	private Dimension btnSize = new Dimension(80, 25);

	/** Creates new form TerminalDialog */
	public TerminalDialog(EditorAdapter editor, java.awt.Frame parent,
			boolean modal) {
		super(parent, modal);
		this.editor = editor;
		initComponents();
		if (editor != null) {
			doc = editor.getHandlesManager().getCurrentHandle().getCanvas()
					.getDocument();
		}
		initList();
	}

	private JPanel createBtnAddPanel() {		
		addTerminalBtn = new javax.swing.JButton("新建");
		addTerminalBtn.setPreferredSize(btnSize);
		manualAddTerminalBtn = new javax.swing.JButton("手动增加");
		manualAddTerminalBtn.setPreferredSize(btnSize);
		JPanel btnPanel = new JPanel();
		addTerminalBtn.setBounds(5, 5, 5, 5);
		manualAddTerminalBtn.setBounds(5, 5, 5, 5);
		GridBagLayout gbLayout = new GridBagLayout();
		btnPanel.setLayout(gbLayout);
		gbLayout.columnWeights = new double[] { 1.0f };
		gbLayout.rowWeights = new double[] { 1.0f };
		GridBagConstraints okConstrain = new GridBagConstraints();
		okConstrain.anchor = GridBagConstraints.EAST;
		okConstrain.gridx = 0;
		okConstrain.gridy = 0;
		okConstrain.insets = new Insets(4, 6, 4, 6);
		gbLayout.setConstraints(addTerminalBtn, okConstrain);
		GridBagConstraints cancelConstrain = new GridBagConstraints();
		cancelConstrain.anchor = GridBagConstraints.EAST;
		cancelConstrain.gridx = 1;
		cancelConstrain.gridy = 0;
		cancelConstrain.insets = new Insets(4, 6, 4, 6);
		gbLayout.setConstraints(manualAddTerminalBtn, cancelConstrain);
		btnPanel.add(addTerminalBtn);
		btnPanel.add(manualAddTerminalBtn);
		return btnPanel;
	}
	
	private JPanel createPointAddPanel(){
		centerPoint = new javax.swing.JCheckBox("中心点");
		leftandrightPoint = new javax.swing.JCheckBox("左右边界点");
		upanddownPoint = new javax.swing.JCheckBox("上下边界点");
		fourwayPoint = new javax.swing.JCheckBox("四向边界点");
		inclinedFourwayPoint = new javax.swing.JCheckBox("斜四向边界点");
		JPanel pointPanel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(pointPanel, BoxLayout.Y_AXIS);
		pointPanel.setLayout(boxlayout);
		pointPanel.setBorder(new EtchedBorder());
		pointPanel.add(centerPoint);
		pointPanel.add(Box.createRigidArea(new Dimension(5,5)));
		pointPanel.add(leftandrightPoint);
		pointPanel.add(Box.createRigidArea(new Dimension(5,5)));
		pointPanel.add(upanddownPoint);
		pointPanel.add(Box.createRigidArea(new Dimension(5,5)));
		pointPanel.add(fourwayPoint);
		pointPanel.add(Box.createRigidArea(new Dimension(5,5)));
		pointPanel.add(inclinedFourwayPoint);
		return pointPanel;
	}
	
	private JPanel createPointDeletePanel(){
		JPanel p = new JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		pointList = new javax.swing.JList(listModel);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(5);
		layout.setVgap(5);
		p.setLayout(layout);
		p.add(createBtnDeletePanel(), BorderLayout.SOUTH);
		p.add(jScrollPane1, BorderLayout.CENTER);
		return p;
	}
	
	private JPanel createBtnDeletePanel(){
		removeTerminalBtn = new javax.swing.JButton("删除");
		removeTerminalBtn.setBounds(5, 5, 5, 5);
		removeTerminalBtn.setPreferredSize(btnSize);
		removeAllTerminalBtn = new javax.swing.JButton("删除所有");
		removeAllTerminalBtn.setBounds(5, 5, 5, 5);
		removeAllTerminalBtn.setPreferredSize(btnSize);
		JPanel btnPanel = new JPanel();
		GridBagLayout gbLayout = new GridBagLayout();
		btnPanel.setLayout(gbLayout);
		gbLayout.columnWeights = new double[] { 1.0f };
		gbLayout.rowWeights = new double[] { 1.0f};
		GridBagConstraints okConstrain = new GridBagConstraints();
		okConstrain.anchor = GridBagConstraints.EAST;
		okConstrain.gridx = 0;
		okConstrain.gridy = 0;
		okConstrain.insets = new Insets(4, 6, 4, 6);
		gbLayout.setConstraints(removeTerminalBtn, okConstrain);
		GridBagConstraints cancelConstrain = new GridBagConstraints();
		cancelConstrain.anchor = GridBagConstraints.EAST;
		cancelConstrain.gridx = 1;
		cancelConstrain.gridy = 0;
		cancelConstrain.insets = new Insets(4, 6, 4, 6);
		gbLayout.setConstraints(removeAllTerminalBtn, cancelConstrain);
		btnPanel.add(removeTerminalBtn);
		btnPanel.add(removeAllTerminalBtn);
		return btnPanel;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		listModel = new DefaultListModel();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		createPointPanel = new javax.swing.JPanel();
		deletePointPanel = new javax.swing.JPanel();
		
		
		jLabel1 = new javax.swing.JLabel("已有拓扑点:");
		JPanel pointPanel = createPointAddPanel();
		BorderLayout layout = new BorderLayout();
		
		createPointPanel.setLayout(layout);
		createPointPanel.add(pointPanel, BorderLayout.CENTER);
		JPanel btnPanel = createBtnAddPanel();
		createPointPanel.add(btnPanel, BorderLayout.SOUTH);
		BorderLayout deletelayout = new BorderLayout();
		deletelayout.setHgap(5);
		deletelayout.setVgap(5);
		deletePointPanel.setLayout(deletelayout);
		JPanel p = createPointDeletePanel();
		deletePointPanel.add(p, BorderLayout.CENTER);
		deletePointPanel.add(jLabel1,BorderLayout.NORTH);
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		leftandrightPoint
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						leftandrightPointActionPerformed(evt);
					}
				});

		upanddownPoint.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				upanddownPointActionPerformed(evt);
			}
		});
		fourwayPoint.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fourwayPointActionPerformed(evt);
			}
		});

		addTerminalBtn.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AddTerminalActionPerformed(evt);
			}
		});

		manualAddTerminalBtn
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manualAddTerminalActionPerformed(evt);
					}
				});

		jTabbedPane1.addTab("新建拓扑点", createPointPanel); // NOI18N
		pointList.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				listPointMouseClicked(evt);
			}
		});
		jScrollPane1.setViewportView(pointList);
		removeTerminalBtn
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						removeTerminalActionPerformed(evt);
					}
				});
		removeAllTerminalBtn
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						removeAllTerminalActionPerformed(evt);
					}
				});


		jTabbedPane1.addTab("删除拓扑点", deletePointPanel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jTabbedPane1);
		pack();
	}

	/**
	 * 删除选中的拓扑点
	 * 
	 * @param evt
	 */
	private void removeTerminalActionPerformed(java.awt.event.ActionEvent evt) {

		String value = (String) pointList.getSelectedValue();
		if (value == null) {
			return;
		}
		Element metadata = getMetadataElement();
		if (metadata == null) {
			return;
		}
		removeTerminalNote(metadata, value);
	}

	/**
	 * 删除所有的拓扑点
	 * 
	 * @param evt
	 */
	private void removeAllTerminalActionPerformed(java.awt.event.ActionEvent evt) {
		// 
		Element metadata = getMetadataElement();
		if (metadata == null) {
			return;
		}
		removeTerminalNote(metadata);
	}

	/**
	 * 新建选中类型的拓扑点
	 * 
	 * @param evt
	 */
	/**
	 * @param evt
	 */
	private void AddTerminalActionPerformed(java.awt.event.ActionEvent evt) {
		// 

		Element metadata = getMetadataElement();
		if (metadata == null) {
			return;
		}

		// 增加中心点
		if (centerPoint.isSelected()) {
			addTerminalNote(metadata, TerminalModule.CENTER_POINT);
		}

		// 增加四向边界点
		if (fourwayPoint.isSelected()) {
			addTerminalNote(metadata, TerminalModule.FOURWAY_POINT);
		} else {
			if (leftandrightPoint.isSelected()) {
				addTerminalNote(metadata, TerminalModule.LEFTANDRIGHT_POINT);
			}

			if (upanddownPoint.isSelected()) {
				addTerminalNote(metadata, TerminalModule.UPANDDOWN_POINT);
			}
		}

		// 增加斜四向边界点
		if (inclinedFourwayPoint.isSelected()) {
			addTerminalNote(metadata, TerminalModule.INCLINE_FOURWAY_POINT);
		}

		initList();

	}

	/**
	 * 手动增加拓扑点
	 * 
	 * @param evt
	 */
	private void manualAddTerminalActionPerformed(java.awt.event.ActionEvent evt) {
		//
		editor.getSelectionManager().setToManualTerminalMode(this);
	}
	
	public void manualAddTerminal(final Point2D point) {
		//
		final NciCustomDialog dialog = new NciCustomDialog(editor.findParentFrame(),true);
		dialog.setTitle("拓扑点属性");
		final EditPanel namePanel = new EditPanel();
		namePanel.getShowText().setText("名称");
		dialog.addComponent(namePanel);
		final ComboPanel rangePanel = new ComboPanel();
		rangePanel.getShowText().setText("应用范围");
		ArrayList<String> list = new ArrayList<String>();
		list.add("当前状态");
		list.add("所有状态");
		rangePanel.setSonComboData(list);
		dialog.addComponent(rangePanel);
		NCIButtonPanel button = new NCIButtonPanel();
		button.getButtonOK().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(namePanel.getEditText().getText() == null ||
						namePanel.getEditText().getText().length() == 0)
				{
					editor.getSvgSession().showMessageBox("不能为空", "拓扑点名称不能为空");
					return;
				}
				if(rangePanel.getSonCombo().getSelectedIndex() == -1)
				{
					editor.getSvgSession().showMessageBox("不能为空", "应用范围不能为空");
					return;
				}
				String name = namePanel.getEditText().getText();
				String range = ((SimpleCodeBean)rangePanel.getSonCombo().getSelectedItem()).getCode();
				if(range.equals("当前状态"))
				{
					addTerminalIntoStatus(name,point);
				}
				else
				{
					Element metadata = getMetadataElement();
					if(metadata == null)
						return;
				    addTerminalNote(metadata,name,point);
				}
				dialog.setVisible(false);
			}
		});
		button.getButtonCancel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		dialog.addComponent(button);
		dialog.setLocationRelativeTo(editor.findParentFrame());
		dialog.setVisible(true);
	}

	private void fourwayPointActionPerformed(java.awt.event.ActionEvent evt) {
		// 
	}

	private void leftandrightPointActionPerformed(java.awt.event.ActionEvent evt) {
		// 
	}

	private void upanddownPointActionPerformed(java.awt.event.ActionEvent evt) {
		// 
	}

	private void listPointMouseClicked(java.awt.event.MouseEvent evt) {
		// 
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				TerminalDialog dialog = new TerminalDialog(null,
						new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {

					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify
	private javax.swing.JButton addTerminalBtn;
	private javax.swing.JCheckBox centerPoint;
	private javax.swing.JButton manualAddTerminalBtn;
	private javax.swing.JCheckBox fourwayPoint;
	private javax.swing.JCheckBox inclinedFourwayPoint;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel createPointPanel;
	private javax.swing.JPanel deletePointPanel;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JCheckBox leftandrightPoint;
	private javax.swing.JList pointList;
	private javax.swing.JButton removeAllTerminalBtn;
	private javax.swing.JButton removeTerminalBtn;
	private javax.swing.JCheckBox upanddownPoint;

	// End of variables declaration

	/**
	 * 获取拓扑点定义域节点
	 * 
	 * @return：拓扑点定义域节点，失败返回null
	 */
	protected Element getMetadataElement() {
		if (doc == null) {
			return null;
		}

		// 选中自动选点配置节点
		Element metadata = null;
		try {
			metadata = (Element) Utilities.findNode(
					"//*[@" + Constants.NCI_TYPE + "='" + TerminalModule.NCI_TYPE_TERMINAL +"']", doc.getDocumentElement());
		} catch (XPathExpressionException e) {
			//
			e.printStackTrace();
		}

		// 不存在则自行创建
		if (metadata == null) {
			Element defs = (Element) doc.getDocumentElement()
					.getElementsByTagName("defs").item(0);
			if (defs == null) {
				defs = doc.createElement("defs");
				doc.getDocumentElement().appendChild(defs);
			}

			metadata = doc.createElement("metadata");
			metadata.setAttribute(Constants.NCI_TYPE, TerminalModule.NCI_TYPE_TERMINAL);
			defs.appendChild(metadata);
		}
		return metadata;
	}

	/**
	 * 在定义域增加拓扑点描述
	 * 
	 * @param parent：父节点
	 * @param strNote：描述内容
	 */
	protected void addTerminalNote(Element parent, String strNote) {
		Element element = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("//*[@").append(TerminalModule.DEFS_TERMINIAL_NOTE).append("='").append(strNote)
					.append("']");
			element = (Element) Utilities.findNode(buffer.toString(), parent);
			if (element == null) {
				element = parent.getOwnerDocument().createElement(
						TerminalModule.DEFS_TERMINIAL_NCINAME);
				element.setAttribute(TerminalModule.DEFS_TERMINIAL_NOTE, strNote);
				parent.appendChild(element);
			}
		} catch (XPathExpressionException e) {
			//
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * add by yux,2009-1-6
	 * 在定义域增加手动选择的拓扑点
	 * @param parent:父节点
	 * @param name:名称
	 * @param point:坐标
	 */
	protected void addTerminalNote(Element parent,String name, Point2D point) {
		Element element = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("//*[@").append(TerminalModule.DEFS_TERMINIAL_NAME).append("='").append(name)
					.append("']");
			element = (Element) Utilities.findNode(buffer.toString(), parent);
			if (element == null) {
				element = parent.getOwnerDocument().createElement(
						TerminalModule.DEFS_TERMINIAL_NCINAME);
				
				parent.appendChild(element);
			}
			element.setAttribute(TerminalModule.DEFS_TERMINIAL_NOTE, TerminalModule.MANUAL_TERMINAL);
			element.setAttribute(TerminalModule.DEFS_TERMINIAL_NAME, name);
			EditorToolkit.setAttributeValue(element,"x",point.getX());
			EditorToolkit.setAttributeValue(element,"y",point.getY());
		} catch (XPathExpressionException e) {
			//
			e.printStackTrace();
		}
		return;
	}

	/**
	 * add by yux,2009-1-7
	 * 在当前状态界面上加入指定名称的拓扑点
	 * @param name:拓扑点名称
	 * @param point：位置
	 */
	protected void addTerminalIntoStatus(String name, Point2D point)
	{
		Element parent = editor.getHandlesManager().getCurrentHandle().getSelection().getParentElement();
		Element useElement = null;
		try {
			//检查符合名称的拓扑点
			StringBuffer buffer = new StringBuffer();
			buffer.append("//*[@name='").append(name)
					.append("']");
			useElement = (Element) Utilities.findNode(buffer.toString(), parent);
			if(useElement == null)
			{
				useElement = ((SVGOMDocument) doc).createElementNS(doc
						.getDocumentElement().getNamespaceURI(), "use");
				parent.appendChild(useElement);
			}
		}
		 catch (XPathExpressionException e) {
		}
		useElement.setAttribute("name", name);
		EditorToolkit.setAttributeValue(useElement,"x",point.getX());
		EditorToolkit.setAttributeValue(useElement,"y",point.getY());
		useElement.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href", "#terminal");
		EditorToolkit.setAttributeValue(useElement, "width", 2);
		EditorToolkit.setAttributeValue(useElement, "height", 2);
		return;
	}
	/**
	 * 在定义域删除拓扑点描述
	 * 
	 * @param parent：父节点
	 * @param strNote：描述内容
	 */
	protected void removeTerminalNote(Element parent, String strNote) {
		Element element = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("//*[@").append(TerminalModule.DEFS_TERMINIAL_NOTE).append("='").append(strNote)
					.append("']");
			element = (Element) Utilities.findNode(buffer.toString(), parent);
			if (element != null) {
				parent.removeChild(element);
				listModel.removeElement(strNote);
			}
		} catch (XPathExpressionException e) {
			//
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 在定义域删除所有拓扑点描述
	 * 
	 * @param parent：父节点
	 */
	protected void removeTerminalNote(Element parent) {

		NodeList nodeList = parent.getElementsByTagName(TerminalModule.DEFS_TERMINIAL_NCINAME);
		int length = nodeList.getLength();
		for (int i = length - 1; i >= 0; i--) {
			parent.removeChild(nodeList.item(i));
		}
		listModel.removeAllElements();
		return;
	}

	/**
	 * 从定义域及状态域中获取拓扑点信息加入删除队列中
	 */
	protected void initList() {
		if (doc == null) {
			return;
		}
		listModel.removeAllElements();

		// 选中自动选点配置节点
		Element metadata = null;
		try {
			metadata = (Element) Utilities.findNode(
					"//*[@" + Constants.NCI_TYPE + "='" + TerminalModule.NCI_TYPE_TERMINAL +"']", doc.getDocumentElement());
		} catch (XPathExpressionException e) {
			//
			e.printStackTrace();
		}
		// 定义域不为空
		if (metadata != null) {
			NodeList nodeList = metadata.getElementsByTagName(TerminalModule.DEFS_TERMINIAL_NCINAME);
			int length = nodeList.getLength();
			for (int i = 0; i < length; i++) {
				if (nodeList.item(i) instanceof Element) {
					Element element = (Element) nodeList.item(i);
					String text = element.getAttribute(TerminalModule.DEFS_TERMINIAL_NOTE);
					listModel.addElement(text);
				}
			}

		}
		return;
	}
}
