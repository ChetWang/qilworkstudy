package com.nci.ums.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.nci.ums.v3.fee.FeeBean;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class FeeUtil {

	/**
	 * 存储所有费用信息的hashmap
	 */
	private static Map feeMap;
	
	private static byte[] lock = new byte[0];

	public static void load() {
		synchronized (lock) {
			feeMap = new ConcurrentHashMap();
			initAllFeeBeans();
		}
	}

	private static void initAllFeeBeans() {
		Connection conn = null;
		try {
			conn = Res.getConnection();
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT * FROM UMS_FEE");
			while (rs.next()) {
				FeeBean bean = new FeeBean();
				bean.setFee(rs.getInt("FEE_FEE"));
				bean.setFee_terminal_Id(rs.getString("FEE_TERMINAL"));
				bean.setFeeDesc(rs.getString("FEE_DESCRIPTION"));
				bean.setFeeServiceNO(rs.getString("FEE_SERVICE_NO"));
				bean.setFeeType(rs.getInt("FEE_TYPE"));
				feeMap.put(bean.getFeeServiceNO(), bean);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "UMS3计费信息集合获取失败");
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * @return the feeMap，key是费用代号，Object是FeeBean
	 */
	public static Map getFeeMap() {
		return feeMap;
	}

	/**
	 * @param feeMap
	 *            the feeMap to set，key是费用代号，Object是FeeBean
	 */
	public synchronized static void setFeeMap(Map feeMap) {
		FeeUtil.feeMap = feeMap;
	}

}
