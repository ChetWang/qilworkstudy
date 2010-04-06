package nci.gps.client;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GpsClient {
	//服务URL
	public static String wsAddress = "http://10.147.217.60:8080/callcenter/services/MyService";
	//调用的方法名
	public static String[] funName = new String[]{"query","insert"};
/**
 * 构造函数
 *
 */
	public GpsClient(){
		init();
	}
	/**
	 * 初始化
	 */
	public static void init(){
		ResourceBundle rb = ResourceBundle.getBundle("nci.gps.client.DBJndi");
		wsAddress = rb.getString("LSWebservices");
	}
/**
 * 调用客服的webservices
 */
	public boolean callServices(String wsAddress,String funName,String[] parm,String[] value){
		boolean returnValue = false;
		BlockingClient bc = new BlockingClient();
		bc.setWsAddress(wsAddress);
		bc.setFunName(funName);
		
		returnValue = bc.connect(parm, value);
		//System.out.println(bc.getList().get(0));
		return returnValue;
	}
	/**
	 * 调用客服的webservices
	 */
		public String callServices(String wsAddress,String funName,String xml){
			String returnValue = "";
			BlockingClient bc = new BlockingClient();
			bc.setWsAddress(wsAddress);
			bc.setFunName(funName);
			
			//bc.connect(parm, value);
			//System.out.println(bc.getList().get(0));
			return returnValue;
		}
	/**
	 * 传入的变量
	 * @return
	 */
	public String[] getParm(){
		String[] parm = { "serviceName","CS","arg0","arg1","arg2","arg3","arg4","arg5","arg6","arg7",
				"arg8","arg9","arg10","arg11","arg12","arg13"};
		return parm;
	}
	/**
	 * 传入的数据
	 * @return
	 */
	public String[] getValue(String[] val,String[] name){
		String[] parm = null;
		Map map = new HashMap();
		for(int i=0;i<name.length;i++){
			map.put(name[i],val[i]);
		}
			//if("LCZT".equals(name[i])){
		String lczt = (String)map.get("CS");
			if("1".equals(lczt)){
				//派发
				parm = getPGValue(map);
			}else if("2".equals(lczt)){
//				到达现场
				parm = getDdxcValue(map);
			}else if("3".equals(lczt)){
//				抢修中
				parm = getQxzValue(map);
			}else if("4".equals(lczt)){
//				处理完毕
				parm = getGzclwcValue(map);
			}else if("5".equals(lczt)){
//				确认
				parm = getGzqrValue(map);
			}else if("10".equals(lczt)){
//				完成
					
			}
		//}
		
		return parm;
	}
	/**
	 * 接单派工
	 * @param val
	 * @return
	 */
	public String[] getPGValue(Map map){
		String[] value = { "SET_CC_GZ" ,"1",
				checkNull((String) map.get("GDH")),checkNull((String) map.get("CLBM")),checkNull((String) map.get("JDSJ")),checkNull((String) map.get("FZBM")),checkNull((String) map.get("PCSJ")),
						checkNull((String) map.get("PCRY")),checkNull((String) map.get("CLYJ")),checkNull((String) map.get("JDRY")),checkNull((String) map.get("XGCLBM")),checkNull((String) map.get("CLSJ")),"","","",""};
		return value;
	}
	/**
	 * 到达现场
	 * @param map
	 * @return
	 */
	public String[] getDdxcValue(Map map){
		String[] value = { "SET_CC_GZ" ,"2",
				checkNull((String) map.get("DDXCSJ")),checkNull((String) map.get("GDH")),checkNull((String) map.get("CLRY")),checkNull((String) map.get("CLBM")),checkNull((String) map.get("XGCLBM")),
						checkNull((String) map.get("JDSJ")),checkNull((String) map.get("CLSJ")),checkNull((String) map.get("CLYJ")),"","","","","",""};
		return value;
	}
	/**
	 * 抢修中
	 * @param map
	 * @return
	 */
	public String[] getQxzValue(Map map){
		String[] value = { "SET_CC_GZ" ,"3",
				checkNull((String) map.get("CLYJ")),checkNull((String) map.get("GDH")),checkNull((String) map.get("CLRY")),checkNull((String) map.get("CLBM")),checkNull((String) map.get("XGCLBM")),
						checkNull((String) map.get("JDSJ")),checkNull((String) map.get("CLSJ")),"","","","","","",""};
		return value;
	}
	/**
	 * 故障处理完成
	 * @param map
	 * @return
	 */
	public String[] getGzclwcValue(Map map){
		String[] value = { "SET_CC_GZ" ,"4",
				checkNull((String) map.get("GZDY")),checkNull((String) map.get("GZLB")),checkNull((String) map.get("GZQY")),checkNull((String) map.get("GZFL")),checkNull((String) map.get("GZYY")),
						checkNull((String) map.get("WCSJ")),checkNull((String) map.get("GZDD")),checkNull((String) map.get("CLJG")),checkNull((String) map.get("JSSJ")),checkNull((String) map.get("GDH")),checkNull((String) map.get("CLRY")),checkNull((String) map.get("CLBM")),checkNull((String) map.get("JDSJ")),checkNull((String) map.get("XGCLBM"))};
		return value;
	}
	/**
	 * 故障确认
	 * @param map
	 * @return
	 */
	public String[] getGzqrValue(Map map){
		String[] value = { "SET_CC_GZ" ,"5",
				checkNull((String) map.get("GZQRYJ")),checkNull((String) map.get("GDH")),checkNull((String) map.get("CLRY")),checkNull((String) map.get("CLBM")),checkNull((String) map.get("XGCLBM")),
						checkNull((String) map.get("JDSJ")),checkNull((String) map.get("CLSJ")),"","","","","","",""};
		return value;
	}
	/**
	 * 退单,目前没有该功能
	 * @param val
	 * @return
	 */
	public String[] getTdValue(Map map){
		String[] value = { "SET_CC_TD" ,"6",
				"","","","","",
				"","","","","","","","",""};
		return value;
	}
	/**
	 * 如果是NULL则转化为空
	 */
	public String checkNull(String temp){
		return temp==null?"":temp.trim();
	}
}
