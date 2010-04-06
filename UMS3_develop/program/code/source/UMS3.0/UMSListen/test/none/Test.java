package none;

import java.io.IOException;

import com.nci.ums.channel.inchannel.email.EmailMsgPlus;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.client.DuplicateServiceException;
import com.nci.ums.v3.service.client.ReceiveEvent;
import com.nci.ums.v3.service.client.UMSDisconnectException;
import com.nci.ums.v3.service.client.UMSReceiver_Socket;
import com.thoughtworks.xstream.XStream;

public class Test {

    public static void main(String[] args) {
        try {
            MyReceiver receiver = null;
            try {
                receiver = new MyReceiver("ums.props");
                System.out.println("¿ªÊ¼¼àÌý...");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (DuplicateServiceException e) {
                
                e.printStackTrace();
            }
            receiver.startListening();
        } catch (UMSDisconnectException ex) {
            ex.printStackTrace();
        } catch (DuplicateServiceException ex) {
            ex.printStackTrace();
        }

    }
}

class MyReceiver extends UMSReceiver_Socket {

    int count = 0;
    XStream xstream;

    public MyReceiver(String propPath) throws IOException, DuplicateServiceException {
        super(propPath);
        xstream = new XStream();
        xstream.alias("UMSMsg", UMSMsg.class);
        xstream.alias("BasicMsg", BasicMsg.class);
        xstream.alias("MsgAttachment", MsgAttachment.class);
        xstream.alias("MsgContent", MsgContent.class);
        xstream.alias("Participant", Participant.class);
        xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
    }

    public void onUMSMsg(ReceiveEvent evt) {
//        System.out.print(count++);
        String xml = evt.getReceivedObj().toString();
        BasicMsg[] basics = (BasicMsg[]) xstream.fromXML(xml);
        System.out.println(": Count=" + basics.length);
//        count = count+basics.length;
//        System.out.println(count);
    }
}
