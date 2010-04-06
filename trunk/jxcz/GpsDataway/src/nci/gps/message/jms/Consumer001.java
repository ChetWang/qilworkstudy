package nci.gps.message.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

import nci.gps.data.DataServices;
import nci.gps.log.MsgLogger;
import nci.gps.message.PackageMessage;
import nci.gps.message.StaticUnPackageMessage;
import nci.gps.util.CharCoding;


public class Consumer001 extends AbstractComsumer{
	
	private static Consumer001 consumer;
	private int K16 = 16 * 1024;	// 16K的整数值
	private int MULTI_FRAME = 7;	// 多帧个数值

	private Consumer001() throws NoSubjectException {
		super();
	}
	
	public static Consumer001 getInstance() throws NoSubjectException{
		if(consumer == null)
			consumer = new Consumer001();
		return consumer;
	}

	public String getConsumerID() {
		return "consumer001";
	}

	/**
	 * 接收到消息后处理函数
	 * @param msg BytesMessage 消息帧
	 * @throws OutofBytesMessageIndexException 
	 */
	public void processMsg(BytesMessage msg) throws OutofBytesMessageIndexException {
		try {
			int msgLength = (int) msg.getBodyLength();
			if(msgLength > 65548) {
				// 整个消息长度超出64k+13个字节
				MsgLogger.log(MsgLogger.ERROR, "接收到消息长度超出限制！");
				throw new OutofBytesMessageIndexException();
			}
			
			// 获取消息帧字节组
			byte[] message = new byte[msgLength];
			msg.readBytes(message);
			MsgLogger.log(MsgLogger.INFO,"接收到的数据帧："+CharCoding.byte2hex(message));
			
			// 获取终端逻辑地址
			byte[] logicalAddress = StaticUnPackageMessage
					.getLogicalAddress(message);
			
			// 获取数据帧是否发现终端
			// 如果不是则直接返回
			if(StaticUnPackageMessage.isToTerminal(message))
				return ;
			
			// 获取数据帧流水号
			int no = StaticUnPackageMessage.getFSEQ(message);
			// 获取数据域
			byte[] data = StaticUnPackageMessage.getData(message);
			// 获取内部用命令编号
			byte methodNo = data[0];
			// 获取参数
			byte[] bParams = StaticUnPackageMessage.subMessage(data, 1,
					data.length - 1);
			String params = new String(bParams);
			// 生成数据帧唯一标识 (终端逻辑号+流水号)
			String messageKey = CharCoding.byte2hex(logicalAddress)+Integer.toString(no);

//			if(methodNo == (byte)0xFE)
//				MsgLogger.log(MsgLogger.INFO, "终端确认数据收到帧："+ CharCoding.byte2hex(message));

			// 数据操作返回的字符串
			String strResult = doData(methodNo, params, messageKey);
			
			// *********************
			// 判断返回字符串是否超过16K
			// 没有则发送单帧数据
			// 操作则发生7帧数据
			// *********************
			if (strResult.length() <= K16) { 	// 发送单帧数据	
			// 获取返回数据的数据域
				byte[] bResult = strResult.getBytes();
				byte[] rData = new byte[1 + bResult.length];
				rData[0] = data[0];
				for (int i = 0, size = bResult.length; i < size; i++) {
					rData[1 + i] = bResult[i];
				}

				/**
				 * 生成返回数据帧
				 */
				PackageMessage pm = new PackageMessage();
				// 设置控制码，用户自定义0FH
				pm.setControlCode(false, false, 15);
				// 设置数据域
				pm.setData(rData);
				// 设置主站地址与命令序号
				// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
				pm.setMstaSeq(0, no);
				// 设置终端逻辑地址
				pm.setLogicalAddress(logicalAddress);
				// 获取返回数据帧
				byte[] bResultData = pm.packageMessage();

				// 发送返回帧
				Producer001.getInstance().addBytesIntoSendQueue(bResultData);
//				MsgLogger.log(MsgLogger.INFO, "返回数据帧:"+CharCoding.byte2hex(bResultData));
			} else if(strResult.length() <= MULTI_FRAME * K16){	// 发送7帧数据
				// 7帧数据要发全
				
				// 将长字符串按每个16K大小截取
				String[] strDatas = new String[MULTI_FRAME];
				int i;
				for (i = 0; i < MULTI_FRAME && (i+1)*K16 < strResult.length(); i++) {
					strDatas[i] = strResult.substring(i*K16, (i+1)*K16);
				}
				strDatas[i] = strResult.substring(i*K16);
				
				// 将截取的字符串全部发送出去
				// 如果字符串为空也发送出去
				for (i = 0; i < MULTI_FRAME; i++) {
					
					/**
					 * 生产数据域
					 */
					byte[] rData;
					if(strDatas[i] != null) {
						byte[] bResult = strDatas[i].getBytes();
						rData = new byte[1 + bResult.length];
						rData[0] = data[0];
						for (int j = 0, size = bResult.length; j < size; j++) {
							rData[1 + j] = bResult[j];
						}
					}else{
						rData = new byte[1];
						rData[0] = data[0];
					}
					
					/**
					 * 生成返回数据帧
					 */
					PackageMessage pm = new PackageMessage();
					// 设置控制码，用户自定义0FH
					pm.setControlCode(false, false, 15);
					// 设置数据域
					pm.setData(rData);
					// 设置主站地址与命令序号
					// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
					pm.setMstaSeq(i+1, no);
					// 设置终端逻辑地址
					pm.setLogicalAddress(logicalAddress);
					// 获取返回数据帧
					byte[] bResultData = pm.packageMessage();

					// 发送返回帧
					Producer001.getInstance().addBytesIntoSendQueue(bResultData);
					MsgLogger.log(MsgLogger.INFO, "多帧，返回数据帧:"+CharCoding.byte2hex(bResultData));
				}
				
			} else{
				MsgLogger.log(MsgLogger.INFO, "返回数据长度超出7×16K的长度！");
			}
		} catch (JMSException e) {
			MsgLogger.logExceptionTrace(getConsumerID()+"接收消息处理函数接收字节组报错！", e);
			e.printStackTrace();
		}
		catch (NoSubjectException e) {
			MsgLogger.logExceptionTrace(getConsumerID()+"发送返回帧时报错,无相关主题.", e);
			e.printStackTrace();
		} catch (ProducerNotRunningException e) {
			// TODO Auto-generated catch block
			MsgLogger.logExceptionTrace(getConsumerID()+"发送返回帧时报错,对应的生产者已经停止运行.", e);
			e.printStackTrace();
		}
		
	}

