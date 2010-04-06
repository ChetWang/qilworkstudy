package com.nci.ums.v3.message.basic;

import java.io.Serializable;


/**
 * <p>Title: MsgContent.java</p>
 * <p>Description:
 *    Definition of a message content, including subject and text content,.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.19
 * @version 1.0
 */
public class MsgContent implements Serializable,Cloneable{
	
	public static final String SUBSCRIBE_MSG = "UMS_@#$@!_R$p_SUBSCRIBE";
	
	private String subject;
	private String content;
	
	public MsgContent(){}
	
	public MsgContent(String subject,String content){
		this.subject = subject;
		this.content = content;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.subject != null ? this.subject.hashCode() : 0);
        hash = 23 * hash + (this.content != null ? this.content.hashCode() : 0);
        return hash;
    }
        
        public boolean equals(Object o){
            if(o instanceof MsgContent){
                return ((MsgContent)o).getContent().equals(this.getContent()) && ((MsgContent)o).getSubject().equals(this.getSubject());
            }return false;
        }

}
