
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-26
 * @���ܣ�ͼ�ļ�����������
 *
 */
public class GraphFileParamsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3367531887492696663L;
	/**
	 * add by yux,2008-12-31
	 * �ֶ�����
	 */
	private GraphFileParamBean beans[] = null;
	/**
	 * add by yux,2008-12-31
	 * ҵ��ͼ����
	 */
	private String graphType = null;
	public GraphFileParamsBean()
	{
		beans= new GraphFileParamBean[10];
		for(int i = 0;i < 10;i++)
		{
			beans[i] = new GraphFileParamBean();
		}
	}
	
	/**
	 * ������Ż�ȡ�ļ���������
	 * @param index���
	 * @return
	 */
	public GraphFileParamBean getBean(int index)
	{
		if(index > 10) return null;
		return beans[index];
	}
	
	/**
	 * ������������ļ�������Ϣ
	 * @param index�����
	 * @param desc���ֶ�����
	 * @param type���ֶ�����
	 * @param nullFlag���ǿձ��
	 */
	public void setBean(int index,String desc,String type,String nullFlag)
	{
		if(index > 10) return;
		//����Ϊ��ʱ����������Ч
		if(desc == null) return ;
		beans[index].setDesc(desc);
		beans[index].setType(type);
		beans[index].setNullFlag(nullFlag);
	}
	
    public class GraphFileParamBean implements Serializable
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -6863401208426231660L;
		/**
    	 * �ֶ�����
    	 */
    	private String desc;
    	/**
    	 * �ֶ�����
    	 */
    	private String type;
    	/**
    	 * �ǿձ��
    	 */
    	private String nullFlag;
    	
    	/**
    	 * ��ѯ����
    	 */
    	private int queryOrder;
		/**
		 * ����
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}
		/**
		 * ����
		 * @param desc the desc to set
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}
		/**
		 * ����
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * ����
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * ����
		 * @return the nullFlag
		 */
		public String getNullFlag() {
			return nullFlag;
		}
		/**
		 * ����
		 * @param nullFlag the nullFlag to set
		 */
		public void setNullFlag(String nullFlag) {
			this.nullFlag = nullFlag;
		}
		/**
		 * ����
		 * @return the queryOrder
		 */
		public int getQueryOrder() {
			return queryOrder;
		}
		/**
		 * ����
		 * @param queryOrder the queryOrder to set
		 */
		public void setQueryOrder(int queryOrder) {
			this.queryOrder = queryOrder;
		}
    }
	/**
	 * ����ҵ��ͼ����
	 * @return the graphType
	 */
	public String getGraphType() {
		return graphType;
	}

	/**
	 * ����ҵ��ͼ����
	 * @param graphType the graphType to set
	 */
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
}
