package com.nci.domino.help;

/**
 * @author ������
 * ���ﶨ����һϵ�еĴ����
 */
public class WorkFlowCode {
	public static String[][] eventTrigerType = new String[][] { { "PROCESS_START", "���̴���" }, { "PROCESS_End", "�������" },
			{ "ACTIVITY_CREATE", "�����" }, { "ACTIVITY_COMPLETE", "����" } };//�¼�����ʱ��
	public static String[][] eventMode = new String[][] { { "SYNCHRONIZATION", "ͬ��" }, { "ASYNCHRONISM", "�첽" } };//�¼�ģʽ
	public static String[][] participantType = new String[][] { { "2", "��Ա" }, { "3", "����" }, { "1", "��ɫ" }, { "9", "����" } };//����������
	public static String[][] activityJoinModeList = new String[][] { { "XOR", "��" }, { "AND", "��" } };//�ۺ�ģʽ
	public static String[][] activitySplitModeList = new String[][] { { "XOR", "��" }, { "AND", "��" } };//��֧ģʽ
	public static String[][] processScopeList = new String[][] { { "0", "����" }, { "1", "˽��" } };//ͨ�÷�Χ
	public static String[][] processStatusList = new String[][] { { "1", "����" }, { "2", "����" } };//����״̬
	public static String[][] activityParticipantTypeList = new String[][] { { "1", "����" }, { "2", "����" } };//��������
	public static String[][] activityStatusList = new String[][] { { "1", "����" }, { "2", "����" } };//�״̬
	public static String[][] moduleTypeList = new String[][] { { "1", "����ģ��" }, { "2", "URL" } };//module-type
	public static String[][] eventType = new String[][] { { "JAVA", "JAVA����" }, { "SQL", "SQL���" }, { "PROCEDURE", "PROCEDURE����" },
			{ "WEBSERVICE", "WEBSERVICE����" } };//�¼�����
	public static String default_activityStatus = "1";//Ĭ�ϻ״̬
	public static String default_displayOrder = "0";//Ĭ����ʾ˳��
	public static String default_processScope = "1";//Ĭ��processScope
	public static String[][] ACTTYPE_CODES = new String[][] { { "1", "31.gif", "��ʼ�" }, { "2", "51.gif", "�˹��" },
			{ "5", "21.gif", "��֧·�ɻ" }, { "6", "81.gif", "�ۺ�·�ɻ" }, { "4", "41.gif", "�Զ��" }, { "9", "61.gif", "�����" } };//����������
	public static String[][] variableType = new String[][] { { "0", "�ַ�������" }, { "1", "���ͱ���" }, { "2", "�߼��ͱ���" }, { "3", "�����ͱ���" },
			{ "4", "�����ͱ���" }, { "5", "����ʱ�����" }, { "6", "�������" }, { "7", "ʱ���ͱ���" }, };
	public static String default_variableType = "0";
	public static String[][] variableDirection = new String[][] { { "1", "����" }, { "2", "���" } };
	public static String default_variableDirection = "1";
}
