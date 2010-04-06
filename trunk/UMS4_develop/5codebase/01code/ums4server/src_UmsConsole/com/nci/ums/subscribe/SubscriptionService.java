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
 * 订阅服务营应用类，订阅消息是指在BasicMsg对象中，umsFlag属性为3的消息
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
	 * 为订阅的消息指定正确的接收者
	 * 
	 * @param subscriptionMsg
	 */
	public void implSubscription(BasicMsg subscriptionMsg) {
		Participant[] receivers = getSubscribeReceivers(subscriptionMsg
				.getServiceID());
		subscriptionMsg.setReceivers(receivers);
	}

	/**
	 * 对指定serviceID的订阅消息进行二次封装，使之满足发送条件，主要是对receiver[]进行重新赋值（不管先前有没有receiver）。
	 * 默认，对每个receiver（其实为Participant对象）进行赋值，首选手机号（并指定正确idType）；
	 * 如果没有手机号，则选email号，如果手机和email都没有，则留null。
	 * 
	 * @param serviceID
	 */
	private Participant[] getSubscribeReceivers(String serviceID) {
		// 订阅者SQL语句
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
			Res.log(Res.ERROR, "获取服务的消息订阅者信息失败！");
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				rs.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Res.log(Res.ERROR, "获取服务的消息订阅者类，关闭数据库连接失败！");
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
