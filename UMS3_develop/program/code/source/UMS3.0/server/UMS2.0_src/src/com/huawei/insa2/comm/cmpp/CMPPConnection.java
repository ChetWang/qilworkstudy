/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.huawei.insa2.comm.cmpp;

import com.huawei.insa2.comm.*;
import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPConnectMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPConnectRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPTerminateMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Resource;
import java.io.*;
import java.util.Date;

// Referenced classes of package com.huawei.insa2.comm.cmpp:
//            CMPPWriter, CMPPReader, CMPPTransaction, CMPPConstant

public class CMPPConnection extends PSocketConnection {

	public CMPPConnection(Args args) {
		degree = 0;
		hbnoResponseOut = 3;
		source_addr = null;
		hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
		source_addr = args.get("source-addr", "huawei");
		version = args.get("version", 1);
		shared_secret = args.get("shared-secret", "");
		CMPPConstant.debug = args.get("debug", false);
		CMPPConstant.initConstant(getResource());
		init(args);
	}

	protected PWriter getWriter(OutputStream out) {
		return new CMPPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new CMPPReader(in);
	}

	public int getChildId(PMessage message) {
		CMPPMessage mes = (CMPPMessage) message;
		int sequenceId = mes.getSequenceId();
		if (mes.getCommandId() == 5 || mes.getCommandId() == 8
				|| mes.getCommandId() == 2)
			return -1;
		else
			return sequenceId;
	}

	public PLayer createChild() {
		return new CMPPTransaction(this);
	}

	public int getTransactionTimeout() {
		return super.transactionTimeout;
	}

	public Resource getResource() {
		try {
			Resource resource = new Resource(getClass(), "resource");
			return resource;
		} catch (IOException e) {
			e.printStackTrace();
		}
		Resource resource1 = null;
		return resource1;
	}

	public synchronized void waitAvailable() {
		try {
			if (getError() == PSocketConnection.NOT_INIT)
				wait(super.transactionTimeout);
		} catch (InterruptedException interruptedexception) {
		}
	}

	public void close() {
		try {
			CMPPTerminateMessage msg = new CMPPTerminateMessage();
			send(msg);
		} catch (PException pexception) {
		}
		super.close();
	}

	protected void heartbeat() throws IOException {
		CMPPTransaction t = (CMPPTransaction) createChild();
		CMPPActiveMessage hbmes = new CMPPActiveMessage();
		t.send(hbmes);
		t.waitResponse();
		CMPPActiveRepMessage rsp = (CMPPActiveRepMessage) t.getResponse();
		if (rsp == null) {
			degree++;
			if (degree == hbnoResponseOut) {
				degree = 0;
				throw new IOException(CMPPConstant.HEARTBEAT_ABNORMITY);
			}
		} else {
			degree = 0;
		}
		t.close();
	}

	protected synchronized void connect() {
		super.connect();
		if (!available())
			return;
		CMPPConnectMessage request = null;
		CMPPConnectRepMessage rsp = null;
		try {
			request = new CMPPConnectMessage(source_addr, version,
					shared_secret, new Date());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			close();
			setError(CMPPConstant.CONNECT_INPUT_ERROR);
		}
		CMPPTransaction t = (CMPPTransaction) createChild();
		try {
			t.send(request);
			PMessage m = super.in.read();
			onReceive(m);
		} catch (IOException e) {
			e.printStackTrace();
			close();
			setError(String.valueOf(CMPPConstant.LOGIN_ERROR)
					+ String.valueOf(explain(e)));
		}
		rsp = (CMPPConnectRepMessage) t.getResponse();
		if (rsp == null) {
			close();
			setError(CMPPConstant.CONNECT_TIMEOUT);
		}
		t.close();
		if (rsp != null && rsp.getStatus() != 0) {
			close();
			if (rsp.getStatus() == 1)
				setError(CMPPConstant.STRUCTURE_ERROR);
			else if (rsp.getStatus() == 2)
				setError(CMPPConstant.NONLICETSP_ID);
			else if (rsp.getStatus() == 3)
				setError(CMPPConstant.SP_ERROR);
			else if (rsp.getStatus() == 4)
				setError(CMPPConstant.VERSION_ERROR);
			else
				setError(CMPPConstant.OTHER_ERROR+"status:"+rsp.getStatus());
		}
		notifyAll();
	}

	private int degree;
	private int hbnoResponseOut;
	private String source_addr;
	private int version;
	private String shared_secret;
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server
 * \lib\smproxy.jar Total time: 46 ms Jad reported messages/errors: Exit status:
 * 0 Caught exceptions:
 */