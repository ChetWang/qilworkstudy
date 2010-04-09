package com.nci.domino.components.dialog.condition;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.beans.desyer.WofoConditionBean;
import com.nci.domino.beans.desyer.WofoConditionMemberBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfComboBox;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.WfComboBox.WfComboBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.table.tablesorter.DefaultSimpleTableSortor;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

public class WfNewConditionGroupDialog extends WfDialog {

	public WfNewConditionGroupDialog(WfEditor editor, String title,
			boolean modal) {
		super(editor, title, modal);
		defaultWidth = 500;
		defaultHeight = 370;
	}

	@Override
	protected String checkInput() {
		if (basicPanel.conditionNameField.getText().trim().equals("")) {
			return "\n必须指定条件名称";
		}
		List<?> conditions = editor.getOperationArea().getCurrentProcess()
				.getConditions();
		for (Object o : conditions) {
			if (basicPanel.conditionNameField.getText().equals(o.toString())) {
				return "\n存在相同的条件："+o;
			}
		}
		return super.checkInput();
	}

	@Override
	protected void clearContents() {
		super.clearContents();
		((JideTabbedPane) getContentPanel()).setSelectedIndex(0);
		basicPanel.reset();
		memberPanel.reset();
	}

	@Override
	public Serializable getInputValues() {
		WofoConditionBean condition = (WofoConditionBean) super
				.getInputValues();
		if (condition == null) {
			condition = new WofoConditionBean(Functions.getUID());
		}
		basicPanel.applyValues(condition);
		memberPanel.applyValues(condition);
		return condition;
	}

	@Override
	public void setInputValues(Serializable value) {
		super.setInputValues(value);
		basicPanel.setValues(value);
		memberPanel.setValues(value);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("流程条件",
				"用于分类当前流程包下的所有适用条件", Functions.getImageIcon("41.gif"));
		return headerPanel1.getGlassBanner();
	}

	// 基本属性面板
	BasicContentPanel basicPanel;

	ConditionMembersPanel memberPanel;

	@Override
	public JComponent createContentPanel() {
		basicPanel = new BasicContentPanel();
		setInitFocusedComponent(basicPanel.conditionNameField);
		memberPanel = new ConditionMembersPanel();
		contentTab.addTab("属性", null, basicPanel);
		contentTab.addTab("值", null, memberPanel);
		return contentTab;
	}

	private class BasicContentPanel extends WfInputPanel {

		JLabel conditionNameLabel = new JLabel("条件名称：");
		WfOverlayableTextField conditionNameField;
		JLabel conditionTypeLabel = new JLabel("条件类型：");
		WfComboBox conditionTypeCombo = new WfComboBox(false);
		JLabel conditionDescLabel = new JLabel("说明：");
		JTextArea descArea;
		JLabel expressLabel = new JLabel("表达式：");
		JTextArea expressArea;

		public BasicContentPanel() {
			init();
		}

