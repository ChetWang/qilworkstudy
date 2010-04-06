package com.nci.svg.server.mappingdata;

import java.util.List;
import java.util.Map;

public interface MappingData {
	/*
	 * @function :��ȡҵ��ģ�Ͷ�����Ϣ��ѯ
	 * @parm filter:�������� ��Ϊ��ϵͳ���ֶ� ��SD_OBJID
	 * @parm filter:�������� ��Ϊ��ϵͳ���ֶ� ��SD_OBJID
	 * @retrun ��ҵ��ϵͳ��sql
	 */
	public List getMappingData(String modelId,Map filter,List relation, String[] order);
	
	public String getMappingDataSQL(String modelId,Map filter,String [] order);
	/*
	 * @function :��ȡ����ֵͨ������
	 * @parm modelName:��ϵͳ������ģ��
	 * @parm field:�������� ��Ϊ��ϵͳ���ֶ�
	 * @param code ����
	 * @param String ����ֵ
	 */
	public String getValueByCode(String modelId,String field,String code);
}
