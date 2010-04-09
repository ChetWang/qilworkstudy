package com.nci.domino.components.toolbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideToggleButton;
import com.nci.domino.WfEditor;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.concurrent.WfStartupEndException;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 定义器工具栏
 * 
 * @author Qil.Wong
 */
public class WfToolBar extends JToolBar {
	private static final long serialVersionUID = -3070426752765660399L;
	private WfEditor editor = null;

	private Map<String, MyButtonGroup> buttonGroups = new HashMap<String, MyButtonGroup>();

	private Map<String, AbstractButton> buttons = new HashMap<String, AbstractButton>();

	WfToolbarActionListener toolbarActionListener;

	// private boolean outsideCall = false;

	public WfToolBar(WfEditor ed) {
		super();
		this.editor = ed;
		toolbarActionListener = new WfToolbarActionListener(this);
		WfRunnable run = new WfRunnable("正在初始化工具栏") {
			public void run() {
				init();
			}
		};
		try {
			editor.getBackgroundManager().enqueueStartupQueue(run);
		} catch (WfStartupEndException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化工具条
	 */
	private void init() {
		setRollover(true);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		NodeList nodes = getToolbarNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element e = (Element) nodes.item(i);
				parseToolbarElement(e);
			}
		}
	}

	/**
	 * 获取工具条配置
	 * 
	 * @return
	 */
	private NodeList getToolbarNodes() {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream is = getClass().getResourceAsStream(
					"/resources/toolbar.xml");
			Document toolbarDoc = docBuilder.parse(is);
			NodeList nodes = toolbarDoc.getDocumentElement().getChildNodes();
			is.close();
			return nodes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void parseToolbarElement(Element e) {

		if (e.getTagName().equals("separator")) {
			addSeparator();
		} else if (e.getTagName().equals("group")) {

			MyButtonGroup bg = new MyButtonGroup();
			buttonGroups.put(e.getAttribute("id"), bg);
			NodeList groupNodes = e.getChildNodes();
			for (int x = 0; x < groupNodes.getLength(); x++) {
				Node groupNode = groupNodes.item(x);
				if (groupNode instanceof Element) {
					Element groupBtnEle = (Element) groupNode;
					AbstractButton btn = null;
					if (groupBtnEle.getNodeName().equals("button")) {
						btn = createToogle(groupBtnEle, bg);
						bg.add(btn);
					} else if (groupBtnEle.getNodeName().equals("splits")) {
						btn = createSplits(groupBtnEle, bg);
					}
					add(btn);
				}
			}

		} else if (e.getTagName().equals("splits")) {
			add(createSplits(e, null));
		} else if (e.getTagName().equals("button")) {
			add(createButton(e));
		} else if (e.getTagName().equals("toogle")) {
			add(createToogle(e, null));
		}
	}

	private AbstractButton createSplits(final Element e, final MyButtonGroup bg) {
		NodeList splits = e.getChildNodes();
		WfToolbarButtonInfo splitInfo = getButtonInfo(e);
		final JideSplitButton splitBtn = new JideSplitButton(splitInfo
				.getText(), Functions.getImageIcon(splitInfo.getIcon()));

		// 设置name和tooltip
		setInfoToToolbarButton(splitBtn, splitInfo);
		// 也要延迟加载执行
		Timer t = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addActionLater(e, splitBtn);
			}
		});
		t.setRepeats(false);
		t.start();
		// addActionLater(e, splitBtn);
		for (int x = 0; x < splits.getLength(); x++) {
			Node splitNode = splits.item(x);
			if (splitNode instanceof Element) {
				Element splitBtnEle = (Element) splitNode;
				splitInfo = getButtonInfo(splitBtnEle);
				if (splitInfo.getText().equals("separator")) {
					splitBtn.addSeparator();
				} else {
					final JMenuItem item = new SplitMenuItem(splitBtn,
							splitInfo.getText(), Functions
									.getImageIcon(splitInfo.getIcon()));//
					if (splitBtnEle.getAttribute("enabled").equals("false")) {
						item.setEnabled(false);
					}
					setInfoToToolbarButton(item, splitInfo);
					splitBtn.add(item);
					// 考虑到菜单和按钮产生的先后顺序，所有其它事件都在后台唤醒
					t = new Timer(1000, new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addActionLater(e, item);
						}
					});
					t.setRepeats(false);
					t.start();
				}
			}
		}

		if (e.getAttribute("enabled").equals("false")) {
			splitBtn.setEnabled(false);
		}
		splitBtn.setPreferredSize(new Dimension(42, 32));
		return splitBtn;
	}

	/**
	 * 延时添加事件，防止在toolbar还没完全生成时就执行
	 * 
	 * @param e
	 * @param item
	 */
	private void addActionLater(Element e, final JMenuItem item) {
		String groupID = e.getAttribute("groupid");
		final MyButtonGroup relatedBg = buttonGroups.get(groupID);
		if (relatedBg != null) {
			// 关联的buttongroup也要受影响
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					relatedBg.actionPerformed(new ActionEvent(item, 0, ""));
				}
			});
		}
	}

	private AbstractButton createButton(Element e) {
		WfToolbarButtonInfo info = getButtonInfo(e);
		AbstractButton button = new JButton(info.getText(), Functions
				.getImageIcon(info.getIcon()));
		button.setOpaque(false);
		if (e.getAttribute("enabled").equals("false")) {
			button.setEnabled(false);
		}
		// 设置name和tooltip
		setInfoToToolbarButton(button, info);
		button.setPreferredSize(new Dimension(32, 32));
		return button;
	}

	private AbstractButton createToogle(Element e, MyButtonGroup group) {
		WfToolbarButtonInfo info = getButtonInfo(e);
		AbstractButton button = new MyToggleButton(info.getText(), Functions
				.getImageIcon(info.getIcon()), group);
		// 设置name和tooltip
		setInfoToToolbarButton(button, info);
		button.setPreferredSize(new Dimension(32, 32));
		if (e.getAttribute("enabled").equals("false")) {
			button.setEnabled(false);
		}
		if (e.getAttribute("selected").equals("true")) {
			button.setSelected(true);
		}
		return button;
	}

	/**
	 * XML中定义的基本组件元素
	 * 
	 * @param e
	 * @return String[], 分别代表name，text，tooltip，icon
	 */
	private WfToolbarButtonInfo getButtonInfo(Element e) {
		String[] info = new String[5];
		info[0] = e.getAttribute("actionCommand");
		info[1] = e.getAttribute("text");
		if (e.getNodeName().equals("split")) {
			info[1] = info[1].equals("") ? WofoResources.getValueByKey(info[0])
					: e.getAttribute("text");
		}
		if (e.getNodeName().equals("separator")) {
			info[1] = "separator";
		}
		info[2] = e.getAttribute("tooltip").trim().equals("") ? WofoResources
				.getValueByKey(info[0]) : e.getAttribute("tooltip");
		info[3] = e.getAttribute("icon").trim().equals("") ? info[0] + ".gif"
				: e.getAttribute("icon");
		info[4] = e.getAttribute("actionClass");
		ActionListener lis = null;
		if (info[4] != null && !info[4].trim().equals("")) {
			try {
				Class lisName = Class.forName(info[4]);
				lis = (ActionListener) lisName.getConstructor(WfToolBar.class)
						.newInstance(this);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		WfToolbarButtonInfo compInfo = new WfToolbarButtonInfo();
		compInfo.setIcon(info[3]);
		compInfo.setActionCommand(info[0]);
		compInfo.setText(info[1]);
		compInfo.setTooltip(info[2]);
		compInfo.setActionListener(lis);
		return compInfo;
	}

	/**
	 * 为配置生成的组件添加其余信息
	 * 
	 * @param comp
	 * @param info
	 * @param lis
	 */
	private void setInfoToToolbarButton(AbstractButton comp,
			WfToolbarButtonInfo info) {
		comp.setToolTipText(info.getTooltip());
		if (info.getActionListener() == null) {
			comp.addActionListener(toolbarActionListener);
		} else {
			comp.addActionListener(info.getActionListener());
		}
		comp.setActionCommand(info.getActionCommand());
		comp.setFocusable(false);
		buttons.put(info.getActionCommand(), comp);
	}

	public void paintComponent(Graphics g) {
		Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
		JideSwingUtilities.fillGradient((Graphics2D) g, rect, new Color(235,
				235, 235), Color.WHITE, false);
	}

	/**
	 * 获取编辑器对象
	 * 
	 * @return the editor
	 */
	public WfEditor getEditor() {
		return editor;
	}

	public Map<String, AbstractButton> getButtons() {
		return buttons;
	}

	/**
	 * 设置对齐按钮有效性
	 * 
	 * @param flag
	 */
	public void setAlignButtonsEnabled(boolean flag) {
		Iterator<String> it = buttons.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key.indexOf("align") == 0) {
				buttons.get(key).setEnabled(flag);
			}
		}
	}

	/**
	 * 自定义的ButtonGroup，考虑到还有其它的非tooglebutton和radiobutton对象也可能加入进来，
	 * jdk的ButtonGroup不太适用
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class MyButtonGroup implements ActionListener {

		private Set<AbstractButton> buttons = new HashSet<AbstractButton>();

		private void add(AbstractButton btn) {
			buttons.add(btn);
			btn.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			for (AbstractButton btn : buttons) {
				if (btn != e.getSource()) {
					btn.setSelected(false);
				} else {
					btn.setSelected(true);
				}
				if (e.getSource() instanceof SplitMenuItem) {
					SplitMenuItem item = (SplitMenuItem) e.getSource();
					item.splitBtn
							.setBorder(new BevelBorder(BevelBorder.LOWERED));
				}
			}
		}
	}

	private class SplitMenuItem extends JMenuItem {
		JideSplitButton splitBtn;

		public SplitMenuItem(JideSplitButton splitBtn, String txt,
				ImageIcon icon) {
			super(txt, icon);
			this.splitBtn = splitBtn;
		}
	}

	private class MyToggleButton extends JideToggleButton {
		MyButtonGroup group;

		public MyToggleButton(String txt, ImageIcon icon, MyButtonGroup group) {
			super(txt, icon);
			this.group = group;
		}

		public void setSelected(boolean flag) {
			super.setSelected(flag);
			// if (flag && group != null)
			// group.actionPerformed(new ActionEvent(this, 0, ""));
		}
	}
}
