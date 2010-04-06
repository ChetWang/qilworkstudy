package com.nci.ums.v3.message.euis;

import com.nci.ums.v3.message.basic.Participant;


/**
 * <p>Title: DefaultEnterpriseUserInfo.java</p>
 * <p>Description:
 *    The default implementation of <b>EnterperiseUserInfo</b>
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.22, since UMS3.0
 * @version 1.0
 */

public class DefaultEnterpriseUserInfo implements EnterpriseUserInfo{

	private static final long serialVersionUID = 4469011334808163777L;

	public String getAccout(Participant user, int neededParticipantIDType) {
		// TODO Auto-generated method stub
		return user.getParticipantID();
	}

	public String getEmailAccout(Participant user) {
		// TODO Auto-generated method stub
		return this.getAccout(user,Participant.PARTICIPANT_ID_EMAIL);
	}

	public String getLcsAccount(Participant user) {
		// TODO Auto-generated method stub
		return this.getAccout(user,Participant.PARTICIPANT_ID_LCS);
	}

	public String getMobileAccout(Participant user) {
		// TODO Auto-generated method stub
		return this.getAccout(user,Participant.PARTICIPANT_ID_MOBILE);
	}

	public String getWebLoginAccount(Participant user) {
		// TODO Auto-generated method stub
		return this.getAccout(user,Participant.PARTICIPANT_ID_WEBUSER);
	}



}
