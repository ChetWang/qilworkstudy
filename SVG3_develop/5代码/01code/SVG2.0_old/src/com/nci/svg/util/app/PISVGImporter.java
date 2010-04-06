package com.nci.svg.util.app;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import oracle.sql.BLOB;

public class PISVGImporter {

	Connection piConn = null;
	Connection svgtoolConn = null;
	String tableName_pi = "verparam";
	String tableName_svgtool = "tb_fms_svg_fileinfo";

	private PISVGImporter() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		piConn = DriverManager.getConnection(
				"jdbc:oracle:thin:@10.147.218.45:1521:oral", "svgpi", "svgpi");
		svgtoolConn = DriverManager.getConnection(
				"jdbc:oracle:thin:@10.147.218.232:1521:jxjxsc", "svgtool",
				"svgtool");
		piConn.setAutoCommit(false);
		svgtoolConn.setAutoCommit(false);
	}

	public void startImport() throws Exception {
		PreparedStatement prep = svgtoolConn
				.prepareStatement("INSERT INTO "+tableName_svgtool+" (ID,APPBH,MODULENAME,FILENAME,FILETYPE,FILEEXT,FILEDESC,AREATYPE,MAPCONTENT) VALUES(?,?,?,?,?,?,?,?,?)");
		ResultSet piRS = piConn.createStatement().executeQuery(
				"SELECT * FROM "+tableName_pi);
		PreparedStatement prep2 = svgtoolConn
				.prepareStatement("SELECT * FROM "+tableName_svgtool+" WHERE ID=?");
		String id = "";
		while (piRS.next()) {
			id = piRS.getString("RECID");
			prep.setString(1, "nci"+id);
			prep.setString(2, "SVG");
			prep.setString(3, "svg");
			prep.setString(4, piRS.getString("NEWFNAME"));
			prep.setString(5, "0");
			prep.setString(6, ".svg");
			prep.setString(7, piRS.getString("SUBNAME"));
			prep.setString(8, "-1");
			prep.setBlob(9, oracle.sql.BLOB.empty_lob());
			prep.executeUpdate();
			prep2.setString(1, "nci"+id);
			ResultSet rs = prep2.executeQuery();
			while (rs.next()) {
				BLOB blob_pi = (BLOB) piRS.getBlob("MAPCONTENT");
				BLOB blob_svgtool = (BLOB) rs.getBlob("MAPCONTENT");
				InputStream is = blob_pi.getBinaryStream();
				OutputStream out = blob_svgtool.getBinaryOutputStream(); // ���������
				long length = blob_pi.length();
				byte[] buff = new byte[(int) length];
				is.read(buff);

				out.write(buff);
				is.close();
				out.close();
			}
			rs.close();
			System.out.println("�ɹ���" + id + "���е��뵼��");
		}
		prep2.close();
		prep.close();
		piRS.close();
		piConn.close();
		svgtoolConn.commit();
		svgtoolConn.close();
		System.out.println("���뵼����ɣ�������id�󶼼���'nci'");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int i = JOptionPane.showConfirmDialog(null, "����wangql��Ҫ���д˳���,���\"ȡ��\"�˳���",
					"Σ��", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (i == 0)
				new PISVGImporter().startImport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
