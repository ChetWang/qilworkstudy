using System;
using System.Collections.Generic;
using System.Text;

namespace MemoryServer
{
    public class MessageCenter
    {
        public static byte[] GetUserLoginMessage(string userName, string password)
        {
            password.ToUpper();
            byte[] bpassword = EncryMD5.GetMD5EncryCode32Upper(password);


            byte[] buserName = Encoding.GetEncoding("UTF-8").GetBytes(userName);
            int nCount = 1 + 1 + buserName.Length + 32;
            byte[] data = new byte[nCount];

            data[0] = DefDataHead.UserLoging;
            data[1] = (byte)(buserName.Length);
            Array.Copy(buserName, 0, data, 2, buserName.Length);
            byte b = 0;
            int ni = bpassword.Length;
            int nS = 1 + 1 + buserName.Length;
            while (ni < 32)
            {
                data[nS] = b;
                nS++;
                ni++;
            }
            Array.Copy(bpassword, 0, data, nS, bpassword.Length);
            //string sinfo = Encoding.UTF8.GetString(data, 0, nCount);
            return data;
        }
        public static byte[] GetLogionOutMessage()
        {
            byte[] b = new byte[1];
            b[0] = DefDataHead.UserLogOut;
            // string sinfo = Encoding.UTF8.GetString(b, 0, 1);
            return b;
        }
        public static bool GetLogionResult(byte[] bResult, int nLen, out string sResult)
        {
            if (bResult == null || nLen != 2)
            {
                sResult = "Server Didn't Return A Right Data!";
            }
            else
            {
                if (bResult[0] != DefDataHead.UserLogResult)
                {
                    sResult = "Server Didn't Handle Login!";
                }
                else
                {

                    if (bResult[1] == 1)
                    {
                        sResult = "Login Successful!";
                        return true;
                    }
                    else
                        sResult = "Login Fail!";
                }
            }
            return false;
        }
        static int _cmdID = 0;
        public static int GetLastCmdID()
        {
            return _cmdID-1;
        }
        public static int CommandID
        {
            get
            {
                return _cmdID++;
            }
        }
        public static byte[] GetUserOperaterMessage(short itype, string sData)
        {
            byte[] bData = Encoding.GetEncoding("UTF-8").GetBytes(sData);

            if (bData.Length > Int32.MaxValue)
            {
                Console.WriteLine("Section is too long");
                return null;
            }
            int nCount = 1 + 2 + 2 + 4 + 4 + bData.Length;
            byte[] data = new byte[nCount];

            //head
            data[0] = DefDataHead.UserOperation;
            //type
            byte[] btype = BitConverter.GetBytes(itype);
            Array.Reverse(btype);
            Array.Copy(btype, 2, data, 1, 2);
            //command
            
            byte[] bCommand = BitConverter.GetBytes(CommandID);
            Array.Reverse(bCommand);
            Array.Copy(bCommand, 2, data, 3, 2);
            //len
            byte[] bLen = BitConverter.GetBytes(bData.Length);
            Array.Reverse(bLen);
            Array.Copy(bLen, 0, data, 9, 4);

            //data
            Array.Copy(bData, 0, data, 13, bData.Length);
            //string sinfo = Encoding.UTF8.GetString(data, 0, nCount);
            return data;
        }
        
    }
}
