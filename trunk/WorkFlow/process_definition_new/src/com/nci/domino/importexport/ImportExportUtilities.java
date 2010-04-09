package com.nci.domino.importexport;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class ImportExportUtilities {

	// private static int

	/**
	 * ��������XML��ʽ���浽����
	 * 
	 * @param filePath
	 *            ���ر�����ļ�·��
	 * @param o
	 *            ָ���Ķ���
	 */
	public static void saveLocal(File f, Serializable o) {
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(o);
			encoder.flush();
			fos.close();
			encoder.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ������xml��ʽ���ļ���ԭΪ����
	 * 
	 * @param filePath
	 *            ����xml��ʽ���ļ�·��
	 * @return ��ԭ��Ķ���
	 */
	public static Serializable loadFromLocal(File xmlFile) {
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(
					xmlFile));
			XMLDecoder decoder = new XMLDecoder(is);
			Serializable s = (Serializable) decoder.readObject();
			is.close();
			decoder.close();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
