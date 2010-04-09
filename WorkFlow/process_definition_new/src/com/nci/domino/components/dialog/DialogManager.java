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
 * 流程定义器对话框管理器，管理各种可能弹出的对话框，好处是不需要再点到时进行初始化，这样用户感受较差；缺点是可能会占用较多内存
 * 
 * @author Qil.Wong
 * 
 */
public class DialogManager {

	private WfEditor editor;

	/**
	 * 对话框缓存对象
	 */
	private Map<Class<? extends WfDialog>, WfDialog> dialogs = new HashMap<Class<? extends WfDialog>, WfDialog>();

	// private Map<Class<ActivityType>, Class<? extends WfDialog>> acitvityMap =
	// new HashMap<Class<ActivityType>, Class<? extends WfDialog>>();

	/**
	 * 对话框管理器构造函数
	 * 
	 * @param editor
	 *            WfEditor对象
	 */
	public DialogManager(WfEditor editor) {
		this.editor = editor;
		WfRunnable dialogCreateRun = new WfRunnable("正在初始化组件") {
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
	 * 根据对话框类型创建对话框
	 * 
	 * @param wfDialogClass
	 *            对话框类型
	 * @param title
	 *            对话框标题
	 * @param modal
	 *            是否模态
	 * @return WfDialog对话框
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
