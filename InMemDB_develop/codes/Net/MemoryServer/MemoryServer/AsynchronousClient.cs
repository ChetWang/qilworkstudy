using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Net;
using System.Net.Sockets;

namespace MemoryServer
{
    
    public class AsynchronousClient
    {
        // The port number for the remote device. 
        private const int port = 11000;
        // ManualResetEvent instances signal completion. 
        private static ManualResetEvent connectDone =
        new ManualResetEvent(false);
        private static ManualResetEvent sendDone =
        new ManualResetEvent(false);
        private static ManualResetEvent receiveDone =
        new ManualResetEvent(false);
       
        private static Socket client;
        /// <summary> 
        /// 接收数据缓冲区 
        /// </summary> 
        private static byte[] _recvdatabuffer;
        /// <summary> 
        /// 接收数据缓冲区 
        /// </summary> 
        private static byte[] _finalbuffer;
        private static StringBuilder sbuffer;
        
        public static void StartClient()
        {
            // Connect to a remote device. 
            
            try
            {
                IPEndPoint iep = new IPEndPoint(IPAddress.Parse(LocalServer.IP), LocalServer.PORT);
                // Create a TCP/IP socket. 
                client = new Socket(AddressFamily.InterNetwork,
                SocketType.Stream, ProtocolType.Tcp);
                // Connect to the remote endpoint. 
                client.BeginConnect(iep,
                new AsyncCallback(ConnectCallback), client);
                connectDone.WaitOne();

                //建立连接后应该立即接收数据 
               
                Receive();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
        public static void Close()
        {
            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }
        public static void ConnectCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object. 
                Socket clientConnect = (Socket)ar.AsyncState;
                // Complete the connection. 
                clientConnect.EndConnect(ar);
               
                connectDone.Set();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
        public static void Receive()
        {
            try
            {
                //建立连接后应该立即接收数据 
                if (_recvdatabuffer == null)
                    _recvdatabuffer = new byte[DefDataSet.RevDatagramSize];
                if(_finalbuffer == null)
                _finalbuffer = new byte[DefDataSet.RevDatagramSize];
                if (sbuffer == null)
                    sbuffer = new StringBuilder();
                sbuffer.Remove(0,sbuffer.Length);
                client.BeginReceive(_recvdatabuffer, 0,
                DefDataSet.RevDatagramSize, SocketFlags.None,
                new AsyncCallback(ReceiveCallback), client);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
        static List<DatagramResolver> listRev = new List<DatagramResolver>();
        public static void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                Socket clientRec = (Socket)ar.AsyncState;
                int bytesRead = clientRec.EndReceive(ar);
                if (bytesRead > 0)
                {
                    // There might be more data, so store the data received so far.
                    
                    sbuffer.Append(Encoding.ASCII.GetString(_recvdatabuffer, 0, bytesRead));
                    client.BeginReceive(_recvdatabuffer, 0,
               DefDataSet.RevDatagramSize, SocketFlags.None,
               new AsyncCallback(ReceiveCallback), client);
                }
                else
                {
                    // All the data has arrived; put it in response. 
                    if (sbuffer.Length > 0)
                    {
                        _finalbuffer = Encoding.ASCII.GetBytes(sbuffer.ToString());
                        DatagramResolver dr = new DatagramResolver();
                        //需调试，无法预知结果如何
                        dr.Resolve(_finalbuffer, sbuffer.Length);
                        listRev.Add(dr);

                    }
                    // Signal that all bytes have been received. 
                   
                    receiveDone.Set();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
        public static DatagramResolver GerRevMessageByCmdID(int cmdID)
        {
            if (listRev == null || listRev.Count < 1) return null;
            foreach (DatagramResolver dr in listRev)
            {
                if (dr.CommandIndex == cmdID)
                    return dr;
            }
            return null;
        }
        /// <summary>
        /// 发送
        /// </summary>
        /// <param name="client"></param>
        /// <param name="data"></param>
        public static void Send(byte[] byteData)
        {
            client.BeginSend(byteData, 0, byteData.Length, 0,
            new AsyncCallback(SendCallback), client);
        }
        public static void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object. 
                Socket clientSend = (Socket)ar.AsyncState;
                // Complete sending the data to the remote device. 
                int bytesSent = clientSend.EndSend(ar);
                System.Diagnostics.Trace.Write(String.Format("Sent {0} bytes to server.", bytesSent));
                // Signal that all bytes have been sent. 
                sendDone.Set();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
    }
}
