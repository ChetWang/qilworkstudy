package com.nci.svg.sdk.graphunit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.ui.EditorPanel;
import com.nci.svg.sdk.ui.autocomplete.AutoCompletionField;
import com.nci.svg.sdk.ui.graphunit.SymbolNameDocument;

/**
 * ͼԪ��ģ���Ӧ��ģ��ѡ����
 * 
 * @author Qil.Wong
 * 
 */
public class SymbolModelChooserPanel extends EditorPanel {

	private static final long serialVersionUID = -6351142577795663083L;

	private AutoCompletionField filterField = null;
	/**
	 * ȷ����ť
	 */
	private JButton okBtn = null;
	/**
	 * ȡ����ť
	 */
	private JButton cancelBtn = null;

	/**
	 * ģ����
	 */
	private JTree modelTree = null;

	/**
	 * ģ��״̬ComboBox
	 */
	private JComboBox statusCombo = null;

	/**
	 * ������������
	 */
	private JPanel nameInputPanel;

	private JPanel statusPanel;

	private JComboBox symbolTypeCombo = null;

	/**
	 * ��װsymbolTypeCombo��panel
	 */
	private JPanel symbolTypeComboPanel = null;
	/**
	 * ���������
	 */
	private JTextField nameField = null;

	private JMenuItem refreshItem = null;

	private int symbolType = -1;

	public SymbolModelChooserPanel(EditorAdapter editor) {
		super(editor);
		init();

		// �������Ƶĸ�ʽ������
		nameField.setDocument(new SymbolNameDocument());
	}

