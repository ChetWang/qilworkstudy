package com.nci.svg.ui.graphunit;

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
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.graphunit.NCIGraphUnitTypeBean;
import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;
import com.nci.svg.ui.DropDownButton;
import com.nci.svg.ui.EditorPanel;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class NCIGraphUnitSelectionPanel extends EditorPanel {

	private static final long serialVersionUID = 1436388319305773693L;
	private SVGHandle handle;
	private JComboBox equipCombo;
	private JComboBox symbolCombo;
	private DropDownButton ddbuton;
	private JRadioButton[] radioGroup = new JRadioButton[3];

	// 图元操作的菜单列表ID
	private String[] menuID = { "nci_graphunit_reset",
			"nci_graphunit_addEquip", "nci_graphunit_delete",
			"nci_graphunit_search", "nci_open_local_graphunit",
			"nci_save_graphunit_to_server" };
	private Document equipSymbolDoc;

	public NCIGraphUnitSelectionPanel(Editor editor) {
		super(editor);
		iniComponents();
	}

	public NCIGraphUnitSelectionPanel(SVGHandle handle) {
		super(handle.getEditor());
		this.handle = handle;
		iniComponents();
	}

	/**
	 * 初始化组件
	 */
	private void iniComponents() {
		JPanel panel1 = new JPanel();// 第一层panel，用于放设备、符号等label和combox

		JPanel panel2 = this.createStatusRadioPanel();// 第二层panel，用来放状态radiobutton

		GridBagLayout top_layout = new GridBagLayout();
		this.setLayout(top_layout);
		// 2 rows 1 column
		top_layout.columnWeights = new double[] { 1.0f };
		top_layout.rowWeights = new double[] { 1.0f, 0.1f };
		GridBagConstraints gbc_panel1 = new GridBagConstraints();
		gbc_panel1.gridx = 0;
		gbc_panel1.gridy = 0;
		gbc_panel1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel1.anchor = GridBagConstraints.WEST;
		top_layout.setConstraints(panel1, gbc_panel1);
		this.add(panel1);
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.gridx = 0;
		gbc_panel2.gridy = 1;
		// gbc_panel2.anchor = GridBagConstraints.WEST;
		// gbc_panel2.fill = GridBagConstraints.HORIZONTAL;
		top_layout.setConstraints(panel2, gbc_panel2);
		this.add(panel2);
		panel1.setBorder(new EmptyBorder(3, 3, 3, 3));
		JLabel equipTypeLabel = new JLabel(ResourcesManager.bundle
				.getString("nci_equipTypeLabel"));
		JLabel symboListLabel = new JLabel(ResourcesManager.bundle
				.getString("nci_symboListLabel"));
		equipCombo = new JComboBox();
		symbolCombo = new JComboBox();

		GridBagLayout layout_panel1 = new GridBagLayout();
		panel1.setLayout(layout_panel1);
		// 1 row 5 columns
		layout_panel1.columnWeights = new double[] { 0.1f, 0.5f, 0.1f, 0.5f,
				0.1f };
		layout_panel1.rowWeights = new double[] { 0.0 };

		GridBagConstraints gbc_equipTypeLabel = new GridBagConstraints();
		gbc_equipTypeLabel.gridx = 0;
		gbc_equipTypeLabel.gridy = 0;
		gbc_equipTypeLabel.ipadx = 0;
		gbc_equipTypeLabel.insets = new Insets(0, 0, 0, 3);
		layout_panel1.setConstraints(equipTypeLabel, gbc_equipTypeLabel);
		panel1.add(equipTypeLabel);
		GridBagConstraints gbc_equipCombo = new GridBagConstraints();
		gbc_equipCombo.gridx = 1;
		gbc_equipCombo.gridy = 0;
		gbc_equipCombo.ipadx = 5;
		gbc_equipCombo.insets = new Insets(0, 0, 0, 5);
		gbc_equipCombo.anchor = GridBagConstraints.WEST;
		layout_panel1.setConstraints(equipCombo, gbc_equipCombo);
		GridBagConstraints gbc_symboListLabel = new GridBagConstraints();
		gbc_symboListLabel.gridx = 2;
		gbc_symboListLabel.gridy = 0;
		gbc_symboListLabel.ipadx = 0;
		gbc_symboListLabel.insets = new Insets(0, 0, 0, 3);
		layout_panel1.setConstraints(symboListLabel, gbc_symboListLabel);
		panel1.add(symboListLabel);
		panel1.add(equipCombo);
		GridBagConstraints gbc_symboCombo = new GridBagConstraints();
		gbc_symboCombo.gridx = 3;
		gbc_symboCombo.gridy = 0;
		gbc_symboCombo.ipadx = 5;
		gbc_symboCombo.insets = new Insets(0, 0, 0, 5);
		gbc_symboCombo.anchor = GridBagConstraints.WEST;
		layout_panel1.setConstraints(symbolCombo, gbc_symboCombo);
		panel1.add(symbolCombo);

		/** ******DropDownButton******* */
		ddbuton = new DropDownButton(ResourcesManager.bundle
				.getString("nci_graphunit_dropdown_text"));
		ddbuton.setEnabled(true);
		GridBagConstraints gbc_ddbuton = new GridBagConstraints();
		gbc_ddbuton.gridx = 4;
		gbc_ddbuton.gridy = 0;
		gbc_ddbuton.ipadx = 0;
		// gbc_equipTypeLabel.ipady = 4;
		gbc_ddbuton.insets = new Insets(0, 0, 0, 20);
		layout_panel1.setConstraints(ddbuton, gbc_ddbuton);
		panel1.add(ddbuton);
		this.createDropDownMenu(ddbuton);

		DefaultComboBoxModel equipCombomodel = new DefaultComboBoxModel();
		equipCombomodel.addElement(ResourcesManager.bundle
				.getString("nci_combos_please_select"));
		equipCombo.setModel(equipCombomodel);
		equipCombo.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent itemevent) {
				if (itemevent.getStateChange() == ItemEvent.SELECTED) {
					if (equipCombo.getSelectedIndex() != 0) {
						clearCombo(symbolCombo);
						initSymbolComboModel();
						// SwingWorker<Object, Object> worker = new
						// SwingWorker<Object, Object>() {
						//
						// @Override
						// protected Object doInBackground() throws Exception {
						// initSymbolComboModel();
						// return null;
						// }
						// };
						// LongtermTaskManager.getInstance().addAndStartLongtermTask(
						// new LongtermTask(
						// ResourcesManager.bundle.getString("nci_symboes_initializing"),
						// worker));
					} else {
						clearCombo(symbolCombo);
					}
				}
			}
		});
		DefaultComboBoxModel symboCombomodel = new DefaultComboBoxModel();
		symboCombomodel.addElement(ResourcesManager.bundle
				.getString("nci_combos_please_select"));
		symbolCombo.setModel(symboCombomodel);
		NCIThumbnailRenderer renderer = new NCIThumbnailRenderer();
		renderer.setPreferredSize(new Dimension(120, 22));
		renderer.setBorder(new EtchedBorder());
		symbolCombo.setRenderer(renderer);
		symbolCombo.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent itemevent) {
				if (itemevent.getStateChange() == ItemEvent.SELECTED) {

					showGraphUnit();
				}
			}
		});
		// initEquipComboModel();
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				initEquipComboModel();
				return null;
			}
		};
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(
				new LongtermTask(ResourcesManager.bundle
						.getString("nci_equipment_initializing"), worker));

	}

	/**
	 * 创建下拉按钮的菜单
	 * 
	 * @param ddbutton
	 */
	private void createDropDownMenu(DropDownButton ddbutton) {
		JMenu dropdownMenu = ddbutton.getMenu();
		for (int i = 0; i < menuID.length; i++) {
			JMenuItem item = new JMenuItem(ResourcesManager.bundle
					.getString(menuID[i]));
			dropdownMenu.add(item);
			item.addActionListener(new DropDownMenuListener(i,this));
		}
	}

	/**
	 * 创建符号状态面板
	 * 
	 * @return
	 */
	private JPanel createStatusRadioPanel() {
		JPanel radioPanel = new JPanel();
		ButtonGroup bg = new ButtonGroup();
		radioPanel.setLayout(new FlowLayout());
		JLabel label = new JLabel(ResourcesManager.bundle
				.getString("nci_graphunit_equipStatus"));
		label.setEnabled(false);
		radioPanel.add(label);
		radioGroup[0] = new JRadioButton(ResourcesManager.bundle
				.getString("nci_graphunit_equipStatus_open"));
		radioGroup[1] = new JRadioButton(ResourcesManager.bundle
				.getString("nci_graphunit_equipStatus_close"));
		radioGroup[2] = new JRadioButton(ResourcesManager.bundle
				.getString("nci_graphunit_equipStatus_none"));
		for (int i = 0; i < radioGroup.length; i++) {
			bg.add(radioGroup[i]);
			radioPanel.add(radioGroup[i]);
			radioGroup[i].setEnabled(false);
			radioGroup[i].setForeground(Color.BLACK);
		}
		return radioPanel;
	}

	/**
	 * 获取设备信息，并初始化设备combobox
	 */
	private void initEquipComboModel() throws IOException{
		clearCombo(equipCombo);
		ArrayList<NCIGraphUnitTypeBean> equipSymboList = editor
				.getGraphUnitManager().getEquipSymbolTypeList();
		for (NCIGraphUnitTypeBean bean : equipSymboList) {
			((DefaultComboBoxModel) equipCombo.getModel()).addElement(bean);
		}

	}

	/**
	 * 清空符号combobox。 当什么设备都没选时，符号combobox应为空。
	 */
	private void clearCombo(JComboBox combo) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(ResourcesManager.bundle
				.getString("nci_combos_please_select"));
		combo.setModel(model);
	}

	/**
	 * 获取符号信息，并初始化符号combobox
	 */
	private void initSymbolComboModel() {
		// this.clearCombo(symbolCombo);
		NCIGraphUnitTypeBean bean = (NCIGraphUnitTypeBean) equipCombo.getModel()
				.getSelectedItem();
		ArrayList<NCIThumbnailPanel> thumbnails = editor
				.getGraphUnitManager().getThumnailList(bean,
						NCIThumbnailPanel.THUMBNAIL_COMBOBOX);
		for (NCIThumbnailPanel thumnail : thumbnails) {
			((DefaultComboBoxModel) symbolCombo.getModel())
					.addElement(thumnail);
		}
	}

	/**
	 * 显示图元，在符号列表中选中某个符号后显示
	 */
	private void showGraphUnit() {
		if (symbolCombo.getSelectedIndex() != 0) {
			final NCIThumbnailPanel thumbnail = (NCIThumbnailPanel) symbolCombo
					.getModel().getSelectedItem();
			thumbnail.getDocument().getDocumentElement().setAttributeNS(null,
					"width", "450");
			thumbnail.getDocument().getDocumentElement().setAttributeNS(null,
					"height", "450");
			handle.getCanvas().reloadNciPoint(thumbnail.getDocument());
			handle.getCanvas().setDocument(thumbnail.getDocument(), null, true);
			// Document doc = null;
			// try {
			// doc =
			// Editor.getEditor().getGraphUnitManager().createDocument("file:/C:/svgpic/batikFX.svg");
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// handle.getCanvas().setDocument(doc,null,true);
		} else {
			reset();
		}
	}

	private void reset() {
		if (handle.isModified()) {
			int result = JOptionPane.showConfirmDialog(handle.getSVGFrame(),
					"图元已经更改, 确定重置?", "警告", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION)
				return;
		}
		handle.setModified(false);
		handle.getSelection().clearSelection();
		equipCombo.setSelectedIndex(0);
		symbolCombo.setSelectedIndex(0);
		handle.getCanvas().clearCache();
		//将根节点下所有的子节点全部去除，并刷新画板，就得到重置的效果
		Element root = handle.getCanvas().getDocument().getDocumentElement();
		NodeList children = root.getChildNodes();
		for(int i=0;i<children.getLength();i++){
			root.removeChild(children.item(i));
		}
		handle.getCanvas().requestRepaintContent();
	}

	public SVGHandle getHandle() {
		return handle;
	}

	private class DropDownMenuListener implements ActionListener {

		private int index;
		private NCIGraphUnitSelectionPanel panel;

		public DropDownMenuListener(int index,NCIGraphUnitSelectionPanel panel) {
			this.index = index;
			this.panel = panel;
		}

		public void actionPerformed(ActionEvent actionevent) {
			switch (index) {
			case 0:// 图元重置
				reset();
				break;
			case 1:// 添加设备

				break;
			case 2:// 删除图元

				break;
			case 3:// 图元查询

				break;
				
			case 4://打开本地图元
				SwingWorker worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						editor.getIOManager().getFileOpenManager().askUserForGraphUnitFile(panel);
						return null;
					}
					
				};
				worker.execute();
//				Editor.getEditor().getIOManager().getFileOpenManager().askUserForGraphUnitFile(panel);
				break;
				
			case 5://保存至服务器
				
				break;
			}
		}
	}
}
