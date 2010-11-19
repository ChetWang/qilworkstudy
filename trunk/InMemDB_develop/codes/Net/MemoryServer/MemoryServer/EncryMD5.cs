using System;
using System.Collections.Generic;
using System.Text;
using System.Security.Cryptography;

namespace MemoryServer
{
    public class EncryMD5
    {

        public static byte[] GetMD5EncryCode(string str)
        {
            MD5 m = new MD5CryptoServiceProvider();
            byte[] s = m.ComputeHash(UnicodeEncoding.UTF8.GetBytes(str));
            return s;
        }
        public static byte[] GetMD5EncryCode32Upper(string str)
        {
            string cl = str;
            string pwd = "";
            MD5 md5 = MD5.Create();//实例化一个md5对像
            // 加密后是一个字节类型的数组，这里要注意编码UTF8/Unicode等的选择　
            byte[] s = md5.ComputeHash(Encoding.UTF8.GetBytes(cl));
            // 通过使用循环，将字节类型的数组转换为字符串，此字符串是常规字符格式化所得
            for (int i = 0; i < s.Length; i++)
            {
                // 将得到的字符串使用十六进制类型格式。格式后的字符是小写的字母，如果使用大写（X）则格式后的字符是大写字符 

                pwd = pwd + s[i].ToString("X");

            }
            pwd = pwd.ToUpper();
            while (pwd.Length < 32)
            {
                pwd = pwd.Insert(0, "0");
            }
            byte[] byteData = Encoding.UTF8.GetBytes(pwd);
            return byteData;

        }

    }
}
