package com.nci.svg.sdk.module;

import java.io.Serializable;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;

/**
 * �������ģ��Ĺ����ӿڣ��ṩ��ʼ�������³�ʼ����������ֹͣ�����������Ӧ��ҵ���߼�����ӿڵȹ��ܡ�
 * @author Qil.Wong
 * @since 3.0
 *
 */
public interface GeneralModuleIF extends Serializable{

	/**
	 * ģ���ʼ���ɹ����
	 */
	public final static int MODULE_INITIALIZE_COMPLETE = 0;

	/**
	 * ģ���ʼ��ʧ�ܱ��
	 */
	public final static int MODULE_INITIALIZE_FAILED = -1;

	/**
	 * ģ�鿪���ɹ����
	 */
	public final static int MODULE_START_COMPLETE = 0;

	/**
	 * ģ�鿪��ʧ�ܱ��
	 */
	public final static int MODULE_START_FAILED = -1;

	/**
	 * ģ��رճɹ����
	 */
	public final static int MODULE_STOP_COMPLETE = 0;

	/**
	 * ģ��ر�ʧ�ܱ��
	 */
	public final static int MODULE_STOP_FAILED = -1;
	
	/**
	 * ����ʧ��
	 */
	public final static int OPER_ERROR = -1;
	/**
	 * �����ɹ�
	 */
	public final static int OPER_SUCCESS = 0;

	/**
	 * ��ʼ�����

	 * @return ��ʼ�������
	 * @see GeneralModuleIF#MODULE_INITIALIZE_COMPLETE
	 * @see GeneralModuleIF#MODULE_INITIALIZE_FAILED
	 */
	public int init();

	/**
	 * ���³�ʼ�����
	 * @param obj ���³�ʼ������������
	 * @return ���³�ʼ���Ľ����
	 * @see GeneralModuleIF#MODULE_INITIALIZE_COMPLETE
	 * @see GeneralModuleIF#MODULE_INITIALIZE_FAILED
	 */
	public int reInit();

	/**
	 * �������
	 * @param obj ��������������
	 * @return ��������Ľ��
	 * @see GeneralModuleIF#MODULE_START_COMPLETE
	 * @see GeneralModuleIF#MODULE_START_FAILED
	 */
	public int start();

	/**
	 * �ر����
	 * @param obj �ر�����������
	 * @return �ر�����Ľ��
	 * @see GeneralModuleIF#MODULE_STOP_COMPLETE
	 * @see GeneralModuleIF#MODULE_STOP_FAILED
	 */
	public int stop();

	/**
	 * ��������߼�ҵ����
	 * @param action �߼�ҵ������
	 * @param params �����������map
	 * @return �����Ľ��
	 */
	public ResultBean handleOper(String action, Map params);

	/**
	 * ��ȡ���ģ�������
	 * @return ���ģ�������
	 */
	public String getModuleType();

	/**
	 * ��ȡ���ģ����
	 * @return ���ģ����
	 */
	public String getModuleID();
	
	/**
	 * �ж�����Ƿ�������
	 * @return trueΪ�Ѿ�ֹͣ��false��������
	 */
	public boolean isStoped();

}
