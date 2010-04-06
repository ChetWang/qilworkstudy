
package com.nci.svg.sdk.server.graphstorage;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-27
 * @���ܣ�ͼ�β��������࣬���еľ���ʵ���ڴ˻�����ת��
 *
 */
public class GraphInfoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5539857267827812878L;
	/**
     * ͼ�α��
     */
    private String id;
    /**
     * ͼ������
     */
    private String fileName;
    /**
     * ͼ���ͣ���׼ͼ������ͼ
     */
    private String type;
    /**
     * ͼҵ������
     */
    private String busiType;
    /**
     * ͼ�ļ���ʽ
     */
    private String fileType;
    /**
     * ����0
     */
    private String param0;
    /**
     * ����1
     */
    private String param1;
    /**
     * ����2
     */
    private String param2;
    /**
     * ����3
     */
    private String param3;
    /**
     * ����4
     */
    private String param4;
    /**
     * ����5
     */
    private String param5;
    /**
     * ����6
     */
    private String param6;
    /**
     * ����7
     */
    private String param7;
    /**
     * ����8
     */
    private String param8;
    /**
     * ����9
     */
    private String param9;
    /**
     * ��������
     */
    private String person;
    /**
     * ������ʱ��
     */
    private String time;
    /**
     * ͼ����
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
