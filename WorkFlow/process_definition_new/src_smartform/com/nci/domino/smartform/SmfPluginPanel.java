package com.nci.domino.smartform;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.swing.JideSplitButton;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.plugin.smartform.FormBOAttributeBean;
import com.nci.domino.beans.plugin.smartform.FormBizObjectBean;
import com.nci.domino.components.WfArgumentsPanel;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.domain.WofoBaseDomain;
import com.nci.domino.help.Functions;

/**
 * ���������
 * 
 * @author Qil.Wong
 * 
 */
public abstract class SmfPluginPanel extends WfDialogPluginPanel {

	public SmfPluginPanel(WfDialog dialog) {
		super(dialog);
		init();
		// ��Ϊ����˳��δ֪������ȴ������塱�������ʼ�����
		Timer t = new Timer(1500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initArgmentImport();
			}
		});
		t.setRepeats(false);
		t.start();
	}

	protected abstract void init();

	/**
	 *��ʼ����������ı�����
	 */
	protected void initArgmentImport() {
		JTabbedPane tab = (JTabbedPane) this.getParent();
		for (int i = 0; i < tab.getTabCount(); i++) {
			Component c = tab.getComponentAt(i);
			if (c instanceof WfArgumentsPanel) {
				final WfArgumentsPanel argPanel = (WfArgumentsPanel) c;
				final JideSplitButton importBtn = argPanel.getImportBtn();
				ActionListener l = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showFormArgsImportChooser(importBtn, argPanel
								.getArgumentsTable());
					}
				};
				JMenuItem item = new JMenuItem("���������");
				importBtn.add(item);
				item.addActionListener(l);
				if (importBtn.getActionListeners() == null
						|| importBtn.getActionListeners().length == 0) {
					importBtn.addActionListener(l);
				}
				break;
			}
		}
	}

	/**
	 * ��ʾ��������������˵�
	 * @param relative
	 * @param valueToSet
	 */
	protected void showFormArgsImportChooser(Component relative,
			final JTable valueToSet) {
		final SmfFormArgsImportChooser chooser = new SmfFormArgsImportChooser(
				dialog);
		final WofoProcessBean process = getProcessBean();
		chooser.addConfirmListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<FormBizObjectBean, Object[]> selectValues = chooser
						.getSelectedValues();
				DefaultTableModel model = (DefaultTableModel) valueToSet
						.getModel();
				Iterator<FormBizObjectBean> keys = selectValues.keySet()
						.iterator();
				while (keys.hasNext()) {
					FormBizObjectBean formBizObj = keys.next();
					Object[] os = selectValues.get(formBizObj);
					for (Object o : os) {
						FormBOAttributeBean attr = (FormBOAttributeBean) o;
						WofoArgumentsBean arg = new WofoArgumentsBean(Functions
								.getUID());
						arg.setArgName(attr.getAttributeName());
						arg.setArgType(WofoArgumentsBean.ARG_TYPE_STRING);
						arg.setEditable(false);
						// arg.setDefaultValue(attr.g)
						arg.setFollowField(formBizObj.getBoCode() + "."
								+ attr.getAttributeCode());
						arg.setSqlRead("SmartForm");
						arg.setSqlWrite("SmartForm");
						arg.setPackageId(process.getPackageId());
						arg.setProcessObjId(process.getID());
						arg.setProcessObjType(WofoBaseDomain.OBJ_TYPE_PROCESS);
						model.addRow(new Object[] { model.getRowCount() + 1,
								arg.getArgName(), arg.getArgType(),
								arg.getDefaultValue(), arg.getFollowField(),
								arg.getSqlRead(), arg.getSqlWrite(), arg });
					}
				}
			}
		});
		chooser.showChooser(relative, process);
	}

	protected abstract WofoProcessBean getProcessBean();

}
