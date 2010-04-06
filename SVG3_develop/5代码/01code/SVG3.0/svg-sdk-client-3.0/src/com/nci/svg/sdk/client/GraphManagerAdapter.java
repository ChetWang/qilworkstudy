
package com.nci.svg.sdk.client;

import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-9
 * @���ܣ�ͼ�ι��������
 * �������ж����鴫��ķ�ʽ�����龡����Ҫƥ�䣬
 *
 */
public abstract class GraphManagerAdapter extends ModuleAdapter {
	public GraphManagerAdapter(EditorAdapter editor)
	{
		super(editor);
	}
	
	/**
	 * �Ե�ǰ���ڵĻ����������Ų���
	 * @param dScale:���ű���������Ϊ1�����ܴ���20��С��0.01
	 * @return:���Ž�������ųɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int scaleCanvas(double dScale);
	
	/**
	 * �Ե�ǰ���ڵĻ������б���ɫ�����޸�
	 * @param rgbColor:���޸ĵ���ɫ
	 * @return�޸Ľ�����ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetCanvasBg(int rgbColor);
	
	/**
	 * �Ե�ǰ���ڵĻ�������ƽ�Ʋ���
	 * @param xĿ��x��
	 * @param yĿ��y��
	 * @returnƽ�ƽ����ƽ�Ƴɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int moveCanvas(double x,double y);
	
	/**
	 * ͨ��ͼ�ϵ�ͼԪ��Ŷ�ͼԪ����״̬�仯
	 * @param strSymbolIDͼԪ���
	 * @param strSymbolStatusͼĿ��״̬����
	 * @return�仯������仯�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetSymbolStatus(String symbolID,String symbolStatus);
	
	/**
	 * ͨ��ҵ������Ŷ�ͼԪ����״̬�仯
	 * @param strBussID:���仯��ҵ���������
	 * @param strSymbolStatusĿ��״̬��������
	 * @return�仯������仯�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetSymbolsStatus_Buss(String[] bussID,String[] symbolStatus);
	
	/**
	 * ͨ��ͼ�ϵ�ͼԪ��Ŷ�ͼԪ����ɫ�ʱ仯
	 * @param strSymbolID���仯��ͼԪ�����
	 * @param strCssRemarkĿ��ɫ�ʵ���������
	 * @return�仯������仯�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetSymbolsColor(String[] symbolID,String[] cssRemark);
	
	/**
	 * ͨ��ҵ������Ŷ�ͼԪ����ɫ�ʱ仯
	 * @param strBussID���仯��ҵ���������
	 * @param strCssRemarkĿ��ɫ�ʵ���������
	 * @return�仯������仯�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetSymbolsColor_Buss(String[] bssID,String[] cssRemark);
	
	/**
	 * ͨ��ҵ������Ŷ�ͼԪ������״�仯
	 * @param strBussID���仯��ҵ���������
	 * @param strMatrixĿ��ת���ַ���
	 * @param xĿ��ƽ��x��
	 * @param yĿ��ƽ��y��
	 * @return�仯������仯�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetSymbolsMatrix(String[] bussID,String[] matrix,int[] x,int[] y);
	
	/**
	 * ͨ��ҵ������Ŷ��������ݡ�ɫ�ʽ��б仯
	 * @param strBussID���仯��ҵ���������
	 * @param strRGBĿ��ɫ��
	 * @param strContentĿ��������ʾ����
	 * @return�仯������仯�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int resetTexts(String[] bussID,String[] rgb,String[] content);
	
	/**
	 * ���ж�����ʾ
	 * @param xml������ʾ�嵥
	 * @return�嵥У���������Ա�������ʾ�ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
	 */
	public abstract int animate(String xml);
}
