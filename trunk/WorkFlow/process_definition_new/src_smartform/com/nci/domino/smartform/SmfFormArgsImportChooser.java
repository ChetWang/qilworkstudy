package com.nci.domino.smartform;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.plugin.smartform.FormBizObjectBean;
import com.nci.domino.beans.plugin.smartform.ProcessFormConfigBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.tree.WfBusyTextOverlayable;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * ����������ѡ����
 * 
 * @author Qil.Wong
 * 
 */
public class SmfFormArgsImportChooser extends JPanel {

	protected JideTabbedPane tab = new JideTabbedPane();
	protected JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
	protected List<PopupMenuListener> popupListeners = new Vector<PopupMenuListener>();
	protected WfBusyTextOverlayable overlayScroll;
	private String defaultStyleText = "{\n���ڻ�ȡ����������:f:gray}";

	protected WfDialog dialog;

	private JidePopup popup;

	public SmfFormArgsImportChooser(WfDialog dialog) {
		this.dialog = dialog;
		init();
	}

	/**
	 * ��ʼ��
	 */
	protected void init() {
		setLayout(new BorderLayout());
		overlayScroll = new WfBusyTextOverlayable(tab, defaultStyleText);
		add(overlayScroll, BorderLayout.CENTER);
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());
		btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		btnPanel.add(new JPanel(), BorderLayout.CENTER);
		btnPanel.add(okBtn, BorderLayout.EAST);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.hidePopup(false);
			}
		});
		add(btnPanel, BorderLayout.SOUTH);
	}

	/**
	 * ��ʾѡ����
	 */
	public void showChooser(Component relative, WofoProcessBean processBean) {
		tab.setTabPlacement(JideTabbedPane.LEFT);
		popup = new JidePopup();
		popup.setPreferredSize(new Dimension(200, 280));
		overlayScroll.setOverlayVisible(false);
		popup.setMovable(true);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(this, BorderLayout.CENTER);
		for (PopupMenuListener l : popupListeners) {
			popup.addPopupMenuListener(l);
		}
		final ProcessFormConfigBean processForm = (ProcessFormConfigBean) processBean
				.getPluginProps().get(ProcessFormConfigBean.class.getName());// ���̹����ı�����
		if (processForm != null) {
			if (processForm.getBusinessObjects().size() == 0) {
				// ��δ��ȡ��ҵ��������ԣ�������������Ҫ�ӷ�������ȡ
				overlayScroll.setOverlayVisible(true);
				final WfSwingWorker<List<FormBizObjectBean>, Object> worker = new WfSwingWorker<List<FormBizObjectBean>, Object>(
						"���ڻ�ȡ����������") {

					@Override
					protected List<FormBizObjectBean> doInBackground()
							throws Exception {
						WofoNetBean netBean = new WofoNetBean(
								WofoActions.GET_BO_ATTRIBUTES, dialog
										.getEditor().getUserID(), processForm
										.getFormId());
						netBean = Functions.getReturnNetBean(dialog.getEditor()
								.getServletPath(), netBean);
						return (List<FormBizObjectBean>) netBean.getParam();
					}

					@Override
					public void wfDone() {
						try {
							List<FormBizObjectBean> result = get();
							if (result != null && result.size() > 0) {
								overlayScroll.setOverlayVisible(false);
								processForm.getBusinessObjects().addAll(result);
								setValueIfExist(processForm
										.getBusinessObjects());
							} else {
								overlayScroll.setOverlayVisible(true);
								overlayScroll.getProgressPanel().setVisible(
										false);
								overlayScroll
										.setBusyText("{\n�޹����ı���������ҵ������:f:gray}");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				worker.setCancelListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialog.getEditor().getStatusBar().stopShowInfo(worker.getTipInfo());
					}
				});
				popup.addPopupMenuListener(new PopupMenuListener() {

					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

					}

					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
						if (worker != null && !worker.isDone()) {
							worker.cancelWorking(true);
						}
					}

					public void popupMenuCanceled(PopupMenuEvent e) {

					}
				});
				dialog.getEditor().getBackgroundManager().enqueueOpertimeQueue(
						worker);
			} else {
				setValueIfExist(processForm.getBusinessObjects());
			}
		} else {
			// ���û�б�����˵���޹���
			overlayScroll.setOverlayVisible(true);
			overlayScroll.getProgressPanel().setVisible(false);
			overlayScroll.setBusyText("{\n�޹���������:f:gray}");
		}
		popup.showPopup(relative);
	}

	/**
	 * ��ѡ������ֵ
	 * @param valus
	 */
	private void setValueIfExist(List<FormBizObjectBean> valus) {
		for (FormBizObjectBean formBiz : valus) {
			CheckBoxList list = new CheckBoxList(new DefaultListModel());
			DefaultListModel model = (DefaultListModel) list.getModel();
			for (Object c : formBiz.getAttributes())
				model.addElement(c);
			tab.addTab(formBiz.getBoName(), new JideScrollPane(list));
			list.putClientProperty(FormBizObjectBean.class.getName(), formBiz);
		}
	}

	/**
	 * ���ѡ���ļ���
	 * 
	 * @param l
	 */
	public void addConfirmListener(ActionListener l) {
		okBtn.addActionListener(l);
	}

	public void addPopupMenuListener(PopupMenuListener popupListener) {
		popupListeners.add(popupListener);
	}

	public Map<FormBizObjectBean, Object[]> getSelectedValues() {
		Map<FormBizObjectBean, Object[]> map = new LinkedHashMap<FormBizObjectBean, Object[]>();
		for (int i = 0; i < tab.getTabCount(); i++) {
			JideScrollPane scroll = (JideScrollPane) tab.getComponentAt(i);
			CheckBoxList list = (CheckBoxList) scroll.getViewport().getView();
			map.put((FormBizObjectBean) list
					.getClientProperty(FormBizObjectBean.class.getName()), list
					.getCheckBoxListSelectedValues());
		}
		return map;
	}

}
