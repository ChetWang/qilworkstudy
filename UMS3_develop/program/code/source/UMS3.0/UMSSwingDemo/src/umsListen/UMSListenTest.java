/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package umsListen;

import com.nci.ums.v3.service.client.DuplicateServiceException;
import com.nci.ums.v3.service.client.ReceiveEvent;
import com.nci.ums.v3.service.client.UMSReceiver_Socket;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class UMSListenTest {
   public static void main(String[] args) {
		TestReceiver receiver;
		try {
			receiver = new TestReceiver("ums.props");
			receiver.startListening();
		} catch (IOException e) {
			System.out.println(e.getMessage());			
		} catch (DuplicateServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

class TestReceiver extends UMSReceiver_Socket {

	public TestReceiver(String propPath) throws IOException,  DuplicateServiceException {
		super(propPath);
	}

	public void onUMSMsg(ReceiveEvent evt) {
		System.out.println("Source:"+evt.getSource());
//		System.out.println("ReceiveObj:"+evt.getReceivedObj());
	}

}

