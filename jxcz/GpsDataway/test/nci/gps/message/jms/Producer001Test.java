package nci.gps.message.jms;

import javax.jms.JMSException;

import junit.framework.TestCase;

public class Producer001Test extends TestCase {

	public Producer001Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testProducer001() {
		try {
			Producer001 s = Producer001.getInstance();
			s.start();
			while(true){
				s.addBytesIntoSendQueue(null);
			}
		} catch (NoSubjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutofBytesMessageIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProducerNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
