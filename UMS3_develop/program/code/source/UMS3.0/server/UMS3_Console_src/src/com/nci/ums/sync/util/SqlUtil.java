package com.nci.ums.sync.util;



public class SqlUtil {
	public static final int STRING 	= 0;
	public static final int INTEGER	= 1;
	public static final int DATE	= 2;
	/**
	 * insert into a values(?,?,?)
	 * @param sql
	 * @param values
	 * @param types
	 * @return
	 */
	public static String getSql(String sql,String values[], int types[]) {
        if(sql == null || values == null || types == null || values.length != types.length || !check(sql,values.length)) {
            throw new IllegalArgumentException("非法参数");
        }
		StringBuffer resultsql 	= new StringBuffer();
		int index 			= 0;
		for(int i = 0; i < sql.length(); i++){
			if(sql.charAt(i) == '?'){
                resultsql.append(getEachString(values[index],types[index]));
                index++;
			} else {
			    resultsql.append(sql.charAt(i));
            }
		}
		System.out.println(resultsql.toString());
		return resultsql.toString();
	}
    public static String getSql(String sql,String values[]) {
        if( values == null ) {
            throw new IllegalArgumentException("非法参数");
        }
        int[] types = new int[values.length];
        for(int i = 0; i < types.length; i++) {
            types[i] = SqlUtil.STRING;
        }
        return getSql(sql,values,types);
    }
    
	private static boolean check(String sql,int count) {
        int sqlCount = 0;
        for(int i = 0; i < sql.length(); i++){
            if(sql.charAt(i) == '?'){
               sqlCount++;
            }
        }
        return sqlCount == count;
    }
	private static String getEachString(String value, int type) {
		String result = "null";
		switch (type) {
		case SqlUtil.INTEGER: //数字型
			result = value==null||"".equalsIgnoreCase(value)?"0":value;
			break;
		case SqlUtil.STRING: //字符型
			result = value==null?result:"'" + value + "'";
			break;
		case SqlUtil.DATE: //时间型
			result = value==null||"".equalsIgnoreCase(value)||"0000-00-00".equalsIgnoreCase(value)?result:"to_date('" + value + "','yyyy-mm-dd')";
            break;
		default:
            throw new IllegalArgumentException("非法参数");
		}
		return result;
	}

	public static void main(String[] args) {
		String values[]   ={"11","2","32"};
		int types[]       ={SqlUtil.STRING,SqlUtil.INTEGER,SqlUtil.DATE};
		String sql = "insert into values(?,?,?)";
		System.out.println(SqlUtil.getSql(sql,values,types));
	}
}
