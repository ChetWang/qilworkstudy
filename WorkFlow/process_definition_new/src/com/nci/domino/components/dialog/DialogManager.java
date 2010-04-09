package com.nci.domino.components.dialog;

import java.util.HashMap;
import java.util.Map;

import com.nci.domino.WfEditor;
import com.nci.domino.components.dialog.activity.WfNewActivityHumanDialog;
import com.nci.domino.components.dialog.importexport.LocalExportChooserDialog;
import com.nci.domino.components.dialog.process.WfNewProcessDialog;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.concurrent.WfStartupEndException;
import com.nci.domino.help.WofoResources;

/**
 * ���̶������Ի����������������ֿ��ܵ����ĶԻ��򣬺ô��ǲ���Ҫ�ٵ㵽ʱ���г�ʼ���������û����ܽϲȱ���ǿ��ܻ�ռ�ý϶��ڴ�
 * 
 * @author Qil.Wong
 * 
 */
public class DialogManager {

	private WfEditor editor;

	/**
	 * �Ի��򻺴����
	 */
	private Map<Class<? extends WfDialog>, WfDialog> dialogs = new HashMap<Class<? extends WfDialog>, WfDialog>();

	// private Map<Class<ActivityType>, Class<? extends WfDialog>> acitvityMap =
	// new HashMap<Class<ActivityType>, Class<? extends WfDialog>>();

	/**
	 * �Ի�����������캯��
	 * 
	 * @param editor
	 *            WfEditor����
	 */
	public DialogManager(WfEditor editor) {
		this.editor = editor;
		WfRunnable dialogCreateRun = new WfRunnable("���ڳ�ʼ�����") {
			public void run() {
				getDialog(LocalExportChooserDialog.class, "", true);
				// getDialog(PngExportChooserDialog.class, "", true);
				// getDialog(LocalImportChooserDialog.class, "", true);
				// getDialog(WfNewPackageDialog.class, "", true);
				 getDialog(WfNewProcessDialog.class, "", true);
				// getDialog(WfNewProcessSetDialog.class, "", true);
				 getDialog(WfNewActivityHumanDialog.class, "", true);
				// getDialog(WfNewActivityStartEndDialog.class, "", true);
				// getDialog(WfNewActivityJoinDialog.class, "", true);
				// getDialog(WfNewActivitySplitDialog.class, "", true);
				// getDialog(WfNewActivityAutoDialog.class, "", true);
				// getDialog(WfNewActivitySubflowDialog.class, "", true);
				// getDialog(WfNewConditionGroupDialog.class, "", true);
			}
		};
		try {
			editor.getBackgroundManager().enqueueStartupQueue(dialogCreateRun);
		} catch (WfStartupEndException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ݶԻ������ʹ����Ի���
	 * 
	 * @param wfDialogClass
	 *            �Ի�������
	 * @param title
	 *            �Ի������
	 * @param modal
	 *            �Ƿ�ģ̬
	 * @return WfDialog�Ի���
	 */
	public WfDialog getDialog(Class<? extends WfDialog> wfDialogClass,
			String title, boolean modal) {
		if (title != null && !title.equals("")) {
			editor.getStatusBar().showGlassInfo(
					WofoResources.getValueByKey("choose") + title);
		}
		WfDialog dialog = dialogs.get(wfDialogClass);
		if (dialog == null) {
			Class<?>[] classargs = { WfEditor.class, String.class,
					boolean.class };
			Object[] args = { editor, title, modal };
			try {
				dialog = wfDialogClass.getConstructor(classargs).newInstance(
						args);
				dialog.pack();
				dialogs.put(wfDialogClass, dialog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (!"".equals(title)) {
				dialog.setTitle(title);
			}
			dialog.setModal(modal);
		}
		return dialog;
	}

}
