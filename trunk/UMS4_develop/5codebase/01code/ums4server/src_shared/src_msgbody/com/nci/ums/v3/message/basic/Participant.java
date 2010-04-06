package com.nci.ums.v3.message.basic;

import java.io.Serializable;


/**
 * <p>Title: Participant.java</p>
 * <p>Description:
 *    A participant is the message sender or receiver, it can be both service and user.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.19
 * @version 1.0
 */
public class Participant implements Serializable {

    private String participantID;
    private int idType;
    private int participantType;
    /**
     * Mobile phone number type of a participant id, value is 1
     */
    public static final int PARTICIPANT_ID_MOBILE = 1;
    /**
     * Email type of a participant id, value is 2
     */
    public static final int PARTICIPANT_ID_EMAIL = 2;
    /**
     * LCS type of a participant id, value is 3
     */
    public static final int PARTICIPANT_ID_LCS = 3;
    /**
     * Type of user login to a web application, value is 4
     */
    public static final int PARTICIPANT_ID_WEBUSER = 4;
    /**
     * Application service type of a participant id, value is 5
     */
    public static final int PARTICIPANT_ID_APPSERVICE = 5;
    
    public static final int PARTICIPANT_ID_UUM_USERID = 6;
    
    /**
     * value is 51, implicit that the participant is the message sender
     */
    public static final int PARTICIPANT_MSG_FROM = 1; //发消息的人
    
    /**
     * value is 52, implicit that the participant is the direct message receiver
     */
    public static final int PARTICIPANT_MSG_TO = 2; //收消息的人
    
    /**
     * value is 53, implicit that the participant is the CC receiver
     */
    public static final int PARTICIPANT_MSG_CC = 3; //抄送的人

    public Participant() {
    }

    public Participant(String participantID, int participantType, int idType) {
        this.participantID = participantID;
        this.participantType = participantType;
        this.idType = idType;
    }
    
    public Participant(String participantID, int idType) {
        this.participantID = participantID;
        this.idType = idType;
        this.participantType = -1;
    }

    /**
     * retrieve participant id, it could be mobile:13819155408,
     * or email:xxx@nci.com.cn, or an application service id:1234,
     * or lcs account, personal web login id, etc.
     * @return the paticipantID
     */
    public String getParticipantID() {
        return participantID;
    }

    /**
     * set value of participant id, it could be mobile:13819155408,
     * or email:xxx@nci.com.cn, or an application service id:1234,
     * or lcs account, personal web login id, etc.
     * @param participantID the paticipantID to set
     */
    public void setParticipantID(String participantID) {
        this.participantID = participantID;
    }

    /**
     * retrieve  participant id type.
     * For example, if participant id is 13819155408,then the idType is 1, it represents mobile phone number;
     * @return the idType
     */
    public int getIDType() {
        return idType;
    }

    /**
     * set value of participant id type.
     * For example, if participant id is 13819155408,then the idType is 1, it represents mobile phone number;
     * @param idType the idType to set
     */
    public void setIDType(int idType) {
        this.idType = idType;
    }

    /**
     * get the type of a participant, it is usually used as email sending,
     * for example, a message could inclued a receiver
     * @return the participantType
     */
    public int getParticipantType() {
        return participantType;
    }

    /**
     * @param participantType the participantType to set
     */
    public void setParticipantType(int participantType) {
        this.participantType = participantType;
    }

    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.participantID != null ? this.participantID.hashCode() : 0);
        hash = 79 * hash + this.idType;
        hash = 79 * hash + this.participantType;
        return hash;
    }
    
    public boolean equals(Object o){
        if(o instanceof Participant){
            return ((Participant)o).getIDType() == this.getIDType() && ((Participant)o).getParticipantID().equals(this.getParticipantID())
                    && ((Participant)o).getParticipantType() == this.getParticipantType();
        }
        return false;
    }
}