package org.vlg.linghu.mms;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		MM7Config mm7Config = new MM7Config(MMSSendTest.class.getResource(
				"/mm7Config.xml").getFile());
		mm7Config.setConnConfigName(MMSSendTest.class.getResource(
				"/mm7ConnConfig.xml").getFile());
		this.Config = mm7Config;
	}



	public MM7VASPRes doDeliver(MM7DeliverReq request) {
		logger.debug("�յ��ֻ�" + request.getSender() + "�ύ�Ĳ��ţ�����Ϊ��"
				+ request.getSubject());
		MM7DeliverRes res = new MM7DeliverRes();
//		res.setServiceCode("�������"); // ����ServiceCode����ѡ
		res.setTransactionID(request.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		// res.setStatusText(statusText)//��������״̬���ı�˵���� Ӧ�޶�����״̬����ѡ

		return res;
	}

	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq request) {
		logger.debug("�յ�����״̬����: "+VACNotifyHandler.getBeanInfo(request));
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setTransactionID(request.getTransactionID());
		return res;
	}

	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq) {
		logger.debug("�յ������Ķ�����: "+VACNotifyHandler.getBeanInfo(mm7ReadReplyReq));
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		return res;
	}
}
