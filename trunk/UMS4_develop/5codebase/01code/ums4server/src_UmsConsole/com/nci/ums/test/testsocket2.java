package com.nci.ums.test;

import java.io.*;
import java.net.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class testsocket2 {
  private ServerSocket socket;
  public void initSocket(int listenPort) {
    try {
      socket = new ServerSocket(listenPort, 5);
      Socket incomingConnection = null;
      while (true) {
        incomingConnection = socket.accept();
        System.out.println("accept a connection!");
        handleConnection(incomingConnection);
      }
    }
    catch (BindException e) {
      System.out.println("Unable to bind to port " + listenPort);
    }
    catch (IOException e) {
      System.out.println("Unable to instantiate a ServerSocket on port: " +
                         listenPort);
    }
  }

  public void handleConnection(Socket incomingConnection) {
    try {
      InputStream inputFromSocket = incomingConnection.getInputStream();
      PrintWriter  out = new PrintWriter(incomingConnection.getOutputStream());

      BufferedReader streamReader =
          new BufferedReader(new InputStreamReader(inputFromSocket));

      String line = null;
      while ( (line = streamReader.readLine()) != null) {
        if(line.equals("quit"))
          break;
        System.out.println("line=" + line);
        out.println(line);
        out.flush();
      }
      streamReader.close();

    }
    catch (Exception e) {
      System.out.println("Error handling a client: " + e);
    }
  }

  public testsocket2() {
    System.out.println("testsocket2 begin init!");
    System.out.println("listening to 9000!");
    initSocket(9000);
  }

  public static void main(String[] args) {
    testsocket2 s = new testsocket2();
  }
}
