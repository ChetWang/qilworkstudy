package com.nci.ums.sichuan.ctcc;

import com.ctcc.www.service.Ctcc_ema_wbsStub;
import com.ctcc.www.service.Ctcc_ema_wbsStub.MessageFormat;
import com.ctcc.www.service.Ctcc_ema_wbsStub.SendMethodType;
import com.ctcc.www.service.Ctcc_ema_wbsStub.SendSmsRequest;
import com.ctcc.www.service.Ctcc_ema_wbsStub.SendSmsResponse;
import com.ctcc.www.service.PolicyException;
import com.ctcc.www.service.ServiceException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;

public class SC_WS_Test
{
  public void test()
  {
    try
    {
      Ctcc_ema_wbsStub stub = new Ctcc_ema_wbsStub();
      Ctcc_ema_wbsStub.SendSmsRequest request = new Ctcc_ema_wbsStub.SendSmsRequest();

      URI[] destinationAddresses = { new URI("tel:15308186058") };
      request.setDestinationAddresses(destinationAddresses);

      request.setApplicationID("test");

      request.setMessage("CDMA SMS Test2 from ÐÂÊÀ¼Í£¬ just for test£¡");
      request.setEcID("dh");
      request.setExtendCode("");
      request.setMessageFormat(Ctcc_ema_wbsStub.MessageFormat.GB18030);
      request.setSendMethod(Ctcc_ema_wbsStub.SendMethodType.Normal);

      Ctcc_ema_wbsStub.SendSmsResponse response = stub.sendSms(request);
      System.out.println(response.getRequestIdentifier());
    }
    catch (AxisFault e) {
      e.printStackTrace();
    }
    catch (RemoteException e) {
      e.printStackTrace();
    }
    catch (PolicyException e) {
      e.printStackTrace();
    }
    catch (ServiceException e) {
      e.printStackTrace();
    }
    catch (URI.MalformedURIException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] xx) {
    new SC_WS_Test().test();
  }
}