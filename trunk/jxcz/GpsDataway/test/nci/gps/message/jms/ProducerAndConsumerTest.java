package nci.gps.message.jms;

import javax.swing.JFrame;

public class ProducerAndConsumerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Consumer001 c = Consumer001.getInstance();
			c.start();
			Producer001 p = Producer001.getInstance();
			p.start();
			JFrame f = new JFrame("s");
			f.setVisible(true);
		} catch (NoSubjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