	/**
	 * ��ʼ��ģ��ѡ��������������
	 */
	private void init() {
		GridBagLayout rootLayout = new GridBagLayout();
		this.setLayout(rootLayout);
		JPanel treePanel = new JPanel();
		GridBagConstraints treeConstraints = new GridBagConstraints();
		treeConstraints.gridx = 0;
		treeConstraints.gridy = 0;
		treeConstraints.insets = new Insets(5, 5, 0, 5);
		treeConstraints.fill = GridBagConstraints.BOTH;
		rootLayout.columnWeights = new double[] { 1.0f };
		rootLayout.rowWeights = new double[] { 1.0f };
		rootLayout.setConstraints(treePanel, treeConstraints);
		BorderLayout treePanelLayout = new BorderLayout(0, 2);
		treePanel.setLayout(treePanelLayout);
		JPanel filterPanel = new JPanel();
		FlowLayout filterLayout = new FlowLayout();
		filterLayout.setAlignment(FlowLayout.LEFT);
		filterPanel.setLayout(filterLayout);
		if (!editor.getModeManager().isGraphUnitHasModel()) {
			filterPanel.setVisible(false);
		}
		filterField = new AutoCompletionField();
		filterField.setPreferredSize(new Dimension(250, 23));
		filterPanel.add(filterField);
		treePanel.add(filterPanel, BorderLayout.NORTH);

		// treePanel.add(statusCombo, BorderLayout.SOUTH);
		JScrollPane scrool = new JScrollPane();
		modelTree = new JTree();
		if (!editor.getModeManager().isGraphUnitHasModel()) {
			scrool.setVisible(false);
		}
		modelTree.setModel(null);
		modelTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		final JPopupMenu rootPopupMenu = new JPopupMenu();
		refreshItem = new JMenuItem("ˢ��");
		rootPopupMenu.add(refreshItem);
		modelTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getButton() == MouseEvent.BUTTON3
						&& evt.getClickCount() == 1) {
					int index = modelTree.getRowForLocation(evt.getX(), evt
							.getY());
					if (index == 0) {
						rootPopupMenu.setLocation(evt.getLocationOnScreen());
						rootPopupMenu.setVisible(true);
						modelTree.setSelectionRow(index);
					}
				}
			}
		});

		treePanel.add(scrool, BorderLayout.CENTER);
		scrool.setViewportView(modelTree);
		JPanel btnPanel = new JPanel();
		okBtn = new JButton("ȷ��");
		cancelBtn = new JButton("�ر�");
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
		statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());

		statusCombo = new JComboBox();
		statusPanel.add(statusCombo, BorderLayout.CENTER);
		// statusCombo.setBorder(new TitledBorder("��ѡ��ǰͼԪ/ģ���Ӧģ�͵�״̬:"));
		statusPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(new PartialGradientLineBorder(new Color[] {
						new Color(0, 0, 128),
						UIDefaultsLookup.getColor("control") }, 2,
						PartialSide.NORTH), "��ѡ��ǰͼԪ/ģ���Ӧģ�͵�״̬",
						TitledBorder.LEFT, TitledBorder.ABOVE_TOP),
				BorderFactory.createEmptyBorder(6, 4, 4, 4)));
		GridBagConstraints statusConstraints = new GridBagConstraints();
		statusConstraints.gridx = 0;
		statusConstraints.gridy = 1;
		statusConstraints.insets = new Insets(3, 5, 3, 1);
		statusConstraints.fill = GridBagConstraints.BOTH;
		rootLayout.setConstraints(statusPanel, statusConstraints);

		nameInputPanel = new JPanel();
		nameInputPanel.setLayout(new BorderLayout());
		nameField = new JTextField();
		// nameInputPanel.setBorder(new TitledBorder("������ͼԪ/ģ������� "));
		nameInputPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(new PartialGradientLineBorder(
						new Color[] { new Color(0, 0, 128),
								UIDefaultsLookup.getColor("control") }, 2,
						PartialSide.NORTH), "������ͼԪ/ģ�������", TitledBorder.LEFT,
						TitledBorder.ABOVE_TOP), BorderFactory
						.createEmptyBorder(6, 4, 4, 4)));
		nameInputPanel.add(nameField, BorderLayout.CENTER);

		GridBagConstraints nameInputConstraints = new GridBagConstraints();
		nameInputConstraints.gridx = 0;
		nameInputConstraints.gridy = 2;
		nameInputConstraints.insets = new Insets(5, 3, 5, 3);
		nameInputConstraints.fill = GridBagConstraints.BOTH;
		rootLayout.setConstraints(nameInputPanel, nameInputConstraints);
		symbolTypeComboPanel = new JPanel();
		symbolTypeCombo = new JComboBox();
		symbolTypeComboPanel.setLayout(new BorderLayout());
		symbolTypeComboPanel.add(symbolTypeCombo, BorderLayout.CENTER);
		// symbolTypeCombo.setBorder(new TitledBorder("��ѡ��ͼԪ/ģ�����"));
		symbolTypeComboPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(new PartialGradientLineBorder(
						new Color[] { new Color(0, 0, 128),
								UIDefaultsLookup.getColor("control") }, 2,
						PartialSide.NORTH), "��ѡ��ͼԪ/ģ�����", TitledBorder.LEFT,
						TitledBorder.ABOVE_TOP), BorderFactory
						.createEmptyBorder(6, 4, 4, 4)));
		GridBagConstraints symbolTypeConstraints = new GridBagConstraints();
		symbolTypeConstraints.gridx = 0;
		symbolTypeConstraints.gridy = 3;
		symbolTypeConstraints.insets = new Insets(0, 5, 6, 0);
		symbolTypeConstraints.fill = GridBagConstraints.BOTH;
		rootLayout.setConstraints(symbolTypeComboPanel, symbolTypeConstraints);

		GridBagConstraints btnPanelConstraints = new GridBagConstraints();
		btnPanelConstraints.gridx = 0;
		btnPanelConstraints.gridy = 4;
		btnPanelConstraints.insets = new Insets(0, 5, 6, 0);
		btnPanelConstraints.fill = GridBagConstraints.BOTH;
		rootLayout.setConstraints(btnPanel, btnPanelConstraints);

		this.add(treePanel);
		this.add(statusPanel);
		this.add(nameInputPanel);
		this.add(btnPanel);
		this.add(symbolTypeComboPanel);
	}

	/**
	 * �������ϵ�����
	 */
	public void clear() {
		modelTree.setModel(null);
		nameField.setText("");
		filterField.setText("");
		((DefaultComboBoxModel) statusCombo.getModel()).removeAllElements();
		okBtn.setEnabled(false);
	}

	// public void addRootMenu()

	/**
	 * ��ȡѡ�����ģ�ͽڵ��UserObject����
	 * 
	 * @return
	 */
	public Object getSelectedModel() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelTree
				.getLastSelectedPathComponent();
		if (node != null) {
			return node.getUserObject();
		}
		return null;
	}

	/**
	 * ��ȡѡ����״̬
	 * 
	 * @return ״̬����
	 */
	public Object getSelectedStatus() {
		Object o = null;
		if (statusCombo != null && statusCombo.isVisible()) {
			o = statusCombo.getModel().getSelectedItem();
		}
		return o;
	}

	/**
	 * ��ȡ��ȷ������ť
	 * 
	 * @return okBtn��ȷ������ť
	 */
	public JButton getOkBtn() {
		return okBtn;
	}

	/**
	 * ��ȡ"�ر�"��ť
	 * 
	 * @return cancelBtn���رա���ť
	 */
	public JButton getCancelBtn() {
		return cancelBtn;
	}

	/**
	 * ��ȡģ���б���
	 * 
	 * @return ģ���б���
	 */
	public JTree getModelTree() {
		return modelTree;
	}

	/**
	 * ��ȡģ��״̬ComboBox
	 * 
	 * @return ģ��״̬ComboBox
	 */
	public JComboBox getStatusCombo() {
		return statusCombo;
	}

	/**
	 * ��ȡ���������������ڵ�Panel
	 * 
	 * @return ���������������ڵ�Panel
	 */
	public JPanel getNameInputPanel() {
		return nameInputPanel;
	}

	/**
	 * ��ȡ�������������JTextField����
	 * 
	 * @return �������������JTextField����
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * ��ȡͼԪ��ģ��С���Combobox����
	 * 
	 * @return ͼԪ��ģ��С���Combobox����
	 */
	public JComboBox getSymbolTypeCombo() {
		return symbolTypeCombo;
	}

	public JPanel getSymbolTypeComboPanel() {
		return symbolTypeComboPanel;
	}

	public AutoCompletionField getFilterField() {
		return filterField;
	}

	public int getSymbolType() {
		return symbolType;
	}

	public void setSymbolType(int symbolType) {
		this.symbolType = symbolType;
	}

	public JMenuItem getRefreshItem() {
		return refreshItem;
	}

	public JPanel getStatusPanel() {
		return statusPanel;
	}

}
