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
	private int K16 = 16 * 1024;	// 16K������ֵ
	private int MULTI_FRAME = 7;	// ��֡����ֵ

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
	 * ���յ���Ϣ������
	 * @param msg BytesMessage ��Ϣ֡
	 * @throws OutofBytesMessageIndexException 
	 */
	public void processMsg(BytesMessage msg) throws OutofBytesMessageIndexException {
		try {
			int msgLength = (int) msg.getBodyLength();
			if(msgLength > 65548) {
				// ������Ϣ���ȳ���64k+13���ֽ�
				MsgLogger.log(MsgLogger.ERROR, "���յ���Ϣ���ȳ������ƣ�");
				throw new OutofBytesMessageIndexException();
			}
			
			// ��ȡ��Ϣ֡�ֽ���
			byte[] message = new byte[msgLength];
			msg.readBytes(message);
			MsgLogger.log(MsgLogger.INFO,"���յ�������֡��"+CharCoding.byte2hex(message));
			
			// ��ȡ�ն��߼���ַ
			byte[] logicalAddress = StaticUnPackageMessage
					.getLogicalAddress(message);
			
			// ��ȡ����֡�Ƿ����ն�
			// ���������ֱ�ӷ���
			if(StaticUnPackageMessage.isToTerminal(message))
				return ;
			
			// ��ȡ����֡��ˮ��
			int no = StaticUnPackageMessage.getFSEQ(message);
			// ��ȡ������
			byte[] data = StaticUnPackageMessage.getData(message);
			// ��ȡ�ڲ���������
			byte methodNo = data[0];
			// ��ȡ����
			byte[] bParams = StaticUnPackageMessage.subMessage(data, 1,
					data.length - 1);
			String params = new String(bParams);
			// ��������֡Ψһ��ʶ (�ն��߼���+��ˮ��)
			String messageKey = CharCoding.byte2hex(logicalAddress)+Integer.toString(no);

//			if(methodNo == (byte)0xFE)
//				MsgLogger.log(MsgLogger.INFO, "�ն�ȷ�������յ�֡��"+ CharCoding.byte2hex(message));

			// ���ݲ������ص��ַ���
			String strResult = doData(methodNo, params, messageKey);
			
			// *********************
			// �жϷ����ַ����Ƿ񳬹�16K
			// û�����͵�֡����
			// ��������7֡����
			// *********************
			if (strResult.length() <= K16) { 	// ���͵�֡����	
			// ��ȡ�������ݵ�������
				byte[] bResult = strResult.getBytes();
				byte[] rData = new byte[1 + bResult.length];
				rData[0] = data[0];
				for (int i = 0, size = bResult.length; i < size; i++) {
					rData[1 + i] = bResult[i];
				}

				/**
				 * ���ɷ�������֡
				 */
				PackageMessage pm = new PackageMessage();
				// ���ÿ����룬�û��Զ���0FH
				pm.setControlCode(false, false, 15);
				// ����������
				pm.setData(rData);
				// ������վ��ַ���������
				// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
				pm.setMstaSeq(0, no);
				// �����ն��߼���ַ
				pm.setLogicalAddress(logicalAddress);
				// ��ȡ��������֡
				byte[] bResultData = pm.packageMessage();

				// ���ͷ���֡
				Producer001.getInstance().addBytesIntoSendQueue(bResultData);
//				MsgLogger.log(MsgLogger.INFO, "��������֡:"+CharCoding.byte2hex(bResultData));
			} else if(strResult.length() <= MULTI_FRAME * K16){	// ����7֡����
				// 7֡����Ҫ��ȫ
				
				// �����ַ�����ÿ��16K��С��ȡ
				String[] strDatas = new String[MULTI_FRAME];
				int i;
				for (i = 0; i < MULTI_FRAME && (i+1)*K16 < strResult.length(); i++) {
					strDatas[i] = strResult.substring(i*K16, (i+1)*K16);
				}
				strDatas[i] = strResult.substring(i*K16);
				
				// ����ȡ���ַ���ȫ�����ͳ�ȥ
				// ����ַ���Ϊ��Ҳ���ͳ�ȥ
				for (i = 0; i < MULTI_FRAME; i++) {
					
					/**
					 * ����������
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
					 * ���ɷ�������֡
					 */
					PackageMessage pm = new PackageMessage();
					// ���ÿ����룬�û��Զ���0FH
					pm.setControlCode(false, false, 15);
					// ����������
					pm.setData(rData);
					// ������վ��ַ���������
					// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
					pm.setMstaSeq(i+1, no);
					// �����ն��߼���ַ
					pm.setLogicalAddress(logicalAddress);
					// ��ȡ��������֡
					byte[] bResultData = pm.packageMessage();

					// ���ͷ���֡
					Producer001.getInstance().addBytesIntoSendQueue(bResultData);
					MsgLogger.log(MsgLogger.INFO, "��֡����������֡:"+CharCoding.byte2hex(bResultData));
				}
				
			} else{
				MsgLogger.log(MsgLogger.INFO, "�������ݳ��ȳ���7��16K�ĳ��ȣ�");
			}
		} catch (JMSException e) {
			MsgLogger.logExceptionTrace(getConsumerID()+"������Ϣ�����������ֽ��鱨��", e);
			e.printStackTrace();
		}
		catch (NoSubjectException e) {
			MsgLogger.logExceptionTrace(getConsumerID()+"���ͷ���֡ʱ����,���������.", e);
			e.printStackTrace();
		} catch (ProducerNotRunningException e) {
			// TODO Auto-generated catch block
			MsgLogger.logExceptionTrace(getConsumerID()+"���ͷ���֡ʱ����,��Ӧ���������Ѿ�ֹͣ����.", e);
			e.printStackTrace();
		}
		
	}

	/**
	 * �����ڲ�����ţ�������Ӧ���ݴ���������xml��ʽ�Ĵ�����
	 * @param methodNo byte �ڲ������
	 * @param params String ����
	 * @param messageKey String ����֡Ψһ��ʶ
	 * @return xml��ʽ�Ĵ�����
	 */
	private String doData(byte methodNo, String params, String messageKey){
		// ���ݲ���������
		DataServices dataServices = new DataServices(messageKey);
		// ���ݲ������ص��ַ���
		String strResult = null;
		switch (methodNo) {
		case 0x01:
			 // ��ȡ���������б�
			strResult = dataServices.selectLSCode();
			break;
		case 0x02:
			// ��ȡ�����б�
			strResult = dataServices.selectLSDept();
			break;
		case 0x03:
			// ��ȡ��Ա�б�
			strResult = dataServices.selectLSPerson();
			break;
		case 0x04:
			// ��ȡ��ʻԱ�б�
			strResult = dataServices.selectDriver();
			break;
		case 0x05:
			// ��ȡ�����б�
			strResult = dataServices.selectLSCar();
			break;
		case 0x06:
			// ��ȡ�����б�
			strResult = dataServices.selectGdInfo(params);
			break;
		case 0x07:
			// ���¹����б�
			strResult = dataServices.updateGdInfo(params);
			break;
		case 0x08:
			// �½������б�
			strResult = dataServices.createGdInfo(params);
			break;
		case 0x09:
			// �½�����������Ϣ
			strResult = dataServices.createGdclgcInfo(params);
			break;
		case 0x0A:
			// �½�������Ϣ
			strResult = dataServices.createClzbInfo(params);
			break;
		case 0x0B:
			// ����95598����
			strResult = dataServices.setLsWebServer(params);
			break;
		case 0x0C:
			// ��ȡϵͳʱ��
			strResult = dataServices.getCurrentTime();
			break;
		case 0x0D:
			// �ϴ���¼��Ϣ
			strResult = dataServices.createLoginInfo(params);
			break;
		case 0x0E:
			// ���³�����Ϣ
			strResult = dataServices.deleteLoginInfo(params);
			break;
		case 0x0F:
			// �˳���¼��Ϣ
			strResult = dataServices.updateLoginInfo(params);
			break;
		case 0x10:
			// �����ն���Ϣ
			strResult = dataServices.updateTerminal(params);
			break;
		case 0x11:
			// ��ȡ�ն���Ϣ
			strResult = dataServices.selectTerminal();
			break;
		case (byte) 0xFE:
			// �ն˽��յ�����֡��ȷ�ϲ���
			strResult = dataServices.doConfirm();
			break;
		default:
			break;
		}
//		MsgLogger.log(MsgLogger.INFO, "�����ַ�����"+strResult);
		return strResult;
	}

}
