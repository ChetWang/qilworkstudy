package org.vlg.linghu.mms;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.service.MM7ReceiveServlet;

public class MMSReceiverServlet extends MM7ReceiveServlet {

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		MM7Config mm7Config = new MM7Config("F: \\mm7Config.xml");
		
	}

	public MM7VASPRes doDeliver(MM7DeliverReq request) {
		System.out.println("�յ��ֻ�" + request.getSender() + "�ύ����Ϣ������Ϊ��"
				+ request.getSubject());
		MM7DeliverRes res = new MM7DeliverRes();
		res.setServiceCode("�������"); // ����ServiceCode����ѡ
		res.setTransactionID(request.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		// res.setStatusText(statusText)//��������״̬���ı�˵���� Ӧ�޶�����״̬����ѡ

		return res;
	}

	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq request) {
		MM7DeliverRes res = new MM7DeliverRes();
		return res;
	}

	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq) {
		mm7ReadReplyReq.getStatusText();
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		return res;
	}
}
