using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace MemoryServer
{
  
    [Guid("C47F02F4-A6FC-4ad1-9D13-5CAEC6ACD5EC")]
    [ClassInterface(ClassInterfaceType.AutoDispatch)]
    [ComVisible(true)]
    public class MemoryClient
    {
        /// <summary>
        /// 与服务器建立连接
        /// </summary>
        /// <param name="ip">ip地址</param>
        /// <param name="pos">端口号</param>
        public void ConnectServer(string ip, int pos)
        {
            SynchronousClient.ConnectToServer(ip, pos);
        }
        /// <summary>
        /// 用户登录
        /// </summary>
        /// <param name="name">名称</param>
        /// <param name="password">密码</param>
        /// <returns>是否登录成功 true登录成功，false失败</returns>
        public bool Login(string name, string password)
        {
            byte[] b = MessageCenter.GetUserLoginMessage(name, password);
            int nRevLen = 0;
            byte[] bRev = SynchronousClient.SendSyncMessage(b, out nRevLen);
            string sResult;
            bool bLogin = MessageCenter.GetLogionResult(bRev, nRevLen, out sResult);

            System.Diagnostics.Trace.WriteLine(sResult);
            return bLogin;
        }
        /// <summary>
        /// 登出服务器
        /// </summary>
        public void Loginout()
        {
            byte[] b = MessageCenter.GetLogionOutMessage();
            int iRev;
            SynchronousClient.SendSyncMessage(b, out iRev);

        }
        /// <summary>
        /// 查询操作
        /// </summary>
        /// <param name="selcmd">查询语句</param>
        /// <returns>返回值</returns>
        public byte[] SelectOperation(string selcmd)
        {
           
            byte[] b =MessageCenter.GetUserOperaterMessage(2001, selcmd);
            int i;
            return SynchronousClient.SendSyncMessage(b, out i);
        }
        /// <summary>
        /// 更新操作
        /// </summary>
        /// <param name="upcmd">更新语句</param>
        /// <returns>返回值</returns>
        public byte[] UpdateOperation(string upcmd)
        {
            byte[] b = MessageCenter.GetUserOperaterMessage(2002, upcmd);
            int i;
            return SynchronousClient.SendSyncMessage(b, out i);
        }
        /// <summary>
        /// 插入操作
        /// </summary>
        /// <param name="incmd">插入语句</param>
        /// <returns>返回值</returns>
        public byte[] InsertOperation(string incmd)
        {
            byte[] b = MessageCenter.GetUserOperaterMessage(2003, incmd);
            int i;
            return SynchronousClient.SendSyncMessage(b, out i);
        }
        /// <summary>
        /// 发送异步消息
        /// </summary>
        /// <param name="message"></param>
        public int AsynchronousMessage(short mesType, string message)
        {
            byte[] info = MessageCenter.GetUserOperaterMessage(mesType, message);
            AsynchronousClient.StartClient();
            AsynchronousClient.Send(info);
            return MessageCenter.GetLastCmdID();
        }
        /// <summary>
        /// 获取异步消息
        /// </summary>
        /// <param name="message"></param>
        public byte[] RevAsynchronousMessage(int cmdID)
        {
            DatagramResolver dr = AsynchronousClient.GerRevMessageByCmdID(cmdID);
            return dr.Datagram;//是否错误如何解析？或都由外部做解析操作？
        }
        /// <summary>
        /// 发送同步信息
        /// </summary>
        /// <param name="mesType"></param>
        /// <param name="message"></param>
        /// <returns></returns>
        public byte[] SynchronousMessage(short mesType,string message)
        {

            byte[] info = MessageCenter.GetUserOperaterMessage(mesType, message);
            int i;
            return SynchronousClient.SendSyncMessage(info, out i);
        }
        /// <summary>
        /// 设置接收时限
        /// </summary>
        /// <param name="RevTime">接收时限，单位为秒</param>
        public static void SetRecTime(int RevTime)
        {
            DefDataSet.SetRecTime(RevTime);
        }
        /// <summary>
        /// 设置心跳时间
        /// </summary>
        /// <param name="HeadTime">心跳时间，单位为秒</param>
        public static void SetHeadTime(int HeadTime)
        {
            DefDataSet.SetHeadTime(HeadTime);
        }
        /// <summary>
        /// 设置接收数据最大长度，默认为64k
        /// </summary>
        /// <param name="RevDataSize">长度，单位为k</param>
        public static void SetRevDataSize(int RevDataSize)
        {
            DefDataSet.SetRevDataSize(RevDataSize);
        }
    }
}