	/**
	 * 根据内部命令号，进行相应数据处理，并返回xml格式的处理结果
	 * @param methodNo byte 内部命令号
	 * @param params String 参数
	 * @param messageKey String 数据帧唯一标识
	 * @return xml格式的处理结果
	 */
	private String doData(byte methodNo, String params, String messageKey){
		// 数据操作请求类
		DataServices dataServices = new DataServices(messageKey);
		// 数据操作返回的字符串
		String strResult = null;
		switch (methodNo) {
		case 0x01:
			 // 获取公共代码列表
			strResult = dataServices.selectLSCode();
			break;
		case 0x02:
			// 获取部门列表
			strResult = dataServices.selectLSDept();
			break;
		case 0x03:
			// 获取人员列表
			strResult = dataServices.selectLSPerson();
			break;
		case 0x04:
			// 获取驾驶员列表
			strResult = dataServices.selectDriver();
			break;
		case 0x05:
			// 获取车辆列表
			strResult = dataServices.selectLSCar();
			break;
		case 0x06:
			// 获取工单列表
			strResult = dataServices.selectGdInfo(params);
			break;
		case 0x07:
			// 更新工单列表
			strResult = dataServices.updateGdInfo(params);
			break;
		case 0x08:
			// 新建工单列表
			strResult = dataServices.createGdInfo(params);
			break;
		case 0x09:
			// 新建工单处理信息
			strResult = dataServices.createGdclgcInfo(params);
			break;
		case 0x0A:
			// 新建坐标信息
			strResult = dataServices.createClzbInfo(params);
			break;
		case 0x0B:
			// 调用95598服务
			strResult = dataServices.setLsWebServer(params);
			break;
		case 0x0C:
			// 获取系统时间
			strResult = dataServices.getCurrentTime();
			break;
		case 0x0D:
			// 上传登录信息
			strResult = dataServices.createLoginInfo(params);
			break;
		case 0x0E:
			// 更新车辆信息
			strResult = dataServices.deleteLoginInfo(params);
			break;
		case 0x0F:
			// 退出登录信息
			strResult = dataServices.updateLoginInfo(params);
			break;
		case 0x10:
			// 更新终端信息
			strResult = dataServices.updateTerminal(params);
			break;
		case 0x11:
			// 获取终端信息
			strResult = dataServices.selectTerminal();
			break;
		case (byte) 0xFE:
			// 终端接收到数据帧的确认操作
			strResult = dataServices.doConfirm();
			break;
		default:
			break;
		}
//		MsgLogger.log(MsgLogger.INFO, "返回字符串："+strResult);
		return strResult;
	}

}
