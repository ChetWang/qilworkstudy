using System;
using System.Collections.Generic;
using System.Text;

namespace MemoryServer
{
    /// <summary> 
    /// 数据报文分析器,通过分析接收到的原始数据,得到完整的数据报文. 
    //头（0x71），功能标记（2字节），Session ID（4字节），数据长度（4字节），用户自定义数据集合。
    //头	功能标记（整数）	命令序号（整数）	Session ID（整数）	数据长度（整数）	用户数据
    //1（0x71）	2字节	2字节	4字节	4字节	自定义数据，最大长度是整数最大表示值Integer.MAX_VALUE

    /// </summary> 

    public class DatagramResolver
    {

        public DatagramResolver()
        {

        }
        /// <summary>
        /// 功能标记
        /// </summary>
        short _function;
        public short Function
        {
            get
            {
                return _function;
            }
        }
        /// <summary>
        /// 命令序号
        /// </summary>
        short _cmdIndex;
        public short CommandIndex
        {
            get
            {
                return _cmdIndex;
            }
        }
        /// <summary>
        /// Session ID（整数）
        /// </summary>
        int _sessionIndex;
        public int SessionIndex
        {
            get
            {
                return _sessionIndex;
            }
        }
        /// <summary>
        /// 接收的具体数据
        /// </summary>
        byte[] _datagram;
        public byte[] Datagram
        {
            get
            {
                return _datagram;
            }
        }
        /// <summary>
        /// 是否是错误返回
        /// 头	        功能标记	命令序号（整数）	错误码
        ///1（0x72）	2字节	    2字节	            系统定义，参照resources/error.conf

        /// </summary>
        bool _isError = false;
        public bool Error
        {
            get
            {
                return _isError;
            }
        }
        /// <summary> 
        /// 解析报文 
        /// </summary> 
       
        public virtual void Resolve(byte[] rawDatagram,int nLen)
        {
            if (nLen < 5 || nLen >= rawDatagram.Length)
                System.Diagnostics.Trace.WriteLine("无效报文");
            byte bFlag = rawDatagram[0];

            if (bFlag == DefDataHead.ErrorHead)
            {
                _isError = true;
                /// <summary>
                /// 是否是错误返回,高位在前
                /// 头	        功能标记	命令序号（整数）	错误码
                ///1（0x72）	2字节	    2字节	            系统定义，参照resources/error.conf
                byte[] data = new byte[2];
                Array.Copy(rawDatagram, 1, data, 0, 2);
                Array.Reverse(data);
                _function = BitConverter.ToInt16(data, 0);
                Array.Copy(rawDatagram, 3, data, 0, 2);
                Array.Reverse(data);
                _cmdIndex = BitConverter.ToInt16(data, 0);
                _datagram = new byte[nLen - 5];
                Array.Copy(rawDatagram, 5, _datagram, 0, nLen - 5);
            }
            else if (bFlag == DefDataHead.UserOperation)
            {
                _isError = false;
                byte[] data = new byte[4];
                Array.Copy(rawDatagram, 1, data, 0, 2);
                Array.Reverse(data);
                _function = BitConverter.ToInt16(data, 0);

                Array.Copy(rawDatagram, 3, data, 0, 2);
                Array.Reverse(data);
                _cmdIndex = BitConverter.ToInt16(data, 0);
                
                Array.Copy(rawDatagram, 5, data, 0, 4);
                Array.Reverse(data);
                _sessionIndex = BitConverter.ToInt32(data, 0) ;
                Array.Copy(rawDatagram, 9, data, 0, 4);
                Array.Reverse(data);
                int nDataLen = BitConverter.ToInt32(data, 0);
                _datagram = new byte[nDataLen];
                Array.Copy(rawDatagram, 13, _datagram, 0, nDataLen);
            }
            else
                System.Diagnostics.Trace.WriteLine("无效报文");
        }

    }
}
