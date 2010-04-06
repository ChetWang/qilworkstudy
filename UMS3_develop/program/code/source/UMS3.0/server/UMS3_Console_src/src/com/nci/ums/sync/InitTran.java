package com.nci.ums.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nci.ums.sync.impl.UMSSynOrg;
import com.nci.ums.sync.impl.UMSSynPer;
import com.nci.ums.sync.ldap.LdapOrgResult;
import com.nci.ums.sync.ldap.LdapPerResult;

public class InitTran extends HttpServlet {
	public void init() throws ServletException {
		new SocketT().start();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
}

class SocketT extends Thread {
	Socket s;

	ObjectInputStream ois;

	ObjectOutputStream oos;

	ServerSocket server = null;

	public SocketT() {
	}

	

	

	public void run() {
		try {
			server = new ServerSocket(10023);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		while (true) {
			try {
				
				s = server.accept();
				ois = new ObjectInputStream(s.getInputStream());
				String xml = (String) ois.readObject();
				int a = com.nci.ums.sync.util.XmlUtil.getXmlType(xml);
				if (2 == a) {
					LdapPerResult lp = com.nci.ums.sync.util.XmlUtil
							.parsePerXml(xml);
					UMSSynPer umssynper = new UMSSynPer();
					umssynper.doSync(lp);
				} else if (1 == a) {
					LdapOrgResult lp = com.nci.ums.sync.util.XmlUtil
							.parseOrgXml(xml);
					UMSSynOrg umssynorg = new UMSSynOrg();
					umssynorg.doSync(lp);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
//			new ReadSocket(s).start();
		}
	}
}
class ReadSocket extends Thread{
	Socket s;
	ObjectInputStream ois;
	public ReadSocket(Socket s) {
		this.s = s;
	}
	public void run() {
	  while(true) {
		try {			
			ois = new ObjectInputStream(s.getInputStream());
			String xml = (String) ois.readObject();
			int a = com.nci.ums.sync.util.XmlUtil.getXmlType(xml);
			if (2 == a) {
				LdapPerResult lp = com.nci.ums.sync.util.XmlUtil
						.parsePerXml(xml);
				UMSSynPer umssynper = new UMSSynPer();
				umssynper.doSync(lp);
			} else if (1 == a) {
				LdapOrgResult lp = com.nci.ums.sync.util.XmlUtil
						.parseOrgXml(xml);
				UMSSynOrg umssynorg = new UMSSynOrg();
				umssynorg.doSync(lp);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Server:IOException");
			e.printStackTrace();
		
		}
	}
  }
}
