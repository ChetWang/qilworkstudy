package org.vlg.linghu.vac;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vlg.linghu.mybatis.bean.VacReceiveMessage;
import org.vlg.linghu.mybatis.bean.VacReceiveMessageExample;
import org.vlg.linghu.mybatis.mapper.VacReceiveMessageMapper;

import com.unicom.vac.bossagent.soap.sync.req.OrderRelationUpdateNotifyRequest;

public class VACNotifyHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(VACNotifyHandler.class);

	@Autowired
	DataSource dataSource;

	@Autowired
	VacReceiveMessageMapper vacReceiveMessageMapper;

	public synchronized void handle(OrderRelationUpdateNotifyRequest req) {
		VacReceiveMessage vacReceive = getVacReceiveMessage(req);
		String userId = req.getUserId();
		int updateType = req.getUpdateType();

		String content = req.getContent();
		// 更新操作的类型包括：
		// 1：订购
		// 2：取消订购退定
		// 3：点播
		// 4：定购关系变更(一般是修改有效期)（保留，暂不用）
		// 5：改号
		switch (updateType) {
		case 1:
			logger.info("用户订购：{}", userId + "  " + content);
			addUser(vacReceive);
			break;
		case 2:
			logger.info("用户退订：{}", userId + "  " + content);
			// updateUser(req);
			deleteUser(vacReceive);
			break;
		case 3:
			logger.info("用户点播：{}", userId + "  " + content);
			addUser(vacReceive);
			break;
		case 4:
			logger.info("update type 4, reserved. ");
			break;
		case 5:
			logger.info("用户改号：{}", userId + "  " + content);
			transferUser(vacReceive);
			break;
		default:
			break;
		}
		logger.info(getBeanInfo(req));
	}

	public static String getBeanInfo(Object o) {
		StringBuilder sb = new StringBuilder();
		Method[] methods = o.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().startsWith("get")&&!m.getName().equals("getClass")) {
				try {
					Object value = m.invoke(o, new Object[] {});
					sb.append(m.getName() + " --> " + value);
					sb.append(" | ");
				} catch (Exception e) {
				}
			}
		}
		return sb.toString();
	}

	private VacReceiveMessage getVacReceiveMessage(
			OrderRelationUpdateNotifyRequest req) {
		VacReceiveMessage m = new VacReceiveMessage();
		m.setContent(req.getContent());
		m.setEffectivedate(req.getEffectiveDate());
		m.setEncodestr(req.getEncodeStr());
		m.setExpiredate(req.getExpireDate());
		// m.setIsputwelcomesms(req.geti)
		m.setLinkid(req.getLinkId());
		m.setProductid(req.getProductId());
		m.setRecordsequenceid(req.getRecordSequenceId());
		m.setResultcode(0);
		m.setServicetype(req.getServiceType());
		m.setSpid(req.getSpId());
		m.setTimeStamp(req.getTime_stamp());
		m.setUpdatedesc(req.getUpdateDesc());
		m.setUpdatetime(req.getUpdateTime());
		m.setUpdatetype(req.getUpdateType());

		m.setUserid(req.getUserId());
		m.setUseridtype(req.getUserIdType());
		// m.setUserName(req.getuser)
		// m.setUserId(Integer.parseInt(req.getUserId()));
		m.setVacAddtime(new Date());
		return m;
	}

	private void transferUser(VacReceiveMessage vacReceive) {

	}

	private void addUser(VacReceiveMessage vacReceive) {
		try {
			vacReceiveMessageMapper.insertSelective(vacReceive);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void updateUser(VacReceiveMessage vacReceive) {
		try {
			hanleExistingUsers(vacReceive);
			vacReceiveMessageMapper.updateByExampleSelective(vacReceive,
					createCriteriaExample(vacReceive));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void deleteUser(VacReceiveMessage vacReceive) {
		try {
			hanleExistingUsers(vacReceive);
			vacReceiveMessageMapper
					.deleteByExample(createCriteriaExample(vacReceive));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void hanleExistingUsers(VacReceiveMessage vacReceive) {
		logger.info("Handle existing users");
		try {
			VacReceiveMessageExample ex = createCriteriaExample(vacReceive);
			List<VacReceiveMessage> existingUsers = vacReceiveMessageMapper
					.selectByExample(ex);

			for (VacReceiveMessage m : existingUsers) {
				String s = getBeanInfo(m);
				logger.info("备份先前用户信息: {}",s);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("Handle existing users complete");
	}

	private VacReceiveMessageExample createCriteriaExample(
			VacReceiveMessage vacReceive) {
		VacReceiveMessageExample ex = new VacReceiveMessageExample();
		ex.createCriteria().andProductidEqualTo(vacReceive.getProductid())
				.andSpidEqualTo(vacReceive.getSpid())
				.andUseridEqualTo(vacReceive.getUserid());
		return ex;
	}

	public static void main(String[] xx) {
		logger.info("xxxx");
	}

}
