package nci.gps.message.jms;

import junit.framework.TestCase;

public class Consumer001Test extends TestCase {

	public Consumer001Test(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testConsumer001() {
		try {
			Consumer001.getInstance();
		} catch (NoSubjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] ss){
		try {
			Consumer001 c = Consumer001.getInstance();
			c.start();
		} catch (NoSubjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
