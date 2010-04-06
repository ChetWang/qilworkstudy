/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-18
 * @功能：业务系统各种数据类型
 *
 */
package com.nci.svg.client.datamanage;

import java.io.Serializable;

/**
 * @author yx.nci
 *
 */
public class BusinessDataType implements Serializable {
    /**
     * add by yux,2009-1-18
     * 图业务规范数据
     */
    public static final String DATATYPE_INDUNORM = "indunorm";
    /**
     * add by yux,2009-1-18
     * 模型数据
     */
    public static final String DATATYPE_MODEL = "model";
    /**
     * add by yux,2009-1-18
     * 图元与规范关联数据
     */
    public static final String DATATYPE_SYMBOLRELAINDUNORM = "symbolRelaIndunorm";
    /**
     * add by yux,2009-1-18
     * 模型与规范关联数据
     */
    public static final String DATATYPE_MODELRELAINDUNORM = "modelRelaIndunorm";
}
