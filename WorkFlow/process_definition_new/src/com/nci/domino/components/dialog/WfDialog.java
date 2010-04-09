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
 * 基础对话框，所有流程定义器的对话框都基于这个类
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfDialog extends StandardDialog {

	private static final long serialVersionUID = 6709020618081377792L;
	protected WfEditor editor;
	// 确定按钮
	protected JButton okButton = new JButton();
	// 取消按钮
	protected JButton cancelButton = new JButton();
	// 对话框的默认宽度
	protected int defaultWidth;
	// 对话框的默认高度
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
	 * 初始化对话框
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
	 * 在ContentPanel是Tab组件的时候，允许在这里给customPanles进行修改，增减自定义的wfinputpanel对象
	 */
	public void initCustomComponents() {
	}

	/**
	 * 业务处理窗口
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
	 * 显示对话框,在显示前清空所有的内容
	 */
	public void showWfDialog(int width, int height) {
		showWfDialog(width, height, null);
	}

	/**
	 * 显示对话框,在显示前清空所有的内容
	 */
	public void showWfDialog(final int width, final int height,
			final Serializable defaultValue) {
		pack();// 第一个pack是引发三个create**方法，创建对象
		clearContents();
		if (defaultValue != null) {
			setInputValues(defaultValue);
		}
		pack();// 第二个pack是对创建完后并clearContents后的对话框进行重新优化组合
		if (width != -1) {
			setSize(width, height);
		}
		setLocationRelativeTo(editor.findParentFrame());
		setVisible(true);
	}

	/**
	 * 显示对话框，并赋予初始值
	 * 
	 * @param defaultValue
	 */
	public void showWfDialog(Serializable defaultValue) {
		showWfDialog(defaultWidth, defaultHeight, defaultValue);
	}

	/**
	 * 获取输入的值,此函数不建议外部调用， 因为在getValue在当前对象中被调用后，已经清空内容，再次调用此函数将获取清空后的值；
	 * 
	 */
	public Serializable getInputValues() {
		for (WfInputPanel p : customPanels) {
			p.applyValues(defalutValue);
		}
		return defalutValue;
	}

	/**
	 * 为对话框初始化内部组件的值
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
	 * 打开前需要清空内容
	 */
	protected void clearContents() {
		for (WfInputPanel p : customPanels) {
			p.reset();
		}
	}

	/**
	 * 检查对话框属性输入的是否正确，如果错误，返回错误提示信息，如果正确，返回null或""
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
