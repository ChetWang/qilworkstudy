package com.nci.svg.sdk.client.update;

/**
 * 升级组件存储各类升级信息的对象bean，根据不同的UpdateBean进行判断，SVG平台客户端可以判断哪些文件或对象需要升级。
 * 
 * @author Qil.Wong
 * 
 */
public class UpdateBean {

	/**
	 * 升级对象的简称
	 */
	private String shortName;

	/**
	 * 升级对象的全称
	 */
	private String name;

	/**
	 * 升级对象的类型
	 */
	private String type;

	/**
	 * 升级对象的版本
	 */
	private String edition;

	/**
	 * 无参构造函数
	 */
	public UpdateBean() {

	}

	/**
	 * 有参构造函数
	 * @param shortName 升级对象的简称
	 * @param name 升级对象的全称
	 * @param type 升级对象的类型
	 * @param edition 升级对象的版本
	 */
	public UpdateBean(String shortName, String name, String type, String edition) {
		this.shortName = shortName;
		this.name = name;
		this.type = type;
		this.edition = edition;
	}

	/**
	 * 获取对象的简称
	 * 
	 * @return 对象的简称
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * 设置升级对象的简称
	 * 
	 * @param shortName
	 *            升级对象的简称
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * 获取升级对象的全称
	 * 
	 * @return 升级对象的全称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置升级对象的全称
	 * 
	 * @param name
	 *            升级对象的全称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取升级对象的类型
	 * 
	 * @return 升级对象的类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置升级对象的类型
	 * 
	 * @param type
	 *            升级对象的类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取升级对象版本
	 * 
	 * @return 升级对象版本
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * 设置升级对象版本
	 * 
	 * @param edition
	 *            升级对象版本
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

}
