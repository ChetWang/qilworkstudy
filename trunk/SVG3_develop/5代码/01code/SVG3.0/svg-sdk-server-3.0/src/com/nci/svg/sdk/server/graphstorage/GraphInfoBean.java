
package com.nci.svg.sdk.server.graphstorage;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-27
 * @功能：图形参数基础类，所有的具体实现在此基础上转换
 *
 */
public class GraphInfoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5539857267827812878L;
	/**
     * 图形编号
     */
    private String id;
    /**
     * 图形名称
     */
    private String fileName;
    /**
     * 图类型，标准图、个性图
     */
    private String type;
    /**
     * 图业务类型
     */
    private String busiType;
    /**
     * 图文件格式
     */
    private String fileType;
    /**
     * 参数0
     */
    private String param0;
    /**
     * 参数1
     */
    private String param1;
    /**
     * 参数2
     */
    private String param2;
    /**
     * 参数3
     */
    private String param3;
    /**
     * 参数4
     */
    private String param4;
    /**
     * 参数5
     */
    private String param5;
    /**
     * 参数6
     */
    private String param6;
    /**
     * 参数7
     */
    private String param7;
    /**
     * 参数8
     */
    private String param8;
    /**
     * 参数9
     */
    private String param9;
    /**
     * 最后更新人
     */
    private String person;
    /**
     * 最后更新时间
     */
    private String time;
    /**
     * 图内容
     */
    private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return fileName;
	}
	public void setName(String name) {
		this.fileName = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getParam0() {
		return param0;
	}
	public void setParam0(String param0) {
		this.param0 = param0;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}
	public String getParam6() {
		return param6;
	}
	public void setParam6(String param6) {
		this.param6 = param6;
	}
	public String getParam7() {
		return param7;
	}
	public void setParam7(String param7) {
		this.param7 = param7;
	}
	public String getParam8() {
		return param8;
	}
	public void setParam8(String param8) {
		this.param8 = param8;
	}
	public String getParam9() {
		return param9;
	}
	public void setParam9(String param9) {
		this.param9 = param9;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
