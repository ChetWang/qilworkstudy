
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：图文件参数描述类
 *
 */
public class GraphFileParamsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3367531887492696663L;
	/**
	 * add by yux,2008-12-31
	 * 字段属性
	 */
	private GraphFileParamBean beans[] = null;
	/**
	 * add by yux,2008-12-31
	 * 业务图类型
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
	 * 根据序号获取文件设置类型
	 * @param index序号
	 * @return
	 */
	public GraphFileParamBean getBean(int index)
	{
		if(index > 10) return null;
		return beans[index];
	}
	
	/**
	 * 根据序号设置文件参数信息
	 * @param index：序号
	 * @param desc：字段描述
	 * @param type：字段类型
	 * @param nullFlag：非空标记
	 */
	public void setBean(int index,String desc,String type,String nullFlag)
	{
		if(index > 10) return;
		//描述为空时，该输入无效
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
    	 * 字段描述
    	 */
    	private String desc;
    	/**
    	 * 字段类型
    	 */
    	private String type;
    	/**
    	 * 非空标记
    	 */
    	private String nullFlag;
    	
    	/**
    	 * 查询排序
    	 */
    	private int queryOrder;
		/**
		 * 返回
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}
		/**
		 * 设置
		 * @param desc the desc to set
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}
		/**
		 * 返回
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * 设置
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * 返回
		 * @return the nullFlag
		 */
		public String getNullFlag() {
			return nullFlag;
		}
		/**
		 * 设置
		 * @param nullFlag the nullFlag to set
		 */
		public void setNullFlag(String nullFlag) {
			this.nullFlag = nullFlag;
		}
		/**
		 * 返回
		 * @return the queryOrder
		 */
		public int getQueryOrder() {
			return queryOrder;
		}
		/**
		 * 设置
		 * @param queryOrder the queryOrder to set
		 */
		public void setQueryOrder(int queryOrder) {
			this.queryOrder = queryOrder;
		}
    }
	/**
	 * 返回业务图类型
	 * @return the graphType
	 */
	public String getGraphType() {
		return graphType;
	}

	/**
	 * 设置业务图类型
	 * @param graphType the graphType to set
	 */
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
}
