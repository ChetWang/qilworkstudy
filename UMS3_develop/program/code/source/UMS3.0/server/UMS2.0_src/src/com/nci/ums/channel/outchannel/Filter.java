package com.nci.ums.channel.outchannel;

import com.nci.ums.util.DBConnect;
import com.nci.ums.util.Res;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 过滤模块
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Filter {
	public static List filters;

	static {
		if (filters == null) {
			filters = getFilters();
		}

	}

	/*
	 * 
	 */
	public static boolean filter(String content) {
		boolean result = false;
		if (filters == null) {
			result = true;
		} else {
			result = checkFilter(content, filters);
		}

		return result;
	}

	private static boolean checkFilter(String content, List filters) {
		boolean result = false;
		Iterator iterator = filters.iterator();
		// 循环判断发送内容是否包含需过滤消息
		while (iterator.hasNext()) {
			FilterInfo filterInfo = (FilterInfo) iterator.next();
			// 若包含需过滤信息，退出检查同时返回为真
			// System.out.println("需过滤内容:"+filterInfo.getContent());
			if (content.indexOf(filterInfo.getContent()) >= 0) {
				result = true;
				break;
			}
		}

		return result;
	}

	/*
	 * 取得需过滤消息
	 */
	public static List getFilters() {
		List result = Collections.synchronizedList(new ArrayList());
		DBConnect db = null;
		ResultSet rs = null;
		try {
			db = new DBConnect();
			rs = db.executeQuery("select * from filter where statusFlag=0");
			while (rs.next()) {
				FilterInfo filterInfo = new FilterInfo();
				filterInfo.setContent(rs.getString("content"));
				result.add(filterInfo);
			}
		} catch (Exception e) {
			Res.log(Res.ERROR, "数据库链接出错!" + e);
			Res.logExceptionTrace(e);
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Res.log(Res.ERROR, "Filter关闭数据库链接出错!" + e);
				Res.logExceptionTrace(e);
			}
		}
		return result;
	}

}