package com.nci.svg.util.app;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import oracle.sql.BLOB;

public class TsImporter {

	Connection piConn = null;
	Connection svgtoolConn = null;
	String tableName_pi = "verparam";
	String tableName_svgtool = "tb_fms_svg_fileinfo";
	
	private static String originalDBServerIP;
	private static String originalDBServerPort;
	private static String originalDBSID;
	private static String originalDBUserName;
	private static String originalDBUserPass;
	
	private static String destinationDBServerIP;
	private static String destinationDBServerPort;
	private static String destinationDBSID;
	private static String destinationDBUserName;
	private static String destinationDBUserPass;

	private TsImporter() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		piConn = DriverManager.getConnection(
				"jdbc:oracle:thin:@"+originalDBServerIP+":"+originalDBServerPort+":"+originalDBSID, originalDBUserName, originalDBUserPass);
		svgtoolConn = DriverManager.getConnection(
				"jdbc:oracle:thin:@"+destinationDBServerIP+":"+destinationDBServerPort+":"+destinationDBSID, destinationDBUserName, destinationDBUserPass);
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
				OutputStream out = blob_svgtool.getBinaryOutputStream(); // 建立输出流
				long length = blob_pi.length();
				byte[] buff = new byte[(int) length];
				is.read(buff);

				out.write(buff);
				is.close();
				out.close();
			}
			rs.close();
			System.out.println("成功对" + id + "进行导入导出");
		}
		prep2.close();
		prep.close();
		piRS.close();
		piConn.close();
		svgtoolConn.commit();
		svgtoolConn.close();
		System.out.println("导入导出完成！，所有id后都加入'nci'");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int i = JOptionPane.showConfirmDialog(null, "不是wangql不要运行此程序,点击\"取消\"退出！",
					"危险", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (i == 0){
				InputStream is = null;
				byte[] inBytes;
				System.out.println("源数据库服务器地址：");
				is = System.in;
//				while(is==null || is.available()==0){
//					System.out.println(".");
//					Thread.sleep(1000);
//				}				
				inBytes = new byte[is.available()];
				is.read(inBytes);
				originalDBServerIP = new String(inBytes);
				new TsImporter().startImport();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
