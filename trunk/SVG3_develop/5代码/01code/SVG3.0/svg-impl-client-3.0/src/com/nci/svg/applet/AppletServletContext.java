package com.nci.svg.applet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.nci.svg.sdk.client.util.Constants;

public class AppletServletContext {

	/**
	 * ������jar���õ�Ŀ¼��extĿ¼�²����ټ�������Ŀ¼������ֻ�ܷ���extĿ¼�£� ��������ڿ����������������jreͬ��һ��jreʱ���������
	 */
	private String cacheJarPath = System.getProperty("java.home") + "/lib/ext/";

	/**
	 * ��Ű汾��Ϣ���ļ���
	 */
	private String versionFileName = "versions.nci";

	public AppletServletContext() {
		File cacheJarDir = new File(cacheJarPath);
		if (!cacheJarDir.exists()) {
			cacheJarDir.mkdirs();
		}
	}

	/**
	 * ��Servletһ��ͨ�Ź��̣��õ�һ�����ض���
	 * 
	 * @param baseURL
	 *            ����URL
	 * @param param
	 *            ���ݸ�servlet�Ĳ���
	 * @return
	 * @throws IOException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object communicateWithURL(String baseURL, String[][] param)
			throws IOException {
		// if (netWorkOK) {
		try {
			URL url = new URL(baseURL);
			URLConnection rConn = url.openConnection();
			rConn.setDoOutput(true);
			rConn.setDoInput(true);
			rConn.setReadTimeout(Constants.SERVLET_TIME_OUT);
			PrintStream output = new PrintStream(rConn.getOutputStream());
			StringBuffer rs = new StringBuffer();
			if (param != null && param.length > 0) {
				for (int i = 0; i < param.length; i++) {
					if (param[i][0] == null || param[i][1] == null)
						continue;
					if (i != 0) {
						rs.append("&");
					}
					rs.append(URLEncoder.encode(param[i][0], "UTF-8")).append(
							'=')
							.append(URLEncoder.encode(param[i][1], "UTF-8"));
				}
			}
			output.print(rs.toString());
			output.flush();
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(rConn.getInputStream()));
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showConfirmDialog(null, "�������ӳ���", "����",
							JOptionPane.CLOSED_OPTION,
							JOptionPane.ERROR_MESSAGE);
				}
			});
			throw e;
		}
		return "";
	}

	/**
	 * ��ȡservlet�˷��͹�������
	 * 
	 * @param baseURL
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public InputStream communicateWithURLStream(String baseURL, String[][] param)
			throws IOException {
		try {
			URL url = new URL(baseURL);
			URLConnection rConn = url.openConnection();
			rConn.setDoOutput(true);
			rConn.setDoInput(true);
			rConn.setReadTimeout(Constants.SERVLET_TIME_OUT);
			PrintStream output = new PrintStream(rConn.getOutputStream());
			StringBuffer rs = new StringBuffer();
			if (param != null && param.length > 0) {
				for (int i = 0; i < param.length; i++) {
					if (param[i][0] == null || param[i][1] == null)
						continue;
					if (i != 0) {
						rs.append("&");
					}
					rs.append(URLEncoder.encode(param[i][0], "UTF-8")).append(
							'=')
							.append(URLEncoder.encode(param[i][1], "UTF-8"));
				}
			}
			output.print(rs.toString());
			output.flush();
			return rConn.getInputStream();
		} catch (IOException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showConfirmDialog(null, "�������ӳ���", "����",
							JOptionPane.CLOSED_OPTION,
							JOptionPane.ERROR_MESSAGE);
				}
			});
			throw e;
		}
	}

	public void downloadJar(HashMap<String, Object> map_config, String jarName) {
		String[][] params = new String[1][2];
		params[0][0] = "jarName";
		params[0][1] = jarName;
		byte b[] = new byte[1024];
		try {
			FileOutputStream fos = new FileOutputStream(new File(
					this.cacheJarPath + jarName));
			InputStream is = communicateWithURLStream((String) map_config
					.get("approot")
					+ (String) map_config.get("servletpath")
					+ "?action=getOneSvgJar", params);
			int n = 0;
			while ((n = is.read(b)) != -1) {
				fos.write(b, 0, n);
			}
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ��server�˵İ汾д�뱾�صİ汾�ļ�
	 * 
	 * @param serverVersionMap
	 */
	public void writeToVersionFile(HashMap<String, String> serverVersionMap) {
		File versionFile = new File(this.cacheJarPath + versionFileName);
		try {
			FileOutputStream fos = new FileOutputStream(versionFile);
			Iterator<String> it = serverVersionMap.keySet().iterator();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = it.next();
				value = serverVersionMap.get(key);
				fos.write(key.getBytes("utf-8"));
				fos.write("=".getBytes("utf-8"));
				fos.write(value.getBytes("utf-8"));
				fos.write("\r".getBytes());
			}
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ��ȡapplet�����������Ĵ��λ��
	 * @return
	 */
	public String getCacheJarPath() {
		return cacheJarPath;
	}

	/**
	 * ��ȡ�汾�ļ�����
	 * @return
	 */
	public String getVersionFileName() {
		return versionFileName;
	}

}
