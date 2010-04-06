package com.nci.ums.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.cmcc.mm7.vasp.common.ConcurrentHashMap;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.desktop.bean.MediaBean;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;

public class ConsoleUtil {

	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static ResourceBundle internationalBundle;

	static {
//		Locale.setDefault(Locale.CHINA);
		Locale.setDefault(Locale.getDefault());
		// Locale.setDefault(Locale.CANADA);
		internationalBundle = ResourceBundle
				.getBundle("com/nci/ums/desktop/ml");
	}

	/**
	 * 获取UMS的所有注册的渠道信息
	 * 
	 * @return 渠道集合，如果返回的是null，则发生异常，UMS无法启动
	 */
	public synchronized static ArrayList getChannels() {
		ArrayList channels = null;
		Connection conn = null;
		Map channelMap = new ConcurrentHashMap();
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			String sql = "select * from media";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			channels = new ArrayList();
			while (rs.next()) {
				MediaBean bean = new MediaBean(rs.getString("media_id"), rs
						.getString("medianame"), rs.getInt("type"),
						MediaBean.STATUS_STOPPED);
				// channels.add(new MediaBean(rs.getString("media_id"),
				// rs.getString("medianame"), rs.getInt("type"),
				// rs.getInt("statusflag")));
				// 先将所有渠道先放入hashmap，状态为停止，数据库中标明开启的渠道要到真正开启完后再ChannelManager中判断
				channelMap.put(bean.getMediaID(), bean);
			}
			rs.close();
			Map inMediaMap = ChannelManager.getInMediaInfoHash();
			Map outMediaMap = ChannelManager.getOutMediaInfoHash();
			Iterator it_in = inMediaMap.values().iterator();
			Iterator it_out = outMediaMap.values().iterator();
			while (it_in.hasNext()) {
				MediaInfo media = (MediaInfo) it_in.next();
				if (media.getChannelObject() != null) {
					if (media.getChannelObject().getThreadState() == true) {
						((MediaBean) channelMap.get(media.getMediaId()))
								.setMediaStatus(MediaBean.STATUS_RUNNING);
					}
				}
			}
			while (it_out.hasNext()) {
				MediaInfo media = (MediaInfo) it_out.next();
				if (media.getChannelObject() != null) {
					if (media.getChannelObject().getThreadState() == true) {
						((MediaBean) channelMap.get(media.getMediaId()))
								.setMediaStatus(MediaBean.STATUS_RUNNING);
					}
				}
			}
		} catch (Exception e) {
			Res.log(Res.ERROR, "获取渠道信息时产生数据库错误。"
					+ e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.logExceptionTrace(e);
				}
			}
		}
		Iterator channelsIT = channelMap.values().iterator();
		while (channelsIT.hasNext())
			channels.add(channelsIT.next());
		return channels;
	}

	public static long getCurrentTime(String format) {
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
		SAXBuilder sb = new SAXBuilder();
		Document originalDoc = null;
		try {
			originalDoc = sb.build(ins);
			v.add(originalDoc);
		} catch (JDOMException e) {
			Res.logExceptionTrace(e);
		}
		ins.close();
		Element config = originalDoc.getRootElement(); // 得到根元素
		Element transportReceiver = (Element) config
				.getChild("transportReceiver");
		List content = transportReceiver.getContent();
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i) instanceof Element) {
				String name = ((Element) content.get(i)).getName();
				String port = null;
				if (name.equalsIgnoreCase("parameter")) {// 第一个Element属性就是port
					port = ((Element) content.get(i)).getText().trim();
					v.add(port);
				}
				break;
			}
		}
		return v;
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
