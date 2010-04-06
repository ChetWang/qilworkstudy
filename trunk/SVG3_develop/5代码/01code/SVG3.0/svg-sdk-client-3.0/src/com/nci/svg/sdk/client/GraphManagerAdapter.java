
package com.nci.svg.sdk.client;

import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-9
 * @功能：图形管理控制类
 * 对于所有多数组传入的方式，数组尽量需要匹配，
 *
 */
public abstract class GraphManagerAdapter extends ModuleAdapter {
	public GraphManagerAdapter(EditorAdapter editor)
	{
		super(editor);
	}
	
	/**
	 * 对当前窗口的画布进行缩放操作
	 * @param dScale:缩放比例，正常为1，不能大于20，小于0.01
	 * @return:缩放结果，缩放成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int scaleCanvas(double dScale);
	
	/**
	 * 对当前窗口的画布进行背景色进行修改
	 * @param rgbColor:待修改的颜色
	 * @return修改结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetCanvasBg(int rgbColor);
	
	/**
	 * 对当前窗口的画布进行平移操作
	 * @param x目标x轴
	 * @param y目标y轴
	 * @return平移结果，平移成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int moveCanvas(double x,double y);
	
	/**
	 * 通过图上的图元编号对图元进行状态变化
	 * @param strSymbolID图元编号
	 * @param strSymbolStatus图目标状态名称
	 * @return变化结果，变化成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetSymbolStatus(String symbolID,String symbolStatus);
	
	/**
	 * 通过业务对象编号对图元进行状态变化
	 * @param strBussID:待变化的业务对象编号组
	 * @param strSymbolStatus目标状态的名称组
	 * @return变化结果，变化成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetSymbolsStatus_Buss(String[] bussID,String[] symbolStatus);
	
	/**
	 * 通过图上的图元编号对图元进行色彩变化
	 * @param strSymbolID待变化的图元编号组
	 * @param strCssRemark目标色彩的配置名称
	 * @return变化结果，变化成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetSymbolsColor(String[] symbolID,String[] cssRemark);
	
	/**
	 * 通过业务对象编号对图元进行色彩变化
	 * @param strBussID待变化的业务对象编号组
	 * @param strCssRemark目标色彩的配置名称
	 * @return变化结果，变化成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetSymbolsColor_Buss(String[] bssID,String[] cssRemark);
	
	/**
	 * 通过业务对象编号对图元进行形状变化
	 * @param strBussID待变化的业务对象编号组
	 * @param strMatrix目标转换字符串
	 * @param x目标平移x轴
	 * @param y目标平移y轴
	 * @return变化结果，变化成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetSymbolsMatrix(String[] bussID,String[] matrix,int[] x,int[] y);
	
	/**
	 * 通过业务对象编号对文字内容、色彩进行变化
	 * @param strBussID待变化的业务对象编号组
	 * @param strRGB目标色彩
	 * @param strContent目标文字显示内容
	 * @return变化结果，变化成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int resetTexts(String[] bussID,String[] rgb,String[] content);
	
	/**
	 * 进行动画显示
	 * @param xml动画显示清单
	 * @return清单校验结果，可以被动画显示成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	public abstract int animate(String xml);
}
