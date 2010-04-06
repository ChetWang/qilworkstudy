package com.nci.svg.sdk.client;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-11
 * @功能：数据管理组件
 *
 */
public abstract class DataManageAdapter extends ModuleAdapter {
	/**
	 * 系统本地配置
	 */
	public static final int KIND_SYSSET = 0;
	/**
	 * 代码表
	 */
	public static final int KIND_CODES = 1;
	/**
	 * 图元和模板
	 */
	public static final int KIND_SYMBOL = 2;
//	/**
//	 * 模板
//	 */
//	public static final int KIND_TEMPLATE = 3;
	/**
	 * 图
	 */
	public static final int KIND_GRAPH = 4;
	/**
	 * 公共存储区
	 */
	public static final int KIND_GLOBAL = 5;
	
	/**
	 * add by yux,2009-1-18
	 * 业务规范
	 */
	public static final int KIND_INDUNORM = 501;
	public static final String KINDTYPE_INDUNORM = "KINDTYPE_INDUNORM";
	
	/**
	 * add by yux,2009-1-18
	 * 模型
	 */
	public static final int KIND_MODEL = 502 ;
	public static final String KINDTYPE_MODEL = "KINDTYPE_MODEL";
	
	
	/**
	 * add by yux,2009-1-18
	 * 模型关联业务规范
	 */
	public static final int KIND_MODELRELAINDUNORM = 503;
	public static final String KINDTYPE_MODELRELAINDUNORM = "KINDTYPE_MODELRELAINDUNORM";
	
	public static final String KINDTYPE_SYMBOLRELA_MODELANDINDUNORM = "KINDTYPE_SYMBOLRELA_MODELANDINDUNORM";
    public DataManageAdapter(EditorAdapter editor)
    {
    	super(editor);
    }
    
    /**
     * 根据传入的数据类型获取数据
     * @param kind:数据种类，代码，图元，模板...
     * @param type：数据类型，不能为空
     * @param obj：数据请求，根据数据种类确定，详见下文
     * @return：如存在则返回数据，不存在则返回null
     * 当kind＝KIND_SYSSET时，type不能为空，obj为空，如果存在该类型系统配置参数，则返回，否则返回null
     * 当kind=KIND_CODE时，type不能为空
     *            obj为空，查询该代码类型的所有代码数据，返回HashMap<String,CodeInfoBean>
	 *            obj为String类型时，obj为代码值，查询该代码类型下符合该代码值的代码数据,返回CodeInfoBean
	 *            obj为CodeInfoBean类型时，
	 *            当value值不为空时查询该代码类型下符合该代码值的代码数据，返回CodeInfoBean
	 *            当name值不为空时查询该代码类型下符合该代码名称的第一个代码数据，返回CodeInfoBean
	 *            当parentValue值不为空时查询该代码类型下符合该父代码值的所有代码数据，返回HashMap<String,CodeInfoBean>
	 *            不满足以上条件或查询不到数据时均返回OPER_ERROR
	 * 当kind＝KIND_GRAPHUNIT或KIND_TEMPLATE时，type不能为空
	 *            当obj为空时，请求该图元类型下所有图元,返回HashMap<String, NCIEquipSymbolBean>
	 * 当obj为NCIEquipSymbolBean时
	 *            当name不为空时，则返回该图元类型下符合该name的图元NCIEquipSymbolBean，不存在则返回错误
	 *            不满足以上条件或查询不到数据时均返回OPER_ERROR
	 * 
     */
    public abstract ResultBean getData(int kind,String type,Object obj);
    
    /**
     * 根据数据类型设置数据内容
     * 如遇见重复数据则覆盖
     * @param kind:数据种类，代码，图元，模板...
     * @param nType数据类型：不能为空
     * @param obj：数据请求，不能为空，根据数据种类确定，详见下文
     * @return：存储结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
     * 当kind＝KIND_SYSSET时，obj为String类型
     * 当kind=KIND_CODE时，obj为CodeInfoBean类型
	 * 当kind＝KIND_GRAPHUNIT或KIND_TEMPLATE时，obj为NCIEquipSymbolBean
	 *            不满足以上条件操作均无效，返回OPER_ERROR
     */
    public abstract int setData(int kind,String type,Object obj);
    
    /**
     * 根据数据类型添加数据内容
     * 如遇见重复数据不覆盖
     * @param kind:数据种类，代码，图元，模板...
     * @param nType数据类型
     * @param obj：数据请求，不能为空，根据数据种类确定，详见下文
     * @return：存储结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
     * 当kind＝KIND_SYSSET时，obj为String类型
     * 当kind=KIND_CODE时，obj为CodeInfoBean类型
	 * 当kind＝KIND_GRAPHUNIT或KIND_TEMPLATE时，obj为NCIEquipSymbolBean
	 *            不满足以上条件操作均无效，返回OPER_ERROR
     */
    public abstract int addData(int kind,String type,Object obj);
    
    /**
     * 根据数据类型删除数据内容
     * @param kind:数据种类，代码，图元，模板...
     * @param nType数据类型
     * @param obj：数据请求，根据数据种类确定，详见下文
     * @return：删除结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
     * 当kind＝KIND_SYSSET时，obj为空
     * 当kind=KIND_CODE时，
     *             obj为空时，则删除该数据类型
     *             obj不为空时,obj为CodeInfoBean，则删除符合该CodeInfoBean描述的代码数据
	 * 当kind＝KIND_GRAPHUNIT或KIND_TEMPLATE时，
     *              obj为空时，则删除该图元类型
     *             obj不为空时,obj为NCIEquipSymbolBean，则删除符合该CodeInfoBean描述的代码数据
     *             不满足以上条件操作均无效，返回OPER_ERROR
     */
    public abstract int removeData(int kind,String type,Object obj);
    
    /**
     * 从本地读取相应的数据种类和数据类型的数据
     * 如数据类型为null则全部读取
     * @param kind：数据种类，代码，图元，模板...
     * @param type：数据类型
     * @return：读取结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
     */
    public abstract int loadLocal(int kind,String type);
    
    /**
     * 从远程读取相应的数据种类和数据类型的数据
     * 如数据类型为null则全部读取
     * @param kind：数据种类，代码，图元，模板...
     * @param type：数据类型
     * @return：读取结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
     */
    public abstract int loadRemote(int kind,String type);
    
    /**
     * 将数据管理内指定种类的数据序列化到本地
     * @param kind:数据种类，代码，图元，模板。。
     * @return：序列化结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
     */
    public abstract int saveLocal(int kind);
}
