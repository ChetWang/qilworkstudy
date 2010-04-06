package nci.gps.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

/**
 * 
 * 阻断式调用客户端
 * 
 * @author rake
 * 
 */
public class BlockingClient {

	private int pointer = -1; // 指针位置

	private List list;// 结果集

	private EndpointReference wsAddress = null;// webservice地址

	public BlockingClient() {

	}

	private String funName = "";

	/**
	 * 将当前指针位置向右移动一位
	 * 
	 * @return
	 */
	public boolean next() {
		if (list != null && pointer < (list.size() - 1) && list.size() > 0) {
			pointer++;
			return true;
		} else
			return false;
	}

	/**
	 * 获取数据集行数
	 * 
	 * @return
	 */
	public int getRowNum() {
		if (list != null) {
			return list.size();
		} else
			return 0;
	}

	/**
	 * 
	 * 获取数据集列数(入参为当前行数)
	 * 
	 * @return
	 */
	public int getColNum(int i) {
		int colNum = 0;
		if (i < 0) {
			return colNum;
		}
		if (list != null && i < list.size()) {
			HashMap map = (HashMap) list.get(i);
			if (map != null) {
				colNum = map.keySet().size();
			}
		}
		return colNum;
	}

	public int getColNum() {
		return getColNum(0);
	}

	/**
	 * 获取字段名,以字符串数组方式返回
	 * 
	 * @param row
	 * @return
	 */
	public String[] getColName(int row) {
		String[] str = null;
		if (row < 0) {
			return str;
		}
		if (list != null && row < list.size()) {
			HashMap map = (HashMap) list.get(row);
			Collection collection = map.keySet();
			str = new String[collection.size()];
			Iterator iterator = collection.iterator();
			for (int i = 0; i < str.length; i++) {
				if (iterator.hasNext()) {
					str[i] = (String) iterator.next();
				}
			}

		}
		return str;
	}

	public String[] getColName() {
		return getColName(0);
	}

	/**
	 * 
	 * @return 返回该字段值
	 */
	public String getField(String fieldName, int row) {
		String fieldValue = "";
		if (row < 0) {
			return fieldValue;
		}
		if (list != null && row < list.size()) {
			HashMap map = (HashMap) list.get(row);
			fieldValue = (String) map.get(fieldName);
		}
		return fieldValue;
	}

	/**
	 * 返回当前行的HashMap
	 * 
	 * @param i
	 * @return
	 */

	public Map getRow(int i) {
		if (i < 0) {
			return null;
		} else if (list != null && i < list.size()) {
			return (Map) list.get(i);
		}
		return null;

	}

	/**
	 * 返回该字段值
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getField(String fieldName) {
		pointer = 0;
		return getField(fieldName, pointer);
	}

	/**
	 * 获取总记录数
	 * 
	 * @param parm
	 * @param value
	 * @return
	 */
	public int getCount(String[] parm, String[] value) {
		int count = 0;
		String[] parmNew = new String[parm.length + 1];
		String[] valueNew = new String[value.length + 1];
		for (int i = 0; i < parm.length; i++) {
			parmNew[i] = parm[i];
			valueNew[i] = value[i];
		}
		parmNew[parmNew.length - 1] = "COUNT";
		valueNew[valueNew.length - 1] = "";
		this.connect(parmNew, valueNew);
		if (this.next()) {
			count = Integer.parseInt(this.getField("COUNT"));
		}
		return count;
	}

	/**
	 * 连结服务端
	 * 
	 * @return 无出错返回true
	 */
	public boolean connect(String[] parm, String[] value) {
		pointer = -1;
		list = new ArrayList();

		OMElement payload = null;
		if (parm != null && value != null) {
			if (parm.length != value.length) {
				return false;
			}
			if ((funName == null) || (funName.compareTo("") == 0)) {
				payload = ClientUtil.getQueryOMElement(parm, value);
			} else {
				payload = ClientUtil.getQueryOMElement(parm, value, funName);
			}
		}
		Options options = new Options();
		options.setTo(wsAddress);
		ServiceClient sender;
		try {
			sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);
			//System.out.println("lzb result = "+result);
			//System.out.println("lzb dbset = "+payload);
			
			OMElement dbset = result.getFirstChildWithName(new QName("DBSET"));
			//System.out.println("lzb dbset = "+dbset);
			
			if (dbset != null) {
				Iterator iterator = dbset.getChildrenWithName(new QName("ROW"));
				if (iterator != null) {
					while (iterator.hasNext()) {
						OMElement row = (OMElement) iterator.next();
						Iterator ite = row.getChildren();
						if (ite != null) {
							HashMap hashMap = new HashMap();
							while (ite.hasNext()) {
								OMElement omData = (OMElement) ite.next();
								hashMap.put(omData.getLocalName(), omData
										.getText());

							}
							list.add(hashMap);
						}
					}
				}
			}
			return true;
		} catch (AxisFault e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 连结服务端
	 * 
	 * @return 无出错返回true
	 */
	public boolean connectDataExchange() {
		pointer = -1;
		list = new ArrayList();

		OMElement payload = null;

		
		payload = ClientUtil.getDataChangeOMElement();

		Options options = new Options();
		options.setTo(wsAddress);
		ServiceClient sender;
		try {
			sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);
			OMElement dbset = result.getFirstChildWithName(new QName("DBSET"));
			if (dbset != null) {
				Iterator iterator = dbset.getChildrenWithName(new QName("ROW"));
				if (iterator != null) {
					while (iterator.hasNext()) {
						OMElement row = (OMElement) iterator.next();
						Iterator ite = row.getChildren();
						if (ite != null) {
							HashMap hashMap = new HashMap();
							while (ite.hasNext()) {
								OMElement omData = (OMElement) ite.next();
								hashMap.put(omData.getLocalName(), omData
										.getText());
							}
							list.add(hashMap);
						}
					}
				}
			}
			return true;
		} catch (AxisFault e) {
			e.printStackTrace();
			return false;
		}
	}

	/*private static EndpointReference targetEPR = new EndpointReference(
			"http://sw/osd/services/TestService");*/

	
	public static void main(String[] args){
		BlockingClient bc = new BlockingClient();
		bc.setWsAddress("http://10.147.217.60:8080/callcenter/services/MyService");
		bc.setFunName("query");
		String[] parm = { "serviceName","CS","arg0","arg1","arg2","arg3","arg4","arg5","arg6","arg7",
				"arg8","arg9","arg10","arg11","arg12","arg13"};
		String[] value = { "SET_CC_GZ" ,"1",
				"888888","1335200000000","2007-01-01 12:00:22","0335200020000","2007-01-01 12:00:22",
				"816","NCI_test","816","0335200020000","2007-01-01 12:00","","","",""};
		bc.connect(parm, value);
		System.out.println(bc.getList().get(0));
	}
	public void setWsAddress(String wsAddress) {
		this.wsAddress = new EndpointReference(wsAddress);
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

}