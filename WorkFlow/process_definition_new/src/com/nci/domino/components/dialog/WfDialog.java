package com.nci.domino.components.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * �����Ի����������̶������ĶԻ��򶼻��������
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfDialog extends StandardDialog {

	private static final long serialVersionUID = 6709020618081377792L;
	protected WfEditor editor;
	// ȷ����ť
	protected JButton okButton = new JButton();
	// ȡ����ť
	protected JButton cancelButton = new JButton();
	// �Ի����Ĭ�Ͽ��
	protected int defaultWidth;
	// �Ի����Ĭ�ϸ߶�
	protected int defaultHeight;

	protected Serializable defalutValue;

	protected List<WfInputPanel> customPanels = new ArrayList<WfInputPanel>();

	protected List<WfSetValueListener> setValueListeners = new ArrayList<WfSetValueListener>();

	/**
	 * content tab
	 */
	protected JideTabbedPane contentTab = new JideTabbedPane();
	
	public WfDialog(WfEditor editor, String title, boolean modal) {
		super(
				editor.getClientProperty("appletOrFrame") instanceof JFrame ? editor
						.getContainerFrame()
						: (JFrame) null, title, modal);
		init(editor);
	}

	/**
	 * ��ʼ���Ի���
	 * 
	 * @param editor
	 */
	protected void init(WfEditor editor) {
		this.editor = editor;
		contentTab.setScrollSelectedTabOnWheel(true);
		contentTab.setBoldActiveTab(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				getBannerPanel().resumeDefault();
				setDialogResult(RESULT_CANCELLED);
			}
		});
	}

	public void addSetValueListener(WfSetValueListener l) {
		setValueListeners.add(l);
	}

	public void removeSetValueListener(WfSetValueListener l) {
		setValueListeners.remove(l);
	}

	public void fireBeforeSetValueListeners(Serializable o) {
		for (WfSetValueListener l : setValueListeners) {
			l.beforeValueSet(o);
		}
	}

	public void fireAfterSetValueListeners(Serializable o) {
		for (WfSetValueListener l : setValueListeners) {
			l.afterValueSet(o);
		}
	}

	@SuppressWarnings("serial")
	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel();

		okButton.setName(OK);
		cancelButton.setName(CANCEL);

		buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
		buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);

		okButton
				.setAction(new AbstractAction(WofoResources.getValueByKey("ok")) {
					public void actionPerformed(ActionEvent e) {
						String result = checkInput();
						if (result == null || result.equals("")) {
							setDialogResult(RESULT_AFFIRMED);
							okActionPerformed(e);
							setVisible(false);
							dispose();
						} else {
							getBannerPanel().setError(result);
						}
					}
				});
		cancelButton.setAction(new AbstractAction(WofoResources
				.getValueByKey("cancel")) {
			public void actionPerformed(ActionEvent e) {
				setDialogResult(RESULT_CANCELLED);
				cancelActionPerformed(e);
				setVisible(false);
				dispose();
			}
		});
		setDefaultCancelAction(cancelButton.getAction());
		setDefaultAction(okButton.getAction());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 7, 10));
		return buttonPanel;
	}

	public void initComponents() {
		super.initComponents();
		initCustomComponents();
		initBusinessPanel();
	}

	/**
	 * ��ContentPanel��Tab�����ʱ�������������customPanles�����޸ģ������Զ����wfinputpanel����
	 */
	public void initCustomComponents() {
	}

	/**
	 * ҵ������
	 */
	protected void initBusinessPanel() {
		String className = getClass().getName();
		try {
			InputStream is = getClass().getResourceAsStream(
					"/resources/plugins.xml");
			if (is != null) {
				DocumentBuilder docBuilder = DocumentBuilderFactory
						.newInstance().newDocumentBuilder();
				Document pluginDoc = docBuilder.parse(is);
				is.close();
				Element pluginEle = Functions.findNode(
						"/plugins-all/wfdialog-plugins/plugins[@containerClass='"
								+ className + "']", pluginDoc
								.getDocumentElement());
				if (pluginEle != null) {
					NodeList plugins = pluginEle
							.getElementsByTagName("pluginClass");
					for (int i = 0; i < plugins.getLength(); i++) {
						Element plugin = (Element) plugins.item(i);
						String panelClassName = plugin
								.getAttribute("className");
						if (!panelClassName.equals("")) {
							try {
								WfInputPanel panel = (WfInputPanel) Class
										.forName(panelClassName)
										.getConstructor(WfDialog.class)
										.newInstance(this);
								String title = panel.getPanelName();
								if (!plugin.getAttribute("title").equals("")) {
									title = plugin.getAttribute("title");
								}
								panel.setPanelName(title);
								customPanels.add(panel);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (WfInputPanel p : customPanels) {
			((JTabbedPane) getContentPanel()).addTab(p.getPanelName(), null, p,
					null);
		}
	}

	public void okActionPerformed(ActionEvent e) {

	}

	public void cancelActionPerformed(ActionEvent e) {

	}

	/**
	 * ��ʾ�Ի���,����ʾǰ������е�����
	 */
	public void showWfDialog(int width, int height) {
		showWfDialog(width, height, null);
	}

	/**
	 * ��ʾ�Ի���,����ʾǰ������е�����
	 */
	public void showWfDialog(final int width, final int height,
			final Serializable defaultValue) {
		pack();// ��һ��pack����������create**��������������
		clearContents();
		if (defaultValue != null) {
			setInputValues(defaultValue);
		}
		pack();// �ڶ���pack�ǶԴ������clearContents��ĶԻ�����������Ż����
		if (width != -1) {
			setSize(width, height);
		}
		setLocationRelativeTo(editor.findParentFrame());
		setVisible(true);
	}

	/**
	 * ��ʾ�Ի��򣬲������ʼֵ
	 * 
	 * @param defaultValue
	 */
	public void showWfDialog(Serializable defaultValue) {
		showWfDialog(defaultWidth, defaultHeight, defaultValue);
	}

	/**
	 * ��ȡ�����ֵ,�˺����������ⲿ���ã� ��Ϊ��getValue�ڵ�ǰ�����б����ú��Ѿ�������ݣ��ٴε��ô˺�������ȡ��պ��ֵ��
	 * 
	 */
	public Serializable getInputValues() {
		for (WfInputPanel p : customPanels) {
			p.applyValues(defalutValue);
		}
		return defalutValue;
	}

	/**
	 * Ϊ�Ի����ʼ���ڲ������ֵ
	 * 
	 * @param value
	 */
	public void setInputValues(Serializable value) {
		fireBeforeSetValueListeners(value);
		this.defalutValue = value;
		for (WfInputPanel p : customPanels) {
			p.setValues(value);
		}
		fireAfterSetValueListeners(value);
	}

	/**
	 * ��ǰ��Ҫ�������
	 */
	protected void clearContents() {
		for (WfInputPanel p : customPanels) {
			p.reset();
		}
	}

	/**
	 * ���Ի�������������Ƿ���ȷ��������󣬷��ش�����ʾ��Ϣ�������ȷ������null��""
	 * 
	 * @return
	 */
	protected String checkInput() {
		for (WfInputPanel p : customPanels) {
			String result = p.check();
			if (result != null && !result.trim().equals("")) {
				((JTabbedPane) p.getParent()).setSelectedComponent(p);
				return result;
			}
		}
		return null;
	}

	@Override
	public WfBannerPanel getBannerPanel() {
		JComponent comp = super.getBannerPanel();
		if (comp instanceof WfBannerPanel) {
			return (WfBannerPanel) comp;
		} else {
			return (WfBannerPanel) comp.getComponent(0);
		}
	}

	public WfEditor getEditor() {
		return editor;
	}

	public Serializable getDefaultValue() {
		return defalutValue;
	}

	public List<WfInputPanel> getCustomPanels() {
		return customPanels;
	}
}
