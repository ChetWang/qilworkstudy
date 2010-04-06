package com.nci.svg.client.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jdesktop.swingworker.SwingWorker;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.logntermtask.LongtermTask;
import com.nci.svg.sdk.logntermtask.LongtermTaskManager;
import com.nci.svg.sdk.module.ScreenCastModuleAdapter;

import fr.itris.glips.library.PreferencesStore;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.managers.export.FileExport;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;

public class NCIScreenCastModule extends ScreenCastModuleAdapter {

	private JToggleButton btn;

	public static String ScreenCastModuleID = "nciScreenCast";

	private static boolean toClipboardInfoPrompt = true;

	public static String toClipboardInfoPromptID = "toClipboardInfoPrompt";

	public static String SHOW_PROMPT = "0";
	public static String HIDE_PROMPT = "1";

	public NCIScreenCastModule(EditorAdapter editor) {
		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initModule();
			}
		});
	}
	
	private void initModule(){
		btn = new JToggleButton();
		btn.setEnabled(false);
		HandlesListener svgHandlesListener = new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				if (handles.size() == 0)
					btn.setEnabled(false);
				else
					btn.setEnabled(true);
			}

		};
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
		String regeditTOPrompt = PreferencesStore.getPreference(null,
				toClipboardInfoPromptID);
		if (regeditTOPrompt == null || regeditTOPrompt.equals("")
				|| regeditTOPrompt.equals(SHOW_PROMPT)) {
			toClipboardInfoPrompt = true;
		} else {
			toClipboardInfoPrompt = false;
		}
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		btn.setToolTipText("截取区域中图形");
		ImageIcon screenCastIcon = ResourcesManager.getIcon(ScreenCastModuleID,
				false);
		btn.setIcon(screenCastIcon);
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				notifyCastMode();
			}

		});
		HashMap<String, AbstractButton> toolItems = new HashMap<String, AbstractButton>();
		toolItems.put(ScreenCastModuleID, btn);
		return toolItems;
	}

	/**
	 * 激活截图模式
	 */
	private void notifyCastMode() {
		editor.getSelectionManager().setSelectionMode(
				SelectionInfoManager.SCREEN_CAST_MODE, null);

	}

	/**
	 * 截图
	 * 
	 * @param svgHandle
	 *            截图区域所在的SVGHandle对象
	 * @param castRect
	 *            截图的区域
	 * @deprecated
	 */
	public void castScreen(SVGHandle svgHandle, Rectangle2D castRect) {
		editor.getSelectionManager().setScreenCastRect(castRect.getBounds());
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				try {
					editor.getIOManager().getFileExportManager().export(
							editor.getHandlesManager().getCurrentHandle(),
							FileExport.JPG_FORMAT,
							editor.getHandlesManager().getCurrentHandle()
									.getCanvas());
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				return null;
			}
		};
		LongtermTask lt = new LongtermTask("正在计算图像...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(lt);
	}

	public String getName() {
		return ScreenCastModuleID;
	}

	/**
	 * 提示消息的面板对象
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class PromptInfo extends JPanel {
		JLabel label = null;
		JCheckBox check = null;

		public PromptInfo() {
			super();
			label = new JLabel(ResourcesManager.bundle
					.getString("nci_screencast_copy_to_clipboard"));
			check = new JCheckBox(ResourcesManager.bundle
					.getString("nci_screencast_nolonger_apper"));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(label);
			this.add(check);
		}
	}

	
	public void showPromptInfo() {
		if (toClipboardInfoPrompt) {
			PromptInfo info = new PromptInfo();
			int result = JOptionPane.showConfirmDialog(
					editor.findParentFrame(), info, ResourcesManager.bundle
							.getString("nci_optionpane_infomation_title"),
					JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (result == JOptionPane.OK_OPTION && info.check.isSelected()) {
				PreferencesStore.setPreference(null, toClipboardInfoPromptID,
						HIDE_PROMPT);
				toClipboardInfoPrompt = false;
			}
		}
	}
}
