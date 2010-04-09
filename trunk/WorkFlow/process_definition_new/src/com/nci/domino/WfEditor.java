package com.nci.domino;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.SystemInfo;
import com.nci.domino.cache.WfCacheManager;
import com.nci.domino.components.dialog.DialogManager;
import com.nci.domino.components.operation.WfOperationArea;
import com.nci.domino.components.statusbar.WfStatusBar;
import com.nci.domino.components.toolbar.WfToolBar;
import com.nci.domino.components.tree.popup.PopupManager;
import com.nci.domino.concurrent.WfBackgroundRunner;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.edit.CopyPaste;
import com.nci.domino.edit.ModeManager;
import com.nci.domino.help.Functions;
import com.nci.domino.importexport.ImportExportUtilities;

/**
 * 
 * @author Qil.Wong
 */
public class WfEditor extends JPanel {

	/**
	 * ������
	 */
	private WfToolBar toolBar = null;
	/**
	 * ״̬��
	 */
	private WfStatusBar statusBar = null;
	/**
	 * ������
	 */
	private WfOperationArea operationArea = null;
	/**
	 * �����˵�������
	 */
	private PopupManager popupManager = null;
	// /**
	// * �˵���
	// */
	// private WfMenuBar menubar = null;
	/**
	 * Frame�����
	 */
	private Frame parentContainer;
	/**
	 * �Ի��������
	 */
	private DialogManager dialogManager;

	/**
	 * ��̨����������
	 */
	private WfBackgroundRunner backgroundManager;

	private ModeManager modeManager;

	private CopyPaste copyPaste;

	private WfCacheManager cache;

	public WfEditor() {
	}