		private void init() {
			WfTextDocument nameDoc = new WfTextDocument(38);
			conditionNameField = new WfOverlayableTextField(nameDoc);
			WfTextDocument descDoc = new WfTextDocument(200);
			descArea = new JTextArea(descDoc);
			WfTextDocument expressDoc = new WfTextDocument(2000);
			expressArea = new JTextArea(expressDoc);
			descArea.setAutoscrolls(true);
			expressArea.setAutoscrolls(true);
			nameDoc.setWfDialog(WfNewConditionGroupDialog.this);
			descDoc.setWfDialog(WfNewConditionGroupDialog.this);
			expressDoc.setWfDialog(WfNewConditionGroupDialog.this);
			JideScrollPane descScrollPane = new JideScrollPane(descArea);
			JideScrollPane expressScrollPane = new JideScrollPane(expressArea);

			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 0;
			cons.insets = new Insets(5, 5, 5, 5);
			cons.fill = GridBagConstraints.BOTH;
			add(this.conditionNameLabel, cons);

			cons.gridx = 1;
			add(this.conditionNameField.getOverlayableTextField(), cons);

			cons.gridx = 0;
			cons.gridy = 1;
			add(this.conditionTypeLabel, cons);

			cons.gridx = 1;
			add(this.conditionTypeCombo, cons);

			cons.gridx = 0;
			cons.gridy = 2;
			add(this.conditionDescLabel, cons);

			cons.gridx = 1;
			cons.weightx = 1;
			cons.weighty = 1;
			add(descScrollPane, cons);

			cons.gridx = 0;
			cons.gridy = 3;
			cons.weightx = 0;
			cons.weighty = 0;
			add(this.expressLabel, cons);

			cons.gridx = 1;
			cons.weightx = 1;
			cons.weighty = 1;
			add(expressScrollPane, cons);
			List<WfComboBean> comboBeans = new ArrayList<WfComboBean>();
			comboBeans.add(new WfComboBean(WofoConditionBean.TYPE_EXPRESSION,
					WofoConditionBean.TYPE_EXPRESSION_TITLE));
			comboBeans.add(new WfComboBean(WofoConditionBean.TYPE_PROCEDURE,
					WofoConditionBean.TYPE_PROCEDURE_TITLE));
			comboBeans.add(new WfComboBean(WofoConditionBean.TYPE_METHOD,
					WofoConditionBean.TYPE_METHOD_TITLE));
			comboBeans.add(new WfComboBean(WofoConditionBean.TYPE_CODE,
					WofoConditionBean.TYPE_CODE_TITLE));
			comboBeans.add(new WfComboBean(WofoConditionBean.TYPE_OTHER,
					WofoConditionBean.TYPE_OTHER_TITLE));
			conditionTypeCombo.resetElements(comboBeans, false);
			conditionTypeCombo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						expressLabel.setText(e.getItem().toString() + "：");
					}
				}
			});
			final JidePopupMenu popupMenu = new JidePopupMenu();
			JMenuItem importFromArgsItem = new JMenuItem("从参数导入");
			popupMenu.add(importFromArgsItem);
			expressArea.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3
							&& e.getClickCount() == 1
							&& conditionTypeCombo.getSelectedIndex() == 0) {
						popupMenu.show(expressArea, e.getX(), e.getY());
						popupMenu.putClientProperty("LocationPopAtDescArea",
								new Point(popupMenu.getLocationOnScreen().x,
										popupMenu.getLocationOnScreen().y));
					}
				}
			});
			importFromArgsItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					importExpressionFromArgs(popupMenu);
				}
			});
		}

		/**
		 * 从流程参数导入表达式
		 */
		private void importExpressionFromArgs(JidePopupMenu relative) {
			WofoProcessBean process = editor.getOperationArea()
					.getCurrentProcess();
			if (process != null) {
				List<WofoArgumentsBean> args = process.getArguments();
				final JidePopup pop = new JidePopup();
				final CheckBoxList list = new CheckBoxList(
						new DefaultListModel());
				JideScrollPane scroll = new JideScrollPane(list);
				for (WofoArgumentsBean arg : args) {
					((DefaultListModel) list.getModel()).addElement(arg);
				}
				JPanel buttonPanel = new JPanel(new BorderLayout());
				JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
				okBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Object[] values = list.getCheckBoxListSelectedValues();
						String exist = basicPanel.expressArea.getText();
						StringBuffer sb = new StringBuffer(exist);
						for (Object o : values) {
							sb.append("{").append(o.toString()).append("}");
						}
						basicPanel.expressArea.setText(sb.toString());
						pop.hidePopup();
					}
				});
				buttonPanel.add(okBtn, BorderLayout.EAST);
				pop.setMovable(true);
				pop.getContentPane().setLayout(new BorderLayout());
				pop.getContentPane().add(scroll, BorderLayout.CENTER);
				pop.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
				Point relativeLocation = (Point) relative
						.getClientProperty("LocationPopAtDescArea");
				pop.setPreferredSize(new Dimension(120, 150));
				pop.packPopup();
				pop.showPopup(relativeLocation.x, relativeLocation.y,
						basicPanel.descArea);
			}
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoConditionBean c = (WofoConditionBean) value;
			// c.setConditionCode(conditionCode)
			c.setConditionName(conditionNameField.getText().trim());
			WfComboBean typeComboBean = (WfComboBean) conditionTypeCombo
					.getSelectedItem();
			c.setConditionType(typeComboBean.id);
			c.setDescription(descArea.getText().trim());
			c.setExpression(expressArea.getText().trim());
			// c.setPackageId()
			c.setProcessId(editor.getOperationArea().getCurrentProcess()
					.getID());
			return c;
		}

		@Override
		public void reset() {
			this.conditionNameField.setText("");
			this.conditionTypeCombo.setSelectedIndex(0);
			this.descArea.setText("");
			this.expressArea.setText("");
		}

		@Override
		public void setValues(Serializable value) {
			WofoConditionBean bean = (WofoConditionBean) value;
			this.conditionNameField.setText(bean.getConditionName());
			String type = bean.getConditionType();

			this.conditionTypeCombo.setSelectedItemByID(type);
			this.descArea.setText(bean.getDescription());
			this.expressArea.setText(bean.getExpression());

		}

	}

	/**
	 * 条件值的属性面板
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class ConditionMembersPanel extends WfInputPanel {

		private JTable memberTable;

		private MyValueEditor cellEditor = new MyValueEditor();

		public ConditionMembersPanel() {
			init();
		}

		private void init() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 1;
			cons.insets = new Insets(0, 5, 0, 5);
			cons.fill = GridBagConstraints.BOTH;
			cons.weightx = 1;
			cons.weighty = 1;
			memberTable = new JTable();
			memberTable.setModel(new javax.swing.table.DefaultTableModel(
					new Object[][] { {} }, new String[] { "", "名称", "值" }) {
				Class[] types = new Class[] { Integer.class, String.class,
						MemberValueInputComponent.class };
				boolean[] canEdit = new boolean[] { false, true, true };

				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			});
			JideScrollPane scroll = new JideScrollPane(memberTable);
			add(scroll, cons);

			WfOperControllerButtonPanel tableOperTool = new WfOperControllerButtonPanel(
					memberTable, true) {

				@Override
				public Object[] getOneRowEmptyData() {
					Object[] data = new Object[] { memberTable.getRowCount(),
							"", new MemberValueInputComponent() };
					return data;
				}
			};

			cons.gridx = 0;
			cons.gridy = 0;
			cons.anchor = GridBagConstraints.WEST;
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.weightx = 0;
			cons.weighty = 0;
			add(tableOperTool, cons);

			memberTable.getColumnModel().getColumn(0).setPreferredWidth(20);
			memberTable.getColumnModel().getColumn(1).setPreferredWidth(60);
			memberTable.getColumnModel().getColumn(2).setPreferredWidth(320);
			// memberTable.getColumnModel().getColumn(3).setPreferredWidth(20);
			// DefaultTableModel model = (DefaultTableModel) memberTable
			// .getModel();
			removeAllRows();
			new DefaultSimpleTableSortor(memberTable).initSortHeader();
			// memberTable
			// .setRowSorter(new TableRowSorter(memberTable.getModel()));
			memberTable.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			TableColumn valueCol = memberTable.getColumnModel().getColumn(2);
			valueCol.setCellRenderer(new MemberValueInputComponent());
			valueCol.setCellEditor(cellEditor);
		}

		@Override
		public Serializable applyValues(Serializable value) {
			Functions.stopEditingCells(memberTable);
			WofoConditionBean conditionBean = (WofoConditionBean) value;
			DefaultTableModel model = (DefaultTableModel) memberTable
					.getModel();
			List<WofoConditionMemberBean> members = new ArrayList<WofoConditionMemberBean>();
			int rows = model.getRowCount();
			for (int i = 0; i < rows; i++) {
				WofoConditionMemberBean memberBean = new WofoConditionMemberBean(
						Functions.getUID());
				memberBean.setConditionId(conditionBean.getConditionId());
				memberBean.setMemberName((String) model.getValueAt(i, 1));
				MemberValueInputComponent valueInput = (MemberValueInputComponent) model
						.getValueAt(i, 2);
				memberBean.setMemberValue(valueInput.getValue());
				members.add(memberBean);
			}

			conditionBean.setMembers(members);
			return conditionBean;
		}

		@Override
		public void reset() {
			removeAllRows();
		}

		/**
		 * 清空表格所有数据
		 */
		private void removeAllRows() {
			DefaultTableModel model = (DefaultTableModel) memberTable
					.getModel();
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
		}

		@Override
		public void setValues(Serializable value) {
			removeAllRows();
			WofoConditionBean conditionBean = (WofoConditionBean) value;
			DefaultTableModel model = (DefaultTableModel) memberTable
					.getModel();
			List<WofoConditionMemberBean> members = conditionBean.getMembers();
			for (int i = 0; i < members.size(); i++) {
				model.addRow(new Object[] {
						i + 1,
						members.get(i).getMemberName(),
						new MemberValueInputComponent(members.get(i)
								.getMemberValue()) });
			}
		}

	}

	private class MemberValueInputComponent extends JPanel implements
			TableCellRenderer {

		JTextField valueField;

		JideButton chooserBtn = new JideButton("..");

		public MemberValueInputComponent() {
			init();
		}

		public MemberValueInputComponent(String txt) {
			init();
			valueField.setText(txt);
		}

		private void init() {
			WfTextDocument doc = new WfTextDocument(2000);
			valueField = new JTextField();
			valueField.setDocument(doc);

			doc.setWfDialog(WfNewConditionGroupDialog.this);

			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 0;
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.weightx = 1;
			add(valueField, cons);
			cons.gridx = 1;
			cons.weightx = 0;
			add(chooserBtn, cons);

			chooserBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// showPopup();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							showPopup();
						}
					});
				}
			});
		}

		private void updateDocument(JTextArea valueArea) {
			valueField.setText(valueArea.getText());
			memberPanel.memberTable.updateUI();
		}

		/**
		 * 显示弹出的TextArea，用于详细写入表达式的值
		 */
		public void showPopup() {
			WfTextDocument doc2 = new WfTextDocument(2000);
			final JTextArea valueArea = new JTextArea(doc2);
			valueArea.setAutoscrolls(true);
			valueArea.setLineWrap(true);
			doc2.setWfDialog(WfNewConditionGroupDialog.this);
			doc2.addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					updateDocument(valueArea);
				}

				public void insertUpdate(DocumentEvent e) {
					updateDocument(valueArea);
				}

				public void changedUpdate(DocumentEvent e) {
					updateDocument(valueArea);
				}
			});
			JideScrollPane scroll = new JideScrollPane(valueArea);
			JidePopup popup = new JidePopup();
			popup.setPreferredSize(new Dimension(200, 200));
			popup.getContentPane().setLayout(new BorderLayout());
			popup.getContentPane().add(scroll, BorderLayout.CENTER);
			popup.setDefaultFocusComponent(valueArea);
			valueArea.setText(valueField.getText());
			popup.showPopup(chooserBtn);
			memberPanel.updateUI();
		}

		/**
		 * 设置值，用于在表格显示
		 * 
		 * @param txt
		 */
		public void setValue(String txt) {
			valueField.setText(txt);
		}

		/**
		 * 获取该单元格的显示的值
		 * 
		 * @return
		 */
		public String getValue() {
			return valueField.getText();
		}

		public String toString() {
			return getValue();
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (isSelected) {
				this.setForeground(table.getSelectionForeground());
				this.setBackground(table.getSelectionBackground());
				valueField.setForeground(table.getSelectionForeground());
				valueField.setBackground(table.getSelectionBackground());
			} else {
				this.setForeground(table.getForeground());
				this.setBackground(table.getBackground());
				valueField.setForeground(table.getForeground());
				valueField.setBackground(table.getBackground());
			}
			valueField.setText(value.toString());
			return this;
		}
	}

	/**
	 * 表格的值编辑器
	 * 
	 * @author Qil.Wong
	 * 
	 */
	public class MyValueEditor extends AbstractCellEditor implements
			TableCellEditor {
		JComponent comp = null;

		public MyValueEditor() {

		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			comp = (JComponent) model.getValueAt(row, column);
			return comp;
		}

		public Object getCellEditorValue() {
			return comp;
		}

		public boolean isCellEditable(EventObject evt) {
			if (evt instanceof MouseEvent) {
				MouseEvent e = (MouseEvent) evt;
				if (e.getClickCount() == 2) {
					return true;
				}
			}
			return false;
		}
	}
}
