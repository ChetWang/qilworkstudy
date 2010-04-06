package com.nci.ums.subscribe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.nci.ums.util.Res;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.Participant;

/**
 * ���ķ���ӪӦ���࣬������Ϣ��ָ��BasicMsg�����У�umsFlag����Ϊ3����Ϣ
 * 
 * @author Qil.Wong
 * 
 */
public class SubscriptionService {

	private static SubscriptionService service;

	public static SubscriptionService getInstance() {
		if (service == null) {
			service = new SubscriptionService();
		}
		return service;
	}

	/**
	 * Ϊ���ĵ���Ϣָ����ȷ�Ľ�����
	 * 
	 * @param subscriptionMsg
	 */
	public void implSubscription(BasicMsg subscriptionMsg) {
		Participant[] receivers = getSubscribeReceivers(subscriptionMsg
				.getServiceID());
		subscriptionMsg.setReceivers(receivers);
	}

	/**
	 * ��ָ��serviceID�Ķ�����Ϣ���ж��η�װ��ʹ֮���㷢����������Ҫ�Ƕ�receiver[]�������¸�ֵ��������ǰ��û��receiver����
	 * Ĭ�ϣ���ÿ��receiver����ʵΪParticipant���󣩽��и�ֵ����ѡ�ֻ��ţ���ָ����ȷidType����
	 * ���û���ֻ��ţ���ѡemail�ţ�����ֻ���email��û�У�����null��
	 * 
	 * @param serviceID
	 */
	private Participant[] getSubscribeReceivers(String serviceID) {
		// ������SQL���
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.user_mobile, c.user_email ").append(
				"FROM ums_message_subscribe a, ums_service b").append(
				", uum_user c WHERE a.person_key = c.user_id ").append(
				"AND a.service_key = b.seqkey ").append("AND b.service_id = '")
				.append(serviceID).append("'");

		ArrayList participantList = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = Res.getConnection();
		
		try {
			stmt = conn.createStatement();
			Res.log(Res.DEBUG, sql.toString());
			rs = stmt.executeQuery(sql.toString());
			while(rs.next()){
				String userMobile = rs.getString("user_mobile");
				String userEmail = rs.getString("user_email");
				Participant participant = new Participant();
				if(userMobile!=null && userMobile.length()>0){
					participant.setIDType(Participant.PARTICIPANT_ID_MOBILE);
					participant.setParticipantID(userMobile);
				}else if(userEmail!=null && userEmail.length()>0){
					participant.setIDType(Participant.PARTICIPANT_ID_EMAIL);
					participant.setParticipantID(userEmail);
				}else{
//					participant.setIDType(Participant.PARTICIPANT_ID_MOBILE);
					participant.setParticipantID(null);
				}
				
				participantList.add(participant);
			}
		} catch (SQLException e) {
			Res.log(Res.ERROR, "��ȡ�������Ϣ��������Ϣʧ�ܣ�");
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				rs.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Res.log(Res.ERROR, "��ȡ�������Ϣ�������࣬�ر����ݿ�����ʧ�ܣ�");
				Res.logExceptionTrace(e);
			}
		}
		
		participantList.trimToSize();
		int size = participantList.size();
		Participant[] reParticipant = new Participant[size];
		for(int i=0;i<size; i++){
			reParticipant[i] = (Participant) participantList.get(i);
		}
		
		return reParticipant;
	}

}
