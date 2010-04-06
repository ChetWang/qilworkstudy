package com.nci.ums.v3.message.euis;

import java.io.Serializable;
import com.nci.ums.v3.message.basic.Participant;

/**
 * <p>Title: EnterpriseUserInfo.java</p>
 * <p>Description:
 *    The definition of a basic Enterprise User Information System interface.
 *    It could get all the necessary user information from AD,LDAP,UUM,etc.
 *    It provides the capability of inter alternation for a logical user account.
 *    For example ,developer can get a man's email account by his mobile number.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.20, since UMS3.0
 * @version 1.0
 */
public interface EnterpriseUserInfo extends Serializable {

//    /**
//     * Query a user's email account
//     * @param user_previous any kind of type(mobile number,lcs account, web login name)
//     * @return user's email account
//     */
//    public String getEmailAccout(Participant user_previous);
//
//    /**
//     * Query a user's mobile number
//     * @param user_previous any kind of type(email,lcs account, web login name)
//     * @return user's mobile number
//     */
//    public String getMobileAccout(Participant user_previous);
//
//    /**
//     * Query a user's LCS account
//     * @param user_previous any kind of type(mobile number,email account, web login name)
//     * @return user's LCS account
//     */
//    public String getLcsAccount(Participant user_previous);
//
//    /**
//     * Query a user's web login account
//     * @param user_previous any kind of type(mobile number,lcs account, email)
//     * @return user's web login account
//     */
//    public String getWebLoginAccount(Participant user_previous);

    /**
     * Query a users id by a defined ID type.
     * @param user_previous the Participant object of a user(receiver or sender) 
     * at previous step of message sending.
     * @param neededParticipantIDType participant id type
     * @return the needed user id.
     */
    public String getAccout(Participant user_previous,
            int neededParticipantIDType);
    
    public String getStasticSQL(Participant receiver);
}