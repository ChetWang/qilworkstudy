using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;
using System.Net;

namespace MemoryServer
{
    public class SynchronousClient
    {
       
        static Socket NowServerCenter = null;
       
        static System.Timers.Timer timeHead = new System.Timers.Timer();
        /// <summary> 
        /// 接收数据缓冲区大小64k 
        /// </summary> 
        public const int defaultbuffersize = 64 * 1024;
        public static bool ConnectToServer(string ip, int port)
        {
            if (NowServerCenter != null)
                NowServerCenter.Close();
            //IPHostEntry ipHostInfo = Dns.GetHostEntry(ip);
            IPAddress ipAddress = IPAddress.Parse(ip);//ipHostInfo.AddressList[ipHostInfo.AddressList.Length - 1];
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress, port);


            // Create a TCP/IP socket.
            NowServerCenter = new Socket(AddressFamily.InterNetwork,
                SocketType.Stream, ProtocolType.Tcp);

            try
            {
                NowServerCenter.ReceiveBufferSize = DefDataSet.RevDatagramSize;
                NowServerCenter.ReceiveTimeout = DefDataSet.RevTime;
                NowServerCenter.Connect(localEndPoint);//连接到服务器
                LocalServer.IP = ip;
                LocalServer.PORT = port;
                timeHead.Interval = DefDataSet.HeadTime;
                timeHead.Elapsed += new System.Timers.ElapsedEventHandler(HeadTime);//到达时间的时候执行事件； 
                timeHead.AutoReset = true;//设置是执行一次（false）还是一直执行(true)； 
                timeHead.Enabled = true;//是否执行System.Timers.Timer.Elapsed事件；

                return true;
            }
            catch (Exception e)
            {
                System.Diagnostics.Trace.Write(e.Message);
                return false;
            }
           
        }
        public static void HeadTime(object source, System.Timers.ElapsedEventArgs e)
        {
            System.Diagnostics.Trace.WriteLine("Heartbeating");
            byte[] b = new byte[1];
            b[0] = DefDataHead.Heartbeat;
            //string sinfo = Encoding.UTF8.GetString(b, 0, 1);
            int iLen = 0;
            byte[] nR = SendSyncMessage(b, out iLen);
            if (iLen == 0)
            {
                Console.WriteLine("Connect lost ! Reconnecting ...");
                if (!ConnectToServer(LocalServer.IP, LocalServer.PORT))
                {
                    Console.WriteLine("Connect fail");
                    return;
                }
            }
        }



        public static void CloseServer()
        {
            if (NowServerCenter != null)
                NowServerCenter.Close();

        }

        public static byte[] SendSyncMessage(byte[] sdata, out int nRevLen)
        {
            nRevLen = 0;
            try
            {

                if (!isConnected)
                {
                    Console.WriteLine("Connect lost ! Reconnecting ...");
                    if (!ConnectToServer(LocalServer.IP, LocalServer.PORT))
                    {
                        Console.WriteLine("Connect fail");
                        return null;
                    }
                }
                //byte[] sdata = Encoding.UTF8.GetBytes(sData);//把字符串编码为字节
                string sinfo = Encoding.UTF8.GetString(sdata, 0, sdata.Length);
                System.Diagnostics.Trace.WriteLine("Send Message:" + sinfo);
                NowServerCenter.Send(sdata, sdata.Length, 0);//发送信息

                string recvStr = "";
                byte[] recvBytes = new byte[1024];
                nRevLen = NowServerCenter.Receive(recvBytes, recvBytes.Length, 0);//从服务器端接受返回信息
                recvStr += Encoding.UTF8.GetString(recvBytes, 0, nRevLen);
                System.Diagnostics.Trace.WriteLine("\nClient get message:{0}", recvStr);//显示服务器返回信息

                return recvBytes;
            }
            catch (Exception e)
            {
                Console.WriteLine("Server Expect Out Link!\n"+e.Message);
            }
            return null;
        }
        
        public static bool isConnected
        {
            get
            {
                if (NowServerCenter == null || !NowServerCenter.Connected)
                {
                    return false;
                }
                else
                    return true;
            }
        }
    }
}
