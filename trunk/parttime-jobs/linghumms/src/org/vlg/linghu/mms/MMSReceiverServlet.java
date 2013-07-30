package org.vlg.linghu.mms;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vlg.linghu.SingleThreadPool;
import org.vlg.linghu.mybatis.bean.MmsSendMessageExample;
import org.vlg.linghu.mybatis.bean.MmsSendMessageWithBLOBs;
import org.vlg.linghu.mybatis.mapper.MmsSendMessageMapper;
import org.vlg.linghu.vac.VACNotifyHandler;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.service.MM7ReceiveServlet;

public class MMSReceiverServlet extends MM7ReceiveServlet {
	
	private	 static final Logger logger = LoggerFactory.getLogger(MMSReceiverServlet.class);
	
	@Autowired
	MmsSendMessageMapper mmsSendMessageMapper;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		WebApplicationContext sprintContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
		sprintContext.getAutowireCapableBeanFactory().autowireBean(this);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(
				this, getServletContext());
		MM7Config mm7Config = new MM7Config(MMSSendTest.class.getResource(
				"/mm7Config.xml").getFile());
		mm7Config.setConnConfigName(MMSSendTest.class.getResource(
				"/mm7ConnConfig.xml").getFile());
		this.Config = mm7Config;
	}



	public MM7VASPRes doDeliver(MM7DeliverReq request) {
		logger.info("收到手机" + request.getSender() + "提交的彩信，标题为："
				+ request.getSubject());
		MM7DeliverRes res = new MM7DeliverRes();
//		res.setServiceCode("服务代码"); // 设置ServiceCode，可选
		res.setTransactionID(request.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		// res.setStatusText(statusText)//设置所用状态的文本说明， 应限定请求状态，可选

		return res;
	}

	public MM7VASPRes doDeliveryReport(final MM7DeliveryReportReq request) {
		Runnable run = new Runnable(){
			public void run(){
				logger.info("收到彩信状态报告: "+VACNotifyHandler.getBeanInfo(request));		
				String messageId = request.getMessageID();
				int status = request.getMMStatusErrCode();
				MmsSendMessageExample ex = new MmsSendMessageExample();
				ex.createCriteria().andMsgidEqualTo(messageId);
				MmsSendMessageWithBLOBs msg = new MmsSendMessageWithBLOBs();
				msg.setMsgid(messageId);
				msg.setSendStatus(status+90000);
				mmsSendMessageMapper.updateByExampleSelective(msg, ex);
			}
		};
		SingleThreadPool.execute(run);
		
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setTransactionID(request.getTransactionID());
		
		return res;
	}

	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq) {
		logger.info("收到彩信阅读报告: "+VACNotifyHandler.getBeanInfo(mm7ReadReplyReq));
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		return res;
	}
}
