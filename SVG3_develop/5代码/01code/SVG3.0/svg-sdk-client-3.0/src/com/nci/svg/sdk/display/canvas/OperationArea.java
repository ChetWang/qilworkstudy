package com.nci.svg.sdk.display.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.util.gui.resource.JToolbarSeparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableBarIconsFactory;
import com.jidesoft.swing.TitledSeparator;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.module.GraphUnitModuleAdapter;
import com.nci.svg.sdk.ui.GradientDesktopPane;
import com.nci.svg.sdk.ui.graphunit.SymbolNameDocument;
import com.nci.svg.sdk.ui.terminal.TerminalDialog;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

public class OperationArea extends JPanel {

	private static final long serialVersionUID = 7539444656687356714L;

	private SVGHandle svgHandle;

	private JComboBox statusComboBox = null;

	private JButton defaultStatusBtn = null;

	private JideSplitButton operBtn = null;

	private String defaultSymbolStatus = null;

	private JLabel statusLabel = null;

	private String[] operActions = { "更改模型", "状态复制", "重命名", "拓扑点管理" };
	
	private JToolbarSeparator sep , sep2;

	public OperationArea(SVGHandle svgHandle) {
		this.svgHandle = svgHandle;
		init();
	}

	private void init() {
		JPanel operPanel = new JPanel();
		Color startColor = ((GradientDesktopPane) svgHandle.getEditor()
				.getDesktop()).getStartColor();
		Color endColor = ((GradientDesktopPane) svgHandle.getEditor()
				.getDesktop()).getEndColor();
		FlowLayout statusLayout = new FlowLayout();
		statusLayout.setAlignment(FlowLayout.RIGHT);
		operPanel.setLayout(statusLayout);
		this.setLayout(new BorderLayout());
		statusLabel = new JLabel("当前状态:");
		operPanel.add(statusLabel);
		statusComboBox = new JComboBox();
		statusComboBox.setPreferredSize(new Dimension(65, 23));
		statusComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent) {
				if (itemevent.getStateChange() == ItemEvent.SELECTED) {
					switchSymbolStatus();
				}
			}
		});
		defaultStatusBtn = new JideButton("设为默认状态", JideIconsFactory
				.getImageIcon(JideIconsFactory.View.DESIGN));
		defaultStatusBtn.setPreferredSize(new Dimension(108, 23));
		defaultStatusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				String status = statusComboBox.getSelectedItem().toString();
				setDefaultSymbolStatus(status, true);
			}
		});

		operBtn = new JideSplitButton("操作", SearchableBarIconsFactory
				.getImageIcon(SearchableBarIconsFactory.Buttons.NEXT));
		operBtn.setForegroundOfState(ThemePainter.STATE_DEFAULT, Color.BLACK);
		operBtn.setPreferredSize(new Dimension(75, 23));
		initOperMenuItems();
		operPanel.add(statusComboBox);
		sep = new JToolbarSeparator();
		sep.setPreferredSize(new Dimension(2,22));
		sep2 = new JToolbarSeparator();
		sep2.setPreferredSize(new Dimension(2,22));
		operPanel.add(sep);
		operPanel.add(defaultStatusBtn);
		operPanel.add(sep2);
		operPanel.add(operBtn);
		this.add(operPanel, BorderLayout.NORTH);
		JComponent sepGradient = new TitledSeparator(new JLabel(),
				new PartialGradientLineBorder(new Color[] { startColor,
						endColor }, 6, PartialSide.SOUTH), SwingConstants.LEFT);
		this.add(sepGradient, BorderLayout.SOUTH);
		sepGradient.requestFocus();
	}

	private void initOperMenuItems() {
		for (int i = 0; i < operActions.length; i++) {
			final int index = i;
			operBtn.add(new AbstractAction(operActions[i]) {
				public void actionPerformed(ActionEvent e) {
					doOperation(index);
				}
			});
		}
	}

	/**
	 * 操作按钮下拉菜单触发的事件
	 * 
	 * @param index
	 */
	private void doOperation(int index) {
		switch (index) {
		case 0:
			changeModel();
			break;
		case 1:
			copyStatusContent();
			break;
		case 2:
			rename(svgHandle.getSymbolBean().getName());
			break;
		case 3:
			showTerminalDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 状态复制
	 */
	private void copyStatusContent() {
		Object currentStatus = statusComboBox.getSelectedItem();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (int i = 0; i < statusComboBox.getModel().getSize(); i++) {
			Object status = statusComboBox.getModel().getElementAt(i);
			if (!currentStatus.equals(status)) {
				model.addElement(status);
			}
		}
		JComboBox combo = new JComboBox(model);
		int result = JOptionPane.showInternalConfirmDialog(svgHandle
				.getCanvas(), combo, "请选择需要复制的状态",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			final String status = combo.getSelectedItem().toString();
			final List<Element> elements = new LinkedList<Element>();
			Runnable executeRunnable = new Runnable() {
				public void run() {
					try {
						elements.clear();
						Node node = Utilities.findNode("//*[@"
								+ Constants.SYMBOL_STATUS + "='" + status
								+ "']", svgHandle.getCanvas().getDocument()
								.getDocumentElement());
						if (node != null) {
							NodeList children = node.getChildNodes();
							for (int i = 0; i < children.getLength(); i++) {
								Node child = children.item(i);
								Node copy = svgHandle.getCanvas().getDocument()
										.importNode(child, true);
								// FIXME 拷贝后的节点id需要重新赋值，还要考虑各节点的层次结构
								// ((Element)copy).setAttribute("id", s1)
								svgHandle.getSelection().getParentElement()
										.appendChild(copy);
								elements.add((Element) copy);
							}
						}
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
					svgHandle.getSelection().clearSelection();
					svgHandle.getEditor().getSvgSession()
							.refreshCurrentHandleImediately();
					svgHandle.setModified(true);
				}
			};

			// the undo runnable
			Runnable undoRunnable = new Runnable() {

				public void run() {

					for (Element e : elements) {
						e.getParentNode().removeChild(e);
					}
					svgHandle.getSelection().clearSelection();
					svgHandle.getEditor().getSvgSession()
							.refreshCurrentHandleImediately();
					svgHandle.setModified(true);
				}
			};
			UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
					"状态复制", executeRunnable, undoRunnable,
					new HashSet<Element>(elements));

			UndoRedoActionList actionlist = new UndoRedoActionList("状态复制",
					false);
			actionlist.add(undoRedoAction);
			svgHandle.getUndoRedo().addActionList(actionlist, false);
		}
	}

	/**
	 * 更改当前图元、模板对应的模型
	 */
	private void changeModel() {
		TreeModel treeModel = ((GraphUnitModuleAdapter) svgHandle.getEditor()
				.getModuleByID(GraphUnitModuleAdapter.MODULE_ID))
				.getSymbolModelTree(null);
		JTree tree = new JTree(treeModel);
		int result = createModelChooser(tree);
		if (result == 0) {
			ModelBean model = (ModelBean) ((DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent()).getUserObject();
			svgHandle.setSymbolModelObj(model);
			svgHandle.setModified(true);
		}
	}

	/**
	 * 获取模板使用图元的关系，如果被引用过，就不允许重命名，并给予通知
	 * 
	 * @param symbolID
	 * @return
	 */
	private boolean checkSymbolTemplateRelation(String symbolID) {
		// FIXME 获取模板使用图元的关系，如果被引用过，就不允许重命名，并给予通知
		return false;
	}

	/**
	 * 获取新名字的有效性
	 * 
	 * @param newName
	 * @return
	 */
	private boolean checkNameRepeat(String newName) {
		boolean flag_remote = false;
		boolean flag_local = false;
		{ // 远程检查
			String[][] params = new String[1][2];
			params[0][0] = ActionParams.SYMBOL_NAME;
			params[0][1] = newName;
			ResultBean result = svgHandle.getEditor().getCommunicator()
					.communicate(
							new CommunicationBean(
									ActionNames.CHECK_SYMBOL_NAME_REPEAT,
									params));
			flag_remote = result.getReturnFlag() == ResultBean.RETURN_SUCCESS;
		}
		{ // 本地检查有效性
			Iterator<Map<String, NCIEquipSymbolBean>> it = svgHandle
					.getEditor().getSymbolManager().getAllSymbols().values()
					.iterator();
			Map<String, NCIEquipSymbolBean> temp = null;
			while (it.hasNext()) {
				temp = it.next();
				if (temp.containsKey(newName)) {
					flag_local = true;
					break;
				}
			}
		}
		return flag_local & flag_remote;
	}

	/**
	 * 重命名当前图元或模板
	 */
	private void rename(String showName) {
		boolean related = checkSymbolTemplateRelation(svgHandle.getSymbolBean()
				.getId());
		if (related) {
			JOptionPane.showInternalConfirmDialog(svgHandle.getSVGFrame(),
					"当前图元已经被其他模板使用，不允许重命名！", "警告", JOptionPane.CLOSED_OPTION,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		JTextField field = new JTextField();
		field.setDocument(new SymbolNameDocument());
		field.setBorder(new TitledBorder("请输入新名称："));
		String oldName = svgHandle.getSymbolBean().getName();
		field.setText(showName);
		field.setSelectionEnd(oldName.length());
		int result = JOptionPane.showInternalConfirmDialog(svgHandle
				.getSVGFrame(), field, "重命名", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String newName = field.getText().trim();
			if (newName.equals("")) {
				JOptionPane.showInternalConfirmDialog(svgHandle.getSVGFrame(),
						"请输入有效的名称！", "错误", JOptionPane.CLOSED_OPTION,
						JOptionPane.ERROR_MESSAGE);
				rename(oldName);
			}
			boolean isRepeatName = checkNameRepeat(newName);
			if (isRepeatName) {
				JOptionPane.showInternalConfirmDialog(svgHandle.getSVGFrame(),
						"新名称\"" + newName + "\"已经存在！", "错误",
						JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
				rename(newName);
			}
			if (svgHandle.getEditor().getSymbolManager().renameSymbol(oldName,
					newName,
					Utilities.getHandleSymbolType(svgHandle.getHandleType()))) {
				svgHandle.getSymbolBean().setName(newName);
				svgHandle.setName(newName);
			}
		}
	}

	private void showTerminalDialog() {
		TerminalDialog dialog = new TerminalDialog(svgHandle.getEditor(),
				svgHandle.getEditor().findParentFrame(), false);
		dialog.setTitle("拓扑点管理");
		dialog.setSize(350, 280);
		dialog.setLocationRelativeTo(svgHandle.getCanvas());
		dialog.setVisible(true);
	}

	/**
	 * 图形检查，包括状态下的图形信息等等。
	 * 
	 * @return
	 */
	private boolean checkStatusContent() {
		boolean flag = true;
		NodeList children = svgHandle.getSelection().getParentElement()
				.getChildNodes();
		if (children.getLength() == 0) {
			JOptionPane.showInternalConfirmDialog(svgHandle.getSVGFrame(),
					"当前状态下尚无任何图形信息！", "错误", JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			flag = false;
		}
		return flag;
	}

	/**
	 * 设置默认状态
	 */
	public void setDefaultSymbolStatus(String defaultSymbolStatus,
			boolean modified) {

		if (modified && !checkStatusContent()) {
			return;
		}
		this.defaultSymbolStatus = defaultSymbolStatus;
		Document svgDoc = svgHandle.getCanvas().getDocument();
		svgDoc.getDocumentElement().setAttributeNS(null,
				Constants.SYMBOL_DEFAULT_STATUS, defaultSymbolStatus);
		statusComboBox.setSelectedItem(defaultSymbolStatus);
		defaultStatusBtn.setEnabled(false);

		svgHandle.setModified(modified);
	}

	/**
	 * 切换状态
	 */
	private void switchSymbolStatus() {
		Document svgDoc = svgHandle.getCanvas().getDocument();
		String status = statusComboBox.getSelectedItem().toString();
		svgHandle.setSymbolStatus(status, false);
		try {
			NodeList symbolNodes = Utilities.findNodes("//*[@"
					+ Constants.SYMBOL_TYPE + "]", svgDoc.getDocumentElement());
			for (int i = 0; i < symbolNodes.getLength(); i++) {
				Node node = symbolNodes.item(i);
				if (node instanceof Element) {
					Element gEle = (Element) node;
					if (gEle.getAttribute(Constants.SYMBOL_STATUS).equals(
							status)) {
						EditorToolkit.setStyleProperty(gEle, "visibility", "");
					} else {
						EditorToolkit.setStyleProperty(gEle, "visibility",
								"hidden");
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		String defaultStatus = svgDoc.getDocumentElement().getAttributeNS(null,
				Constants.SYMBOL_DEFAULT_STATUS);
		if (defaultStatus != null && defaultStatus.equals(status)) {
			defaultStatusBtn.setEnabled(false);
		} else {
			defaultStatusBtn.setEnabled(true);
		}
		svgHandle.getSelection().clearSelection();
		svgHandle.getEditor().getSvgSession().refreshCurrentHandleImediately();
	}

	public SVGHandle getSvgHandle() {
		return svgHandle;
	}

	public JComboBox getStatusComboBox() {
		return statusComboBox;
	}

	public JButton getDefaultStatusBtn() {
		return defaultStatusBtn;
	}

	/**
	 * 获取默认的状态
	 * 
	 * @return
	 */
	public String getDefaultSymbolStatus() {
		return defaultSymbolStatus;
	}

	/**
	 * 将状态的组件设置可见或不可见
	 * 
	 * @param visible
	 */
	public void setStatusCompVisible(boolean visible) {
		statusLabel.setVisible(visible);
		statusComboBox.setVisible(visible);
		defaultStatusBtn.setVisible(visible);
		sep.setVisible(visible);
		sep2.setVisible(visible);
	}

	private int createModelChooser(JTree modelTree) {
		final int[] result = new int[] { -1 };
		final JDialog chooser = new JDialog(svgHandle.getEditor()
				.findParentFrame(), true);
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setViewportView(modelTree);
		chooser.getContentPane().setLayout(new BorderLayout());
		chooser.getContentPane().add(scrollPane, BorderLayout.CENTER);
		JPanel btnPanel = new JPanel();
		final JButton okBtn = new JButton("确定");
		JButton cancelBtn = new JButton("取消");
		okBtn.setBounds(5, 5, 5, 5);
		cancelBtn.setBounds(5, 5, 5, 5);
		GridBagLayout gbLayout = new GridBagLayout();
		btnPanel.setLayout(gbLayout);
		gbLayout.columnWeights = new double[] { 1.0f };
		gbLayout.rowWeights = new double[] { 1.0f };
		GridBagConstraints okConstrain = new GridBagConstraints();
		okConstrain.anchor = GridBagConstraints.EAST;
		okConstrain.gridx = 0;
		okConstrain.gridy = 0;
		okConstrain.insets = new Insets(4, 6, 4, 6);
		gbLayout.setConstraints(okBtn, okConstrain);
		GridBagConstraints cancelConstrain = new GridBagConstraints();
		cancelConstrain.anchor = GridBagConstraints.EAST;
		cancelConstrain.gridx = 1;
		cancelConstrain.gridy = 0;
		cancelConstrain.insets = new Insets(4, 6, 4, 6);
		gbLayout.setConstraints(cancelBtn, cancelConstrain);
		btnPanel.add(okBtn);
		btnPanel.add(cancelBtn);
		okBtn.setEnabled(false);
		chooser.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		modelTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent treeselectionevent) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeselectionevent
						.getNewLeadSelectionPath().getLastPathComponent();
				if (node.getUserObject() instanceof ModelBean) {
					okBtn.setEnabled(true);
				} else {
					okBtn.setEnabled(false);
				}
			}
		});

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				result[0] = 0;
				chooser.dispose();
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				result[0] = -1;
				chooser.dispose();
			}
		});
		chooser.setSize(440, 350);
		chooser.setTitle("请选择新的模型");
		chooser.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		chooser.setLocationRelativeTo(svgHandle.getSVGFrame());
		chooser.setVisible(true);
		return result[0];
	}
}
