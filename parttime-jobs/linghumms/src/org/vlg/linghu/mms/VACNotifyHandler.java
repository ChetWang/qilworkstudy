package org.vlg.linghu.mms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virbraligo.db.ConnectionManager;

import com.unicom.vac.bossagent.soap.sync.req.OrderRelationUpdateNotifyRequest;

public class VACNotifyHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(VACNotifyHandler.class);

	private static final String insertsql = "insert into e_vac_receive (vac_addtime,) values ()";
	
	
	
	/****** Script for SelectTopNRows command from SSMS  ******/
	SELECT TOP 1000 [vac_id]
	      ,[vac_addtime]
	      ,[RecordSequenceID]
	      ,[UserIdType]
	      ,[UserId]
	      ,[ServiceType]
	      ,[SpId]
	      ,[ProductId]
	      ,[UpdateType]
	      ,[UpdateTime]
	      ,[UpdateDesc]
	      ,[LinkID]
	      ,[Content]
	      ,[EffectiveDate]
	      ,[ExpireDate]
	      ,[ResultCode]
	      ,[Time_Stamp]
	      ,[EncodeStr]
	      ,[user_id]
	      ,[user_name]
	  FROM [LoveLife_DB].[dbo].[e_vac_receive]
			  
			  

	private static final String updatesql = "update e_vac_receive set vac_addtime=?, " +
			"RecordSequenceID=?, UserIdType=?, ServiceType=?, UpdateType=?,"
			+" UpdateTime=?, UpdateDesc=?, LinkID=?,Content=?,EffectiveDate=?,ExpireDate=?, "+
			"ResultCode=?,Time_Stamp=?,EncodeStr=?,user_id=?,user_name=? where userid=? and SpId=? and ProductId=?";

	private static final String transfersql="update e_vac_receive set userid=? where userid=? and SpId=? and ProductId=?";
	
	private static final String selectsql = "select count(*) from e_vac_receive where userid=? and ";

	public void handle(OrderRelationUpdateNotifyRequest req) {
		String userId = req.getUserId();
		int updateType = req.getUpdateType();
		//更新操作的类型包括：
		//1：订购
		//2：取消订购退定
		//3：点播
		//4：定购关系变更(一般是修改有效期)（保留，暂不用）
		//5：改号

		if(isUserTypeExist(req)){
			updateUser(req);
		}else{
			addUser(req);
		}
	}

	private void addUser(OrderRelationUpdateNotifyRequest req) {
		Connection conn = ConnectionManager.getConnection();
		try{
			PreparedStatement ps = conn.prepareStatement(updatesql);
			
		}
	}

	private void updateUser(OrderRelationUpdateNotifyRequest req) {
		
	}

	public boolean isUserTypeExist(OrderRelationUpdateNotifyRequest req)  {
		//ProductId, spid, userid为唯一业务订购主键
		String userId = req.getUserId();
		Connection conn = ConnectionManager.getConnection();
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(selectsql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch(SQLException e){
			logger.error("",e);
			return true;
		}finally {
			ConnectionManager.releaseConnection(conn);
		}
		return count > 0;
	}

}
