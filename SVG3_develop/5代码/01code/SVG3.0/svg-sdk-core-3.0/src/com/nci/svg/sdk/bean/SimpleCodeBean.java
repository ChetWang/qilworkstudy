package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * �򵥵�"����--����"ƥ�����
 * 
 * @author Qil.Wong
 * 
 */
public class SimpleCodeBean implements Serializable {

	private static final long serialVersionUID = -9083359374146066993L;
	/**
	 * ����
	 */
	private String code = "";
	/**
	 * ����
	 */

	private String name;

	public SimpleCodeBean() {
	}

	public SimpleCodeBean(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object o) {
		boolean flag = false;
		if (o instanceof SimpleCodeBean) {

			try {
				SimpleCodeBean other = (SimpleCodeBean) o;
				flag = this.code == other.getCode() ? true : this.code
						.equals(other.getCode());
				if (flag) {
					flag = this.name == other.getName() ? true : this.name
							.equals(other.getName());
				}
			} catch (Exception e) {
				flag = false;
			}
		}
		return flag;
	}

	public int hashCode() {
		int hash = -1;
		if (code == null && name != null) {
			hash = name.hashCode();
		} else if (code != null && name == null) {
			hash = code.hashCode();
		} else if (code == null && name == null) {
			hash = 0;
		} else {
			hash = code.hashCode() * name.hashCode();
		}
		return hash;
	}

	/**
	 * ��¡һ���͵�ǰ����һ�µĶ���
	 * @return
	 */
	public SimpleCodeBean cloneCodeBean() {
		return new SimpleCodeBean(this.code, this.name);
	}
}
