package nci.gps;

import nci.gps.message.StaticUnPackageMessage;
import nci.gps.util.MsgLogger;

/**
 * ���ݿ������еĹ����룬��ȡ��Ӧ�Ĳ���
 * 
 * @author Qil.Wong
 * 
 */
public class FunctionCodeCommunicator {
	TransactorServer server;

	public FunctionCodeCommunicator(TransactorServer server) {
		this.server = server;
	}

	public void parse(byte functionCode, byte[] oneFrame) {
		switch (functionCode) {
		case 0x00:// �м�
			System.out.println("�м�");
			break;
		case 0x01:// ����ǰ��Ϣ
			System.out.println("����ǰ��Ϣ");
			break;
		case 0x02:// ����������
			System.out.println("����������");
			break;
		case 0x04:// �������־
			System.out.println("�������־");
			break;
		case 0x08:// д�������
			System.out.println("д�������");
			break;
		case 0x09:// �쳣�澯
			System.out.println("�쳣�澯");
			break;
		case 0x0A:// �澯ȷ��
			System.out.println("�澯ȷ��");
			break;
		case 0x21:// ��¼
			System.out.println("��¼");
			break;
		case 0x22:// ��¼�˳�
			System.out.println("��¼�˳�");
			break;
		case 0x24:// �������
			System.out.println("�������");
			break;
		case 0x28:// �����Ͷ���
			System.out.println("�����Ͷ���");
			break;
		case 0x29:// �յ������ϱ�
			System.out.println("�յ������ϱ�");
			break;
		}
		// ���쳣����
		if (StaticUnPackageMessage.getIsErrorCode(oneFrame)) {
			byte[] data = StaticUnPackageMessage.getData(oneFrame);
			parseError(data[0]);
		}

	}

	private void parseError(byte errorCode) {
		switch (errorCode) {
		case 0x00: { // ����
			MsgLogger.log(MsgLogger.INFO, "������");
			break;
		}
		case 0x01: {// �м�����û�з��أ�
			MsgLogger.log(MsgLogger.INFO, "�м�����û�з��أ�");
			break;
		}
		case 0x02: {// �������ݷǷ�
			MsgLogger.log(MsgLogger.INFO, "�������ݷǷ���");
			break;
		}
		case 0x03: {// ����Ȩ�޲���
			MsgLogger.log(MsgLogger.INFO, "����Ȩ�޲��㣡");
			break;
		}
		case 0x04: {// �޴�������
			MsgLogger.log(MsgLogger.INFO, "�޴������ݣ�");
			break;
		}
		case 0x11: {// Ŀ���ַ������
			MsgLogger.log(MsgLogger.INFO, "Ŀ���ַ�����ڣ�");
			break;
		}
		case 0x12: {// ����ʧ��
			MsgLogger.log(MsgLogger.INFO, "����ʧ�ܣ�");
			break;
		}
		case 0x13: {// ����Ϣ̫֡��
			MsgLogger.log(MsgLogger.INFO, "����Ϣ̫֡����");
			break;
		}
		default:{
			MsgLogger.log(MsgLogger.INFO, "�޷�ʶ��Ĵ����룡"+errorCode);
			break;
		}
		}
	}

}
