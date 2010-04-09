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
	 * 将对象以XML格式保存到本地
	 * 
	 * @param filePath
	 *            本地保存的文件路径
	 * @param o
	 *            指定的对象
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
	 * 将本地xml格式的文件还原为对象
	 * 
	 * @param filePath
	 *            本地xml格式的文件路径
	 * @return 还原后的对象
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