	/**
	 * ��ʼ���༭��
	 */
	public void init() {
		System.out.println("SERVLET_PATH:" + getServletPath());
		System.out.println("USER_ID:" + getUserID());
		initLookAndFeel();
		if (SystemInfo.isJdk6Above()) {
			setUIFont();
		}
		backgroundManager = new WfBackgroundRunner(this);

		parentContainer = findParentFrame();
		System.out.println(parentContainer.getClass());
		parentContainer.setIconImage(Functions.getImageIcon("workflow.gif")
				.getImage());
		statusBar = new WfStatusBar(this);
		toolBar = new WfToolBar(this);
		cache = new WfCacheManager(this);
		operationArea = new WfOperationArea(this);
		// menubar = new WfMenuBar(this);
		BorderLayout rootLayout = new BorderLayout();
		JPanel root = new JPanel();
		root.setLayout(rootLayout);
		setLayout(new BorderLayout());
		add(root, BorderLayout.CENTER);

		root.add(toolBar, BorderLayout.NORTH);
		root.add(statusBar, BorderLayout.SOUTH);
		operationArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 4));
		root.add(operationArea, BorderLayout.CENTER);

		statusBar.showGlassInfo("��ʼ��");

		popupManager = new PopupManager(this);
		dialogManager = new DialogManager(this);
		modeManager = new ModeManager(this);
		copyPaste = new CopyPaste();
	}

	/**
	 * ��applet������JFrame
	 */
	public void openJFrameOnApplet() {
		final JApplet applet = (JApplet) getClientProperty("appletOrFrame");
		statusBar.getExtBtn().setVisible(false);
		final JFrame f = new JFrame("WorkFlow");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		applet.getContentPane().remove(this);
		applet.repaint();
		f.getContentPane().add(this);
		f.setIconImage(Functions.getImageIcon("workflow.gif").getImage());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.pack();
		f.setLocationByPlatform(true);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				f.getContentPane().remove(WfEditor.this);
				applet.getContentPane().add(WfEditor.this, BorderLayout.CENTER);
				WfEditor.this.getStatusBar().getExtBtn().setVisible(true);
				applet.repaint();
			}
		});
	}

	/**
	 * ��ʼ�����
	 */
	private void initLookAndFeel() {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
			UIManager.put("ClassLoader", WfEditor.class.getClassLoader());
			LookAndFeelFactory
					.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
		} catch (Exception ex) {
			try {
				UIManager.setLookAndFeel(new MetalLookAndFeel());
			} catch (UnsupportedLookAndFeelException e) {
			}
			ex.printStackTrace();
		}
	}

	/**
	 * ��������
	 * 
	 * @param f
	 */
	private void setUIFont() {
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				FontUIResource oldValue = (FontUIResource) value;
				// oldValue.
				Font newF = oldValue.deriveFont(oldValue.getStyle(), oldValue
						.getSize() == 11 ? 12 : oldValue.getSize());
				UIManager.put(key, new FontUIResource(newF));
			}
		}
	}

	/**
	 * ��ȡeditor����Frame���
	 */
	public Frame getContainerFrame() {
		return parentContainer;
	}

	public Frame findParentFrame() {
		Container c = getParent();
		while (c != null) {
			if (c instanceof Frame) {
				((Frame) c).setIconImage(Functions.getImageIcon("workflow.gif")
						.getImage());
				return (Frame) c;
			}
			c = c.getParent();
		}
		return (Frame) null;
	}

	/**
	 * ����ǰ�������̶��󱣴浽����
	 */
	public void saveLocal(final String filePath, final Serializable obj) {
		WfSwingWorker<File, String> worker = new WfSwingWorker<File, String>(
				"���ڱ���������") {

			@Override
			protected File doInBackground() throws Exception {

				DefaultTreeModel model = operationArea.getWfTree()
						.getWfTreeModel();
				File f = new File(filePath);
				ImportExportUtilities.saveLocal(f, obj);
				return f;
			}

			@Override
			public void wfDone() {
				try {
					File f = get();
					getStatusBar().showGlassInfo("�ѱ�������" + f.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		getBackgroundManager().enqueueOpertimeQueue(worker);
	}

	/**
	 * �ӱ����ļ�����
	 * 
	 * @param filePath
	 *            ���صĸ�ʽ���ļ�
	 */
	public void importFromLocal(String filePath) {
		final File f = new File(filePath);
		WfSwingWorker<DefaultTreeModel, String> worker = new WfSwingWorker<DefaultTreeModel, String>(
				"���ڴӱ��ص���") {
			@Override
			protected DefaultTreeModel doInBackground() throws Exception {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ImportExportUtilities
						.loadFromLocal(f);
				DefaultTreeModel model = new DefaultTreeModel(node);
				return model;
			}

			@Override
			public void wfDone() {
				try {
					DefaultTreeModel model = get();
					operationArea.load(model);
					operationArea.getWfTree().expandRow(1);
					getStatusBar().showGlassInfo(
							"�Ѵӣ�" + f.getAbsolutePath() + "����");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		getBackgroundManager().enqueueOpertimeQueue(worker);
	}

	/**
	 * @return the toolBar
	 */
	public WfToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * @return the statusBar
	 */
	public WfStatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * @return the operationArea
	 */
	public WfOperationArea getOperationArea() {
		return operationArea;
	}

	/**
	 * ��ȡ�Ҽ��˵�������
	 * 
	 * @return
	 */
	public PopupManager getPopupManager() {
		return popupManager;
	}

	/**
	 * ��ȡ�Ի��������
	 * 
	 * @return
	 */
	public DialogManager getDialogManager() {
		return dialogManager;
	}

	/**
	 * ��ȡ��̨������
	 * 
	 * @return
	 */
	public WfBackgroundRunner getBackgroundManager() {
		return backgroundManager;
	}

	public ModeManager getModeManager() {
		return modeManager;
	}

	public CopyPaste getCopyPaste() {
		return copyPaste;
	}

	public void addEditorPackedListener(WfEditorPackedListener l) {
		packListeners.add(l);
	}

	public void fireEditorPackListeners() {
		for (WfEditorPackedListener l : packListeners) {
			l.uiPacked(this);
		}
	}

	private List<WfEditorPackedListener> packListeners = new Vector<WfEditorPackedListener>();

	public static interface WfEditorPackedListener {
		public void uiPacked(WfEditor editor);
	}

	/**
	 * ��ȡ��ǰ�Ĳ����û�ID
	 * 
	 * @return
	 */
	public String getUserID() {
		return (String) this.getClientProperty("WOFO_USERID");
	}

	public WfCacheManager getCache() {
		return cache;
	}

	/**
	 * ��ȡԶ��Servlet path
	 * 
	 * @return
	 */
	public String getServletPath() {
		return (String) this.getClientProperty("WOFO_SERVLETPATH");
	}

	/**
	 * ��ȡ�ͼԪչʾ���ͣ�center��ʾ��ͼԪ��������ʾ���м䣬bottom��ʾСͼԪ��������ʾ���·�
	 * 
	 * @return
	 */
	public String getWfActivityType() {
		return (String) this.getClientProperty("WFACTIVITY_TYPE");
	}

	public void paint(Graphics g) {
		if (!isEnabled()) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f));
		}
		super.paint(g);
	}
}
