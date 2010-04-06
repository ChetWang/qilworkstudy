package com.nci.ums.desktop.bean;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 扩展AppenderSkeleton，实现append方法即可
 */
public class ComponentAppender extends AppenderSkeleton {

	protected Component comp; // 用来展现log信息的gui组件
	protected int entries;
	protected int maxEntries; // 记录的最多显示数

	public static Appender getAppender(String appenderName) {
		return getAppender(appenderName, null);
	}

	public static Appender getAppender(String appenderName, String categoryName) {
		Appender result = null;
		Logger testcat;
		if (categoryName != null) {
			testcat = Logger.getLogger(categoryName);
			if (testcat != null) {
				result = testcat.getAppender(appenderName);
			}
		}
		if (result == null) {
			testcat = Logger.getRootLogger();
			result = testcat.getAppender(appenderName);
		}
		return result;
	}

	public ComponentAppender() {
		this(null);
	}

	public ComponentAppender(Component comp) {
		this(comp, 1);
	}

	public ComponentAppender(Component comp, int maxEntries) {
		this.entries = 0;
		this.maxEntries = maxEntries;
		setComponent(comp);
	}

	public Component getComponent() {
		return this.comp;
	}

	public void setComponent(Component comp) {
		String pattern = "";
		pattern += "[%d{ISO8601}] ";
		pattern += "%l %m %n";
		PatternLayout layout = new PatternLayout(pattern);
		if ((comp instanceof JTextArea) || (comp instanceof JTextPane))
			this.layout = layout;
		else
			this.layout = new PatternLayout("%m");
		this.comp = comp;
	}

	public int getMaxEntries() {
		return this.maxEntries;
	}

	/**
	 * Sets the maximum number of logging entries.
	 * 
	 * @param value -
	 *            maximum number of logging entries. This value is ignored if
	 *            the component supports just 1 line.
	 */
	public void setMaxEntries(int value) {
		if (this.entries > value) {
			// the new maxEntry value is smaller than the actual entry counter
			// we have to delete the oldest entries
			int toomuch = this.entries - value;

			if (comp instanceof JTextPane) {
				JTextPane textPane = (JTextPane) comp;
				try {
					StyledDocument doc = textPane.getStyledDocument();
					if (entries == maxEntries) {
						Element element = doc.getParagraphElement(0);
						int startOfs = element.getStartOffset();
						element = doc.getParagraphElement(toomuch - 1);
						int endOfs = element.getEndOffset();
						doc.remove(startOfs, endOfs - startOfs);
					}
				} catch (Exception x) {
					x.printStackTrace();
				}
			} else if (comp instanceof JTextArea) {
				JTextArea textArea = (JTextArea) comp;
				try {
					Document doc = textArea.getDocument();
					int endOfs = textArea.getLineEndOffset(toomuch - 1);
					int docLen = doc.getLength();
					String docText = textArea.getText();
					if (docLen < endOfs)
						doc.remove(0, docLen);
					else
						doc.remove(0, endOfs);
					textArea.setCaretPosition(doc.getLength());
				} catch (Exception x) {
				}
			} else if (comp instanceof JComboBox) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) ((JComboBox) comp)
						.getModel();
				for (int i = 0; i < toomuch; i++) {
					model.removeElementAt(0);
				}
			} else if (comp instanceof java.awt.List) {
				for (int i = 0; i < toomuch; i++) {
					((java.awt.List) comp).remove(0);
				}
			}

			this.entries = value;
		}
		this.maxEntries = value;
	}

	public boolean requiresLayout() {
		return true;
	}

	public void append(LoggingEvent event) {
		String text = this.layout.format(event);
		// swing components
		if (comp instanceof JLabel) {
			((JLabel) comp).setText(text);
		} else if (comp instanceof JTextArea) {
			JTextArea textArea = (JTextArea) comp;
			try {
				Document doc = textArea.getDocument();
				if (entries == maxEntries) {
					// Delete 1 line
					int endOfs = textArea.getLineEndOffset(0);
					int docLen = doc.getLength();
//					String docText = textArea.getText();
					if (docLen < endOfs)
						doc.remove(0, docLen);
					else
						doc.remove(0, endOfs);
					entries -= 1;
				}
				textArea.append(text);
				if (entries == 0)
					doc.remove(1, 1);
				textArea.setCaretPosition(doc.getLength());
			} catch (Exception x) {
			}
			;
			entries += 1;
		} else if (comp instanceof JTextComponent) {
			((JTextComponent) comp).setText(text);
		} else if (comp instanceof JComboBox) {
			DefaultComboBoxModel model = (DefaultComboBoxModel) ((JComboBox) comp)
					.getModel();
			if (entries == maxEntries) {
				model.removeElementAt(0);
				entries -= 1;
			}
			model.addElement(text);
			entries += 1;
			((JComboBox) comp).setSelectedIndex(entries - 1);
		}
		// awt components
		else if (comp instanceof java.awt.Label) {
			((java.awt.Label) comp).setText(text);
		} else if (comp instanceof java.awt.List) {
			if (entries == maxEntries) {
				((java.awt.List) comp).remove(0);
				entries -= 1;
			}
			((java.awt.List) comp).add(text);
			entries += 1;
		} else if (comp instanceof TextComponent) {
			((TextComponent) comp).setText(text);
		}
	}

	/**
	 * Removes all logging entries from the UI elements
	 * 
	 */
	public void reset() {
		// swing components
		if (comp instanceof JLabel) {
			((JLabel) comp).setText("");
		} else if (comp instanceof JTextComponent) { // includes JTextArea
														// and JTextPane!
			((JTextComponent) comp).setText("");
		} else if (comp instanceof JComboBox) {
			DefaultComboBoxModel model = (DefaultComboBoxModel) ((JComboBox) comp)
					.getModel();
			model.removeAllElements();
		}
		// awt components
		else if (comp instanceof java.awt.Label) {
			((java.awt.Label) comp).setText("");
		} else if (comp instanceof java.awt.List) {
			((java.awt.List) comp).removeAll();
		} else if (comp instanceof java.awt.TextComponent) {
			((java.awt.TextComponent) comp).setText("");
		}
		this.entries = 0;
	}

	public void close() {
		reset();
	}
}
