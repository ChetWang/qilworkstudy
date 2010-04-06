package com.nci.svg.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.nci.svg.sdk.bean.ResultBean;

public class SendRequestTest {
	private final static String CODE_TYPE = "UTF-8";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[][] params;

		// // 请求代码信息
		// params = new String[2][2];
		// params[0][0] = "action";
		// params[0][1] = "getSvgCodes";
		// params[1][0] = "moduleName";
		// params[1][1] = "SVG_MODULE_TYPE";
		// // 获取JPG图形
		// params = new String[2][2];
		// params[0][0] = "action";
		// params[0][1] = "transformSymbolToJpg";
		// params[1][0] = "name";
		// params[1][1] = "测试路线";
		// // 获取指定文件
		// params = new String[3][2];
		// params[0][0] = "action";
		// params[0][1] = "getGraphFile";
		// params[1][0] = "businessID";
		// params[1][1] = "1";
		// params[2][0] = "id";
		// params[2][1] = "f12b6f38-3361-4927-8b4c-7abb23ead2ce";
		// // 获取图元版本信息
		// params = new String[2][2];
		// params[0][0] = "action";
		// params[0][1] = "getSymbolsVersion";
		// params[1][0] = "owners";
		// params[1][1] = "测试1";
		// // 保存图形
		// params = new String[10][2];
		// params[0][0] = "action";
		// params[0][1] = "saveGraphFile";
		// params[1][0] = "filename";
		// params[1][1] = "周慧明测试4";
		// params[2][0] = "graphType";
		// params[2][1] = "2";
		// params[3][0] = "graphBusinessType";
		// params[3][1] = "4";
		// params[4][0] = "fileFormat";
		// params[4][1] = "svg";
		// params[5][0] = "operator";
		// params[5][1] = "zhm";
		// params[6][0] = "content";
		// params[6][1] = "测试内容sssss。。。";
		// params[7][0] = "businessID";
		// params[7][1] = "1";
		// params[8][0] = "logs";
		// params[8][1] = "测试内容11111。。。";
		// params[9][0] = "id";
		// params[9][1] = "f8445f49-808f-4557-a1ca-6f870a599c38";

		// // 测试业务模型操作类
		// params = new String[2][2];
		// params[0][0] = "action";
		// params[0][1] = "getBusinessModel2";
		// params[1][0] = "businessID";
		// params[1][1] = "1";

		// // 测试重新初始化业务模型动作缓存
		// params = new String[2][2];
		// params[0][0] = "action";
		// params[0][1] = "cacheManagermodleActions";
		// params[1][0] = "subAction";
		// params[1][1] = "init";

		// // 测试获取业务模型模拟数据
		// params = new String[2][2];
		// params[0][0] = "action";
		// params[0][1] = "getDemoData";
		// params[1][0] = "filename";
		// params[1][1] = "modelData";

		// 测试获取业务模型模拟数据
		// params = new String[3][2];
		// params[0][0] = "action";
		// params[0][1] = "getNestDistrictDevices";
		// params[1][0] = "DistrictID";
		// params[1][1] = "ca099b8c-c857-45e6-818d-bca8a24989e4";
		// params[2][0] = "businessID";
		// params[2][1] = "1";

		// 测试自动生成结构图
		params = new String[][] { { "action", "autoFrameMapping" },
				{ "type", "demo" }, { "id", "ssss" },
				{ "contextpath", "../images/formula.gif" },
				{ "sysname", "sixhas" } };

		// 测试结构图保存
		// params = new String[][] { { "action", "autoFrameMapping" },
		// { "type", "org" },
		// { "id", "ssss" },
		// { "sysname", "sixhas" }, { "issave", "save" },
		// { "content", "测试" } };
		// 测试结构图删除
		// params = new String[][] { { "action", "autoFrameMapping" },
		// { "type", "org" }, { "id", "ssss" }, { "sysname", "sixhas" },
		// { "issave", "delete" } };

		URLConnection conn = sendToServer(params);
		if (conn != null) {
			// ResultBean resultBean = recvFromServer(conn);
			// System.out.println(resultBean.getReturnObj());
			String reStr = recvFromServer2(conn);
			System.out.println("ok");
		}
	}

	/**
	 * 根据传入的连接对象，获取返回信息
	 * 
	 * @param conn
	 *            URLConnection对象
	 * @return 服务端处理的结果对象
	 */
	private static ResultBean recvFromServer(URLConnection conn) {

		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(conn
					.getInputStream()));
			return (ResultBean) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 服务器请求，返回的是字符串
	 * @param conn
	 * @return
	 */
	private static String recvFromServer2(URLConnection conn) {
		try {
			InputStream is = conn.getInputStream();
			int length = is.available();
			byte[] b = new byte[length];
			is.read(b);
			return new String(b, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static URLConnection sendToServer(Object obj) {
		try {
			URLConnection urlConn = connectServer("http://127.0.0.1:8090/svg30serv/svgservices?");
			if (urlConn != null) {
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				PrintStream output = new PrintStream(urlConn.getOutputStream());
				String[][] param = (String[][]) obj;
				if (param != null && param.length > 0) {
					StringBuffer rs = new StringBuffer();
					for (int i = 0; i < param.length; i++) {
						if (param[i][0] == null || param[i][1] == null)
							continue;
						if (i != 0) {
							rs.append("&");
						}
						rs.append(URLEncoder.encode(param[i][0], CODE_TYPE))
								.append('=').append(
										URLEncoder.encode(param[i][1],
												CODE_TYPE));
					}
					output.print(rs.toString());
				}
				output.flush();
			}
			return urlConn;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static URLConnection connectServer(String urlLocation) {
		URL url;
		try {
			url = new URL(urlLocation);
			URLConnection urlConn = url.openConnection();
			return urlConn;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
