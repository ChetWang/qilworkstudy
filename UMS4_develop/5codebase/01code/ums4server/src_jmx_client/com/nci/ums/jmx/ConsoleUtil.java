package com.nci.ums.jmx;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConsoleUtil {

	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static ResourceBundle internationalBundle;

	static {
		// Locale.setDefault(Locale.CHINA);
		Locale.setDefault(Locale.getDefault());
		// Locale.setDefault(Locale.CANADA);
		internationalBundle = ResourceBundle
				.getBundle("com/nci/ums/jmx/desktop/ml");
	}

	

	public static synchronized long getCurrentTime(String format) {
		SimpleDateFormat current = new SimpleDateFormat(format);
		long currentTime = Long.parseLong(current.format(new java.util.Date()));
		return currentTime;
	}

	public static String getCurrentTimeStr(String format) {
		SimpleDateFormat current = new SimpleDateFormat(format);
		String currentTime = current.format(new java.util.Date());
		return currentTime;
	}

	public static void replacePropFile(String[] parameters, String[] values,
			String filePath) throws IOException {
		File output = new File(filePath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(output
				.getAbsolutePath(), false));
		for (int i = 0; i < parameters.length; i++) {
			if (!parameters[i].equals("") || !values[i].equals(""))
				bw.write(parameters[i] + "=" + values[i] + "\r\n");
		}
		bw.close();
	}

	public static synchronized void addEscapeHotKey(
			final RootPaneContainer rootContainer) {
		rootContainer.getRootPane().getInputMap(
				JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		Action escAction = new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				if (rootContainer instanceof JFrame) {
					((JFrame) rootContainer).setVisible(false);
				} else if (rootContainer instanceof JDialog) {
					((JDialog) rootContainer).setVisible(false);
				}
			}
		};
		rootContainer.getRootPane().getActionMap().put("escape", escAction);
	}

	public static synchronized void setIPTo4TextField(String ip,
			javax.swing.JTextField[] textArr) {
		textArr[0].setText(ip.substring(0, ip.indexOf(".")));
		ip = ip.substring(ip.indexOf(".") + 1);
		textArr[1].setText(ip.substring(0, ip.indexOf(".")));
		ip = ip.substring(ip.indexOf(".") + 1);
		textArr[2].setText(ip.substring(0, ip.indexOf(".")));
		ip = ip.substring(ip.indexOf(".") + 1);
		textArr[3].setText(ip);
	}

	public static String toUnicode(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace) {
					outBuffer.append('\\');
				}
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if ((aChar < 0x0020) || (aChar > 0x007e)) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	public static Vector getAxis2PortVector(String axis2ConfigXml)
			throws IOException {
		Vector v = new Vector(2);
		InputStream ins = new FileInputStream(axis2ConfigXml);
		// Document doc
		// SAXBuilder sb = new SAXBuilder();
		Document originalDoc = null;
		originalDoc = getXMLDocumentByStream(ins);
		v.add(originalDoc);
		ins.close();
		// Element config = originalDoc.getRootElement(); // 得到根元素
		// Element transportReceiver = (Element) config
		// .getChild("transportReceiver");
		Element transportReceiver = (Element) originalDoc.getElementsByTagName(
				"transportReceiver").item(0);
		NodeList params = transportReceiver.getElementsByTagName("parameter");
		for(int i=0;i<params.getLength();i++){
			Element param = (Element)params.item(i);
			if(param.getAttribute("name").equals("port")){
				v.add(param.getTextContent());
			}
		}
		return v;
	}

	/**
	 * 通过给定的InputStream对象获取XML Document对象
	 * 
	 * @param is
	 * @return
	 */
	public static Document getXMLDocumentByStream(InputStream is) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder bulider = null;
		try {
			bulider = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = bulider.parse(is);
		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		doc.normalize();
		return doc;
	}

	public static String getLocaleString(String key) {

		return internationalBundle.getString(key);
	}

	public static void stopEditingCells(javax.swing.JTable table) {
		if (table.getCellEditor() != null) {
			int selectIndex = table.getSelectedRow();
			if (selectIndex != -1) {
				table.getCellEditor().stopCellEditing();
			}
		}
	}
}
